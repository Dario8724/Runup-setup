package pt.iade.ei.runupsetup

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.models.HistoryItemDto
import pt.iade.ei.runupsetup.models.HistoryItemModel
import pt.iade.ei.runupsetup.network.RetrofitClient
import pt.iade.ei.runupsetup.ui.components.BottomBarItem
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HistoryDetailPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RunupSetupTheme {
                HistoryDetailPageView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailPageView(userId: Int = 1) { // por enquanto userId fixo
    val context = LocalContext.current

    // ---- ESTADO ----
    var items by remember { mutableStateOf<List<HistoryItemModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // ---- CHAMADA AO BACKEND ----
    LaunchedEffect(userId) {
        try {
            val response = RetrofitClient.instance.getHistorico(userId)

            if (response.isSuccessful) {
                val body = response.body() ?: emptyList()
                // Mapear DTO -> UI model
                items = body.map { it.toUiModel() }
            } else {
                errorMessage = "Erro ao carregar histórico (${response.code()})"
            }
        } catch (e: Exception) {
            errorMessage = "Erro de rede: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xFF7CCE6B),
                ),
                title = {}
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
                        onclick = { /* TODO rotas */ },
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
                        onclick = { /* já estamos no histórico */ },
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
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
        ) {
            // Título
            Text(
                text = "Histórico ",
                fontSize = 25.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Suas atividades recentes")
            Spacer(modifier = Modifier.height(4.dp))

            // Card resumo (por enquanto estático; depois podemos calcular com base em items)
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                shape = RectangleShape,
                elevation = CardDefaults.cardElevation(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF7CCE6B)
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = items.size.toString(),
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Atividades",
                            color = Color.White
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val totalKm = items
                            .mapNotNull {
                                it.distance.replace(" km", "").replace(",", ".").toDoubleOrNull()
                            }
                            .sum()
                        Text(
                            text = String.format("%.1f", totalKm),
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Text(
                            text = "km Total",
                            color = Color.White
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // por enquanto só um placeholder
                        Text(
                            text = "—",
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Tempo Total",
                            color = Color.White
                        )
                    }
                }
            }

            // Filtro por mês (por enquanto só mostra o mês atual ou da primeira corrida)
            val monthLabel = if (items.isNotEmpty()) {
                DateFormat.format("MMMM 'de' yyyy", items[0].date).toString()
            } else {
                DateFormat.format("MMMM 'de' yyyy", Calendar.getInstance()).toString()
            }

            Row(
                modifier = Modifier
                    .padding(start = 5.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = monthLabel)
                Button(
                    onClick = { /* TODO abrir filtro de datas */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFF7CCE6B)
                    )
                ) {
                    Icon(
                        Icons.Outlined.DateRange,
                        contentDescription = "Filtrar datas"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ---- ESTADO: LOADING / ERRO / LISTA ----
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Carregando histórico…")
                    }
                }

                errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = Color.Red
                        )
                    }
                }

                else -> {
                    LazyColumn {
                        items(items.size) { index ->
                            val item = items[index]
                            HistoryItemCard(item = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(item: HistoryItemModel) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth(),
        onClick = {
            // aqui depois vamos navegar para a tela de detalhes
            // por enquanto pode deixar vazio
        },
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Corrida", // se quiser pode trocar depois por tipo ou título
                )
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "ver detalhes",
                    tint = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.LocationOn,
                    contentDescription = "Localização"
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Rota: ${item.title}")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Pace médio",
                        fontWeight = FontWeight.Black
                    )
                    Text(text = item.minimumPace)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Tempo",
                        fontWeight = FontWeight.Black
                    )
                    Text(text = item.duration)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Distância",
                        fontWeight = FontWeight.Black
                    )
                    Text(text = item.distance)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Calorias",
                        fontWeight = FontWeight.Black
                    )
                    Text(text = item.calories)
                }
            }
        }
    }
}

fun HistoryItemDto.toUiModel(): HistoryItemModel {
    // data vem como "yyyy-MM-dd"
    val cal = Calendar.getInstance()
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(this.data)
        if (date != null) cal.time = date
    } catch (_: Exception) {
    }

    // formata duração hh:mm:ss
    val hours = duracaoSegundos / 3600
    val minutes = (duracaoSegundos % 3600) / 60
    val seconds = duracaoSegundos % 60
    val durationStr = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    // pace como "6.0 min/km"
    val paceStr = String.format(Locale.getDefault(), "%.1f min/km", paceMinPorKm)

    return HistoryItemModel(
        title = routeName,
        date = cal,
        distance = String.format(Locale.getDefault(), "%.1f km", distanciaKm),
        duration = durationStr,
        calories = "$kcal kcal",
        minimumPace = paceStr,
        minimap = R.drawable.map_image // placeholder
    )
}

// Preview só com um item fake
@Preview(showBackground = true)
@Composable
fun HistoryDetailPagePreview() {
    val cal = Calendar.getInstance()
    val fakeItem = HistoryItemModel(
        title = "Corrida de Segunda",
        date = cal,
        distance = "5.0 km",
        duration = "00:30:45",
        calories = "250 kcal",
        minimumPace = "6.0 min/km",
        minimap = R.drawable.map_image
    )
    RunupSetupTheme {
        Column {
            HistoryItemCard(item = fakeItem)
        }
    }
}