package pt.iade.ei.runupsetup

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Speed
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

class HistoryPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RunupSetupTheme {
                HistoryPageView(userId = 1) // por enquanto user fixo
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryPageView(userId: Int) {
    val context = LocalContext.current

    var items by remember { mutableStateOf<List<HistoryItemModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        try {
            val resp = RetrofitClient.instance.getHistorico(userId)
            if (resp.isSuccessful) {
                val body = resp.body() ?: emptyList()
                items = body.map { it.toUiModel() }
                errorMessage = null
            } else {
                errorMessage = "Erro ao carregar histórico (${resp.code()})"
            }
        } catch (e: Exception) {
            errorMessage = "Falha: ${e.localizedMessage}"
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
                title = { Text(text = "Histórico") }
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color.White) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomBarItem(
                        onclick = {
                            context.startActivity(
                                Intent(context, InitialPageActivity::class.java)
                            )
                        },
                        icon = R.drawable.outline_home_24,
                        label = "Início"
                    )
                    BottomBarItem(
                        onclick = { /* Rotas */ },
                        icon = R.drawable.outline_map_24,
                        label = "Rotas"
                    )
                    BottomBarItem(
                        onclick = {
                            context.startActivity(
                                Intent(context, ComunityPageActivity::class.java)
                            )
                        },
                        icon = R.drawable.comunity_icon,
                        label = "Comunidade"
                    )
                    BottomBarItem(
                        onclick = {
                            context.startActivity(
                                Intent(context, HistoryPageActivity::class.java)
                            )
                        },
                        icon = R.drawable.outline_history_24,
                        label = "Histórico"
                    )
                    BottomBarItem(
                        onclick = {
                            context.startActivity(
                                Intent(context, ProfilePageActivity::class.java)
                            )
                        },
                        icon = R.drawable.outline_account_circle_24,
                        label = "Perfil"
                    )
                }
            }
        }
    ) { innerPadding ->
        HistoryPageContent(
            modifier = Modifier.padding(innerPadding),
            items = items,
            isLoading = isLoading,
            errorMessage = errorMessage
        )
    }
}

@Composable
private fun HistoryPageContent(
    modifier: Modifier = Modifier,
    items: List<HistoryItemModel>,
    isLoading: Boolean,
    errorMessage: String?
) {
    Column(modifier = modifier.fillMaxSize()) {

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = "Histórico",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(Modifier.height(2.dp))
            Text(text = "Suas atividades recentes")
        }

        HistoryStatsCard(items)

        Text(
            text = DateFormat.format("MMMM yyyy", Calendar.getInstance()).toString()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        when {
            isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(8.dp))
                    Text("Carregando histórico…")
                }
            }

            errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(errorMessage, color = Color.Red)
                }
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(items) { item ->
                        HistoryItemCard(item)
                    }
                }
            }
        }
    }
}

/** Card verde com totais */
@Composable
fun HistoryStatsCard(items: List<HistoryItemModel>) {
    val totalAtividades = items.size

    val totalKm = items.sumOf { item ->
        item.distance.substringBefore(" ")
            .replace(",", ".")
            .toDoubleOrNull() ?: 0.0
    }

    val totalMinutos = items.sumOf { item ->
        // duration vem em "HH:MM"
        val parts = item.duration.split(":")
        val h = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
        h * 60 + m
    }
    val totalHoras = totalMinutos / 60

    Card(
        modifier = Modifier
            .padding(vertical = 12.dp)
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
                Text(text = totalAtividades.toString(), fontSize = 22.sp, color = Color.White)
                Text("Atividades", color = Color.White)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = String.format("%.1f", totalKm), fontSize = 22.sp, color = Color.White)
                Text("km Total", color = Color.White)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (totalHoras > 0) "${totalHoras}h" else "–",
                    fontSize = 22.sp,
                    color = Color.White
                )
                Text("Tempo Total", color = Color.White)
            }
        }
    }
}

/** Card de cada item da lista */
@Composable
fun HistoryItemCard(item: HistoryItemModel) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        onClick = {
            val intent = Intent(context, HistoryDetailPageActivity::class.java)
            intent.putExtra("corridaId", item.corridaId)
            context.startActivity(intent)
        },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp, pressedElevation = 12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFFE7F6E4),
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Text(
                        text = item.tipoLabel,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        color = Color(0xFF228B22),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = DateFormat.format("dd MMM yyyy · HH:mm", item.date).toString(),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Localização",
                    tint = Color(0xFF7CCE6B)
                )
                Spacer(Modifier.width(6.dp))
                Text(text = item.title, fontSize = 14.sp)
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.DirectionsRun,
                            contentDescription = null,
                            tint = Color(0xFF7CCE6B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Distância", fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = null,
                            tint = Color(0xFF7CCE6B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Duração", fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Speed,
                            contentDescription = null,
                            tint = Color(0xFF7CCE6B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Ritmo", fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.LocalFireDepartment,
                            contentDescription = null,
                            tint = Color(0xFF7CCE6B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Calorias", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(item.distance, fontWeight = FontWeight.SemiBold)
                Text(item.duration, fontWeight = FontWeight.SemiBold)
                Text(item.minimumPace, fontWeight = FontWeight.SemiBold)
                Text(item.calories, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}


/** Converte DTO da API em modelo da UI */
fun HistoryItemDto.toUiModel(): HistoryItemModel {
    val cal = Calendar.getInstance()
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(this.data)
        if (date != null) cal.time = date
    } catch (_: Exception) {
    }

    // duração só horas e minutos
    val hours = duracaoSegundos / 3600
    val minutes = (duracaoSegundos % 3600) / 60
    val durationStr = String.format("%02d:%02d", hours, minutes)

    val paceStr = String.format(Locale.getDefault(), "%.1f", paceMinPorKm)
    val caloriesStr = kcal.toString()

    val tipoLabel = when (tipo.uppercase(Locale.getDefault())) {
        "CAMINHADA" -> "Caminhada"
        else -> "Corrida"
    }

    return HistoryItemModel(
        corridaId = corridaId,
        title = routeName,
        date = cal,
        distance = String.format(Locale.getDefault(), "%.1f km", distanciaKm),
        duration = durationStr,
        calories = caloriesStr,
        minimumPace = paceStr,
        tipoLabel = tipoLabel,
    )
}

/** Preview só da tela de histórico */
@Preview(showBackground = true)
@Composable
fun HistoryPagePreview() {
    val cal = Calendar.getInstance()
    val fakeItems = listOf(
        HistoryItemModel(
            corridaId = 1,
            title = "Parque da Cidade",
            date = cal,
            distance = "5.2 km",
            duration = "00:45",
            calories = "320",
            minimumPace = "8.7",
            tipoLabel = "Corrida",
        ),
        HistoryItemModel(
            corridaId = 2,
            title = "Marginal do Rio",
            date = cal,
            distance = "3.8 km",
            duration = "00:32",
            calories = "180",
            minimumPace = "8.4",
            tipoLabel = "Caminhada",
        )
    )

    RunupSetupTheme {
        HistoryPageContent(
            items = fakeItems,
            isLoading = false,
            errorMessage = null
        )
    }
}