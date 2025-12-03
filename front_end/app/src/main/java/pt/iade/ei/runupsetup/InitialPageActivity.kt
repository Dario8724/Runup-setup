package pt.iade.ei.runupsetup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.models.HistoryItemModel
import pt.iade.ei.runupsetup.ui.components.BottomBarItem
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme
import java.text.SimpleDateFormat
import java.util.*

class InitialPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RunupSetupTheme {
                InitialPageView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialPageView() {
    val context = LocalContext.current

    val item = HistoryItemModel(
        corridaId = 2,
        title = "Corrida de Segunda",
        date = Calendar.getInstance(),
        distance = "5 km",
        duration = "00:30:45",
        calories = "250 kcal",
        minimumPace = "5'30\"/km",
        tipoLabel = "caminhada"
    )

    // texto da data em pt-PT
    val dateText = remember {
        val locale = Locale("pt", "PT")
        val formatter = SimpleDateFormat("EEEE, d 'de' MMMM", locale)
        formatter.format(Date()).replaceFirstChar { it.uppercase() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xFF7CCE6B)
                ),
                title = {
                    Column {
                        Text(
                            text = dateText,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
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
                            val intent = Intent(context, InitialPageActivity::class.java)
                            context.startActivity(intent)
                        },
                        icon = R.drawable.outline_home_24,
                        label = "Início"
                    )
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, RoutePageActivity::class.java)
                            context.startActivity(intent)
                        },
                        icon = R.drawable.outline_map_24,
                        label = "Rotas"
                    )
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, ComunityPageActivity::class.java)
                            context.startActivity(intent)
                        },
                        icon = R.drawable.comunity_icon,
                        label = "Comunidade"
                    )
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, HistoryPageActivity::class.java)
                            context.startActivity(intent)
                        },
                        icon = R.drawable.outline_history_24,
                        label = "Histórico"
                    )
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, ProfilePageActivity::class.java)
                            context.startActivity(intent)
                        },
                        icon = R.drawable.outline_account_circle_24,
                        label = "Perfil"
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            InitialPageHeader()

            // Resumo de hoje (vamos ligar ao backend depois)
            Card(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                onClick = {},
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Resumo de hoje",
                        fontSize = 25.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 8.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Pace médio", fontWeight = FontWeight.Black)
                            Text(item.minimumPace)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Tempo", fontWeight = FontWeight.Black)
                            Text(item.duration)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Distância", fontWeight = FontWeight.Black)
                            Text(item.distance)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Calorias", fontWeight = FontWeight.Black)
                            Text(item.calories)
                        }
                    }
                }
            }

            Row(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = "Atalhos rápidos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Column {
                ShortCutCard(
                    icon = R.drawable.outline_map_24,
                    title = "Explorar rotas",
                    subtitle = "Descubra novos caminhos"
                ) {
                    val intent = Intent(context, RoutePageActivity::class.java)
                    context.startActivity(intent)
                }
                ShortCutCard(
                    icon = R.drawable.outline_circle_circle_24,
                    title = "Metas pessoais",
                    subtitle = "Acompanhe seu progresso"
                ) {
                    val intent = Intent(context, ProfilePageActivity::class.java)
                    context.startActivity(intent)
                }
                ShortCutCard(
                    icon = R.drawable.outline_history_24,
                    title = "Histórico",
                    subtitle = "Veja as suas atividades"
                ) {
                    val intent = Intent(context, HistoryPageActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun InitialPageHeader() {
    Box {
        // Imagem de fundo
        Image(
            painter = painterResource(R.drawable.corredor_ao_por_do_sol),
            contentDescription = "Imagem de um corredor ao pôr do sol",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
        )

        // 🔹 Overlay verde com gradiente para melhorar visibilidade do texto
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xCC7CCE6B), // mais forte em cima
                            Color(0x807CCE6B),
                            Color.Transparent      // some em baixo
                        )
                    )
                )
        )

        // Texto em cima da imagem
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 20.dp, end = 20.dp, top = 40.dp)
        ) {
            Text(
                text = "Olá, Corredor",
                fontWeight = FontWeight.Black,
                fontSize = 25.sp,
                color = Color.White
            )
            Text(
                text = "Comece a se mover e alcance hoje mesmo suas metas de corrida!",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = Color.White
            )
        }

        // Botão Start em baixo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp)
                .align(Alignment.BottomCenter)
        ) {
            StartButton()
        }
    }
}

// Botão de iniciar
@Composable
fun StartButton() {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(context, RouteFiltersActivity::class.java)
            context.startActivity(intent)
        },
        modifier = Modifier.height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF7CCE6B),
            contentColor = Color.Unspecified
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Start button"
            )
            Text(
                text = "Start",
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortCutCard(
    @DrawableRes icon: Int,
    title: String,
    subtitle: String,
    onclick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth(),
        onClick = onclick, // 🔹 usa o callback passado
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            pressedElevation = 12.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "",
                tint = Color.Black
            )
            Column(
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = subtitle
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "seta para prosseguir",
                tint = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InitialPagePreview() {
    RunupSetupTheme {
        InitialPageView()
    }
}