package pt.iade.ei.runupsetup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.models.RouteRequest
import pt.iade.ei.runupsetup.ui.RouteActivity
import pt.iade.ei.runupsetup.ui.RunMapActivity

class RouteFiltersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                RouteFiltersScreen(
                    onGenerateRoute = { routeRequest ->
                        val intent = Intent(this, RouteActivity::class.java)
                        intent.putExtra("nome", routeRequest.nome)
                        intent.putExtra("distance", routeRequest.desiredDistanceKm)
                        intent.putExtra("trees", routeRequest.preferTrees)
                        intent.putExtra("beach", routeRequest.nearBeach)
                        intent.putExtra("park", routeRequest.nearPark)
                        intent.putExtra("sunny", routeRequest.sunnyRoute)
                        intent.putExtra("tipo", routeRequest.tipo)
                        startActivity(intent)
                    },
                    onBack = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteFiltersScreen(
    onGenerateRoute: (RouteRequest) -> Unit,
    onBack: () -> Unit
) {
    var nome by remember { mutableStateOf(TextFieldValue("")) }
    var tipo by remember { mutableStateOf("corrida") }
    var selectedDistance by remember { mutableStateOf("5 km") }

    var preferTrees by remember { mutableStateOf(false) }
    var nearBeach by remember { mutableStateOf(false) }
    var nearPark by remember { mutableStateOf(false) }
    var sunnyRoute by remember { mutableStateOf(false) }
    var avoidHills by remember { mutableStateOf(false) }

    val distances = listOf("2 km", "5 km", "10 km")

    // Paleta de cores
    val backgroundColor = Color(0xFFFAF7F2)
    val greenLight = Color(0xFFB8E986)
    val greenMain = Color(0xFF6ECB63)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Nova Rota",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color(0xFF222222)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nome da rota
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome da rota") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            // Tipo de atividade
            Text("Tipo de atividade", fontWeight = FontWeight.Medium)
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("corrida", "caminhada").forEach { option ->
                    FilterChip(
                        selected = tipo == option,
                        onClick = { tipo = option },
                        label = { Text(option.replaceFirstChar { it.uppercase() }) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = greenLight,
                            selectedContainerColor = greenMain,
                            labelColor = Color.Black,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Distância
            Text("Distância desejada", fontWeight = FontWeight.Medium)
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                distances.forEach { distance ->
                    FilterChip(
                        selected = selectedDistance == distance,
                        onClick = { selectedDistance = distance },
                        label = { Text(distance) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = greenLight,
                            selectedContainerColor = greenMain,
                            labelColor = Color.Black,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Filtros adicionais
            Text("Filtros adicionais", fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))

            Column(Modifier.fillMaxWidth()) {
                FilterCheck("Mais árvores", preferTrees) { preferTrees = it }
                FilterCheck("Perto da praia", nearBeach) { nearBeach = it }
                FilterCheck("Perto de parque", nearPark) { nearPark = it }
                FilterCheck("Rota ensolarada", sunnyRoute) { sunnyRoute = it }
                FilterCheck("Evitar subidas", avoidHills) { avoidHills = it }
            }

            Spacer(Modifier.height(28.dp))

            // Botão principal
            Button(
                onClick = {
                    val nomeLimpo = nome.text.trim()
                    if (nomeLimpo.isEmpty()) return@Button

                    val tipoNormalizado = tipo.lowercase()
                    val distanceValue = selectedDistance.replace(" km", "").toDouble()

                    // Localização base do emulador
                    val originLat = 38.781810
                    val originLng = -9.102510

                    // Ajuste proporcional de destino conforme a distância
                    val fator = distanceValue / 2.0
                    val destLat = originLat + 0.009 * fator
                    val destLng = originLng + 0.009 * fator

                    val routeRequest = RouteRequest(
                        nome = nomeLimpo,
                        originLat = originLat,
                        originLng = originLng,
                        destLat = destLat,
                        destLng = destLng,
                        desiredDistanceKm = distanceValue,
                        preferTrees = preferTrees,
                        nearBeach = nearBeach,
                        nearPark = nearPark,
                        sunnyRoute = sunnyRoute,
                        avoidHills = avoidHills,
                        tipo = tipoNormalizado
                    )

                    onGenerateRoute(routeRequest)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = greenMain)
            ) {
                Text("Gerar Rota", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun FilterCheck(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .toggleable(value = checked, onValueChange = onCheckedChange)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = null)
        Spacer(Modifier.width(8.dp))
        Text(label)
    }
}

@Preview(showBackground = true)
@Composable
fun RouteFiltersScreenPreview() {
    MaterialTheme {
        RouteFiltersScreen(onGenerateRoute = {}, onBack = {})
    }
}
