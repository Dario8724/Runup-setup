package pt.iade.ei.runupsetup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.models.RouteRequest
import pt.iade.ei.runupsetup.ui.RunMapActivity

class RouteFiltersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                RouteFiltersScreen { routeRequest ->
                    val intent = Intent(this, RunMapActivity::class.java)
                    intent.putExtra("routeRequest", routeRequest)
                    startActivity(intent)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteFiltersScreen(onGenerateRoute: (RouteRequest) -> Unit) {
    var nome by remember { mutableStateOf(TextFieldValue("")) }
    var tipo by remember { mutableStateOf("corrida") }
    var selectedDistance by remember { mutableStateOf("5 km") }

    var preferTrees by remember { mutableStateOf(false) }
    var nearBeach by remember { mutableStateOf(false) }
    var nearPark by remember { mutableStateOf(false) }
    var sunnyRoute by remember { mutableStateOf(false) }
    var avoidHills by remember { mutableStateOf(false) }

    val distances = listOf("2 km", "5 km", "10 km")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nova Rota", fontWeight = FontWeight.Bold, fontSize = 22.sp) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome da rota") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Text("Tipo de atividade", fontWeight = FontWeight.Medium)
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    selected = tipo == "corrida",
                    onClick = { tipo = "corrida" },
                    label = { Text("Corrida") }
                )
                FilterChip(
                    selected = tipo == "caminhada",
                    onClick = { tipo = "caminhada" },
                    label = { Text("Caminhada") }
                )
            }

            Spacer(Modifier.height(16.dp))

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
                        label = { Text(distance) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

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

            Button(
                onClick = {
                    if (nome.text.isBlank()) return@Button

                    val originLat = 38.781810
                    val originLng = -9.102510
                    val destLat = originLat + 0.01
                    val destLng = originLng + 0.01

                    val routeRequest = RouteRequest(
                        nome = nome.text,
                        originLat = originLat,
                        originLng = originLng,
                        destLat = destLat,
                        destLng = destLng,
                        desiredDistanceKm = selectedDistance.replace(" km", "").toDouble(),
                        preferTrees = preferTrees,
                        nearBeach = nearBeach,
                        nearPark = nearPark,
                        sunnyRoute = sunnyRoute,
                        avoidHills = avoidHills,
                        tipo = tipo
                    )

                    onGenerateRoute(routeRequest)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Gerar Rota", fontSize = 18.sp)
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
