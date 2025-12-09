package pt.iade.ei.runupsetup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import pt.iade.ei.runupsetup.models.CorridaGeradaDto
import pt.iade.ei.runupsetup.models.PredefinedRouteDto
import pt.iade.ei.runupsetup.network.RetrofitClient
import pt.iade.ei.runupsetup.ui.RunMapActivity
import pt.iade.ei.runupsetup.ui.components.BottomBarItem
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme
import java.util.Locale

class RoutePageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences("runup_prefs", MODE_PRIVATE)
        val loggedId = prefs.getLong("logged_id", -1L)

        setContent {
            RunupSetupTheme {
                RoutePageView(loggedId = loggedId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutePageView(loggedId: Long) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var routes by remember { mutableStateOf<List<PredefinedRouteDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Carregar rotas predefinidas do backend
    LaunchedEffect(Unit) {
        try {
            val resp = RetrofitClient.instance.getPredefinedRoutes()
            if (resp.isSuccessful) {
                routes = resp.body().orEmpty()
                errorMessage = null
            } else {
                errorMessage = "Erro ao carregar rotas (${resp.code()})"
            }
        } catch (e: Exception) {
            errorMessage = "Falha ao comunicar com o servidor."
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xFF7CCE6B),
                    titleContentColor = Color.White
                ),
                title = {
                    Column {
                        Text(
                            text = "Rotas",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Descubra novos caminhos",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            // Card "Criar Rota Personalizada"
            CreateCustomRouteCard(
                onClick = {
                    val intent = Intent(context, RouteFiltersActivity::class.java)
                    context.startActivity(intent)
                }
            )

            when {
                isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(8.dp))
                        Text("Carregando rotas…")
                    }
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFB00020),
                        modifier = Modifier.padding(8.dp)
                    )
                }

                else -> {
                    routes.forEach { route ->
                        PredefinedRouteCard(
                            route = route,
                            imageRes = R.drawable.parque_da_bela_vista, // por enquanto a mesma imagem
                            onStartClick = {
                                if (loggedId <= 0L) {
                                    Toast.makeText(
                                        context,
                                        "Faça login para iniciar uma rota.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@PredefinedRouteCard
                                }

                                scope.launch {
                                    try {
                                        val resp = RetrofitClient.instance.startPredefinedRoute(
                                            rotaId = route.rotaId,
                                            userId = loggedId,
                                            tipo = route.tipo
                                        )
                                        if (resp.isSuccessful) {
                                            val corrida: CorridaGeradaDto? = resp.body()
                                            if (corrida != null) {
                                                val intent =
                                                    Intent(context, RunMapActivity::class.java)
                                                intent.putExtra("corridaGerada", corrida)
                                                context.startActivity(intent)
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Resposta vazia do servidor.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Erro ao iniciar rota (${resp.code()})",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "Falha ao iniciar rota.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNavigationBar() {
    val context = LocalContext.current
    BottomAppBar(
        containerColor = Color.White,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarItem(
                onclick = {
                    context.startActivity(Intent(context, InitialPageActivity::class.java))
                },
                icon = R.drawable.outline_home_24,
                label = "Início"
            )
            BottomBarItem(
                onclick = {
                    context.startActivity(Intent(context, RoutePageActivity::class.java))
                },
                icon = R.drawable.outline_map_24,
                label = "Rotas"
            )
            BottomBarItem(
                onclick = {
                    context.startActivity(Intent(context, ComunityPageActivity::class.java))
                },
                icon = R.drawable.comunity_icon,
                label = "Comunidade"
            )
            BottomBarItem(
                onclick = {
                    context.startActivity(Intent(context, HistoryPageActivity::class.java))
                },
                icon = R.drawable.outline_history_24,
                label = "Histórico"
            )
            BottomBarItem(
                onclick = {
                    context.startActivity(Intent(context, ProfilePageActivity::class.java))
                },
                icon = R.drawable.outline_account_circle_24,
                label = "Perfil"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCustomRouteCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFF7CCE6B)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = Color(0xFF7CCE6B),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    modifier = Modifier
                        .height(60.dp)
                        .width(40.dp),
                    contentDescription = "Ícone de localização",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Criar Rota Personalizada",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Defina distância, terreno e preferências",
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredefinedRouteCard(
    route: PredefinedRouteDto,
    @DrawableRes imageRes: Int,
    onStartClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Imagem da rota",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFDDF6DD), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(route.dificuldade, color = Color(0xFF2E7D32), fontSize = 12.sp)
                }
            }

            Text(
                text = route.nome,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    String.format(Locale.getDefault(), "%.1f km", route.distanciaKm),
                    fontSize = 13.sp
                )
                Text("Lisboa", fontSize = 13.sp) // fixo
                Text(route.tipo, fontSize = 13.sp)
            }

            route.descricao?.let {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = it,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rota predefinida",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Button(
                    onClick = onStartClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7CCE6B),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Iniciar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoutePagePreview() {
    RunupSetupTheme {
        RoutePageView(loggedId = 1L)
    }
}