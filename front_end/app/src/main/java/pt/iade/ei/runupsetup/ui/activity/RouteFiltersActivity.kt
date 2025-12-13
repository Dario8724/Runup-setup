package pt.iade.ei.runupsetup.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import pt.iade.ei.runupsetup.data.dto.GenerateCorridaRequestDto
import pt.iade.ei.runupsetup.data.network.RetrofitClient

class RouteFiltersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                RouteFiltersScreen(
                    onBack = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteFiltersScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nome by remember { mutableStateOf(TextFieldValue("")) }
    var tipo by remember { mutableStateOf("corrida") }
    var selectedDistance by remember { mutableStateOf("5 km") }

    var preferTrees by remember { mutableStateOf(false) }
    var nearBeach by remember { mutableStateOf(false) }
    var nearPark by remember { mutableStateOf(false) }
    var sunnyRoute by remember { mutableStateOf(false) }
    var avoidHills by remember { mutableStateOf(false) }

    val distances = listOf("2 km", "5 km", "10 km")

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
                    if (nomeLimpo.isEmpty()) {
                        Toast.makeText(context, "Dá um nome à rota 😊", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val tipoNormalizado =
                        if (tipo.lowercase() == "caminhada") "CAMINHADA" else "CORRIDA"
                    val distanceValue = selectedDistance.replace(" km", "").toDouble()

                    // Localização base
                    val originLat = 38.781810
                    val originLng = -9.102510

                    // filtros
                    val filtros = mutableListOf<String>()
                    if (nearPark) filtros.add("PERTO_PARQUE")
                    if (nearBeach) filtros.add("PERTO_PRAIA")
                    if (sunnyRoute) filtros.add("ENSOLARADA")
                    // avoidHills por enquanto não usamos no back

                    val req = GenerateCorridaRequestDto(
                        userId = 1,
                        routeName = nomeLimpo,
                        tipoAtividade = tipoNormalizado,
                        startLatitude = originLat,
                        startLongitude = originLng,
                        distanceKm = distanceValue,
                        filtros = if (filtros.isEmpty()) null else filtros
                    )

                    Log.d("RouteFilters", "➡️ Enviando request: $req")
                    Toast.makeText(context, "A gerar rota...", Toast.LENGTH_SHORT).show()

                    scope.launch {
                        try {
                            val resp = RetrofitClient.instance.gerarCorrida(req)
                            Log.d("RouteFilters", "⬅️ Resposta HTTP: ${resp.code()}")

                            if (resp.isSuccessful) {
                                val corrida = resp.body()
                                Log.d("RouteFilters", "corpo = $corrida")

                                if (corrida != null) {
                                    Toast.makeText(
                                        context,
                                        "Rota gerada com sucesso!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(context, RunMapActivity::class.java)
                                    intent.putExtra("corridaGerada", corrida)
                                    context.startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Resposta vazia do servidor",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Erro ao gerar rota: ${resp.code()}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (e: Exception) {
                            Log.e("RouteFilters", "❌ Falha ao chamar gerarCorrida", e)
                            Toast.makeText(
                                context,
                                "Falha na ligação: ${e.localizedMessage}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
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
        RouteFiltersScreen(onBack = {})
    }
}