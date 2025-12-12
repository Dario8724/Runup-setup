package pt.iade.ei.runupsetup.view

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import pt.iade.ei.runupsetup.models.CorridaDetalheDto
import pt.iade.ei.runupsetup.models.RoutePointDto
import pt.iade.ei.runupsetup.network.RetrofitClient
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme

class HistoryDetailPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val extraId = intent.getIntExtra("corridaId", -1)
        val corridaId = if (extraId > 0) extraId else 5 // fallback de teste

        setContent {
            RunupSetupTheme {
                HistoryDetailPageView(corridaId = corridaId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailPageView(corridaId: Int) {
    val context = LocalContext.current

    var detalhe by remember { mutableStateOf<CorridaDetalheDto?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(corridaId) {
        try {
            val response = RetrofitClient.instance.getCorridaDetalhe(corridaId)
            if (response.isSuccessful) {
                detalhe = response.body()
                errorMessage = null
            } else {
                errorMessage = "Erro ${response.code()}"
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
                title = { Text(text = detalhe?.routeName ?: "Detalhes da Corrida") },
                navigationIcon = {
                    IconButton(onClick = { (context as? Activity)?.finish() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        when {
            isLoading -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(8.dp))
                    Text("Carregando detalhes…")
                }
            }

            errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Erro: $errorMessage", color = Color.Red)
                }
            }

            detalhe != null -> {
                DetalheConteudo(
                    modifier = Modifier.padding(innerPadding),
                    detalhe = detalhe!!
                )
            }
        }
    }
}

@Composable
fun DetalheConteudo(
    modifier: Modifier = Modifier,
    detalhe: CorridaDetalheDto
) {
    val isPreview = LocalInspectionMode.current

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        if (detalhe.pontos.isNotEmpty()) {
            val latLngList = detalhe.pontos.map { LatLng(it.latitude, it.longitude) }
            val startLatLng = latLngList.first()
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(startLatLng, 14f)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                if (!isPreview) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {
                        Polyline(
                            points = latLngList,
                            color = Color(0xFF7CCE6B),
                            width = 10f
                        )
                        Marker(
                            state = rememberMarkerState(position = latLngList.first()),
                            title = "Início"
                        )
                        Marker(
                            state = rememberMarkerState(position = latLngList.last()),
                            title = "Fim"
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF111827)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Preview do mapa",
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color(0xFF22C55E),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Text(
                            text = detalhe.tipo.uppercase(),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Surface(
                        color = Color(0xFF111827),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Text(
                            text = detalhe.data.toString(),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }

                Text(
                    text = "",
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        // ----- BLOCO CLARO COM STATS -----
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF7CCE6B)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = detalhe.routeName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            // Cards Distância / Tempo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFFBF0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.DirectionsRun,
                            contentDescription = null,
                            tint = Color(0xFF22C55E)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Distância Total", color = Color.Gray, fontSize = 12.sp)
                        Text(
                            text = "%.1f".format(detalhe.distanciaKm),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text("quilômetros", color = Color.Gray, fontSize = 12.sp)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF4FF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = null,
                            tint = Color(0xFF2563EB)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Tempo Total", color = Color.Gray, fontSize = 12.sp)
                        Text(
                            text = formatDetailDuration(detalhe.duracaoSegundos),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text("minutos", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Estatísticas detalhadas
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Estatísticas Detalhadas",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(12.dp))

                    StatRow(
                        iconBg = Color(0xFFF3E8FF),
                        iconTint = Color(0xFF8B5CF6),
                        title = "Ritmo Médio",
                        subtitle = "Velocidade por km",
                        value = "%.2f min/km".format(detalhe.paceMinPorKm)
                    )

                    StatRow(
                        iconBg = Color(0xFFFFEDD5),
                        iconTint = Color(0xFFFB923C),
                        title = "Calorias Queimadas",
                        subtitle = "Energia gasta",
                        value = "${detalhe.kcal} kcal"
                    )

                    StatRow(
                        iconBg = Color(0xFFE0F2FE),
                        iconTint = Color(0xFF3B82F6),
                        title = "Elevação",
                        subtitle = "Ganho de altitude",
                        value = "%.1f m".format(detalhe.totalElevationGain)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7CCE6B),
                    contentColor = Color.White
                )
            ) {
                Text("Partilhar na Comunidade")
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatRow(
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Speed,
                    contentDescription = null,
                    tint = iconTint
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Color.Gray, fontSize = 12.sp)
            }
        }
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}

private fun formatDetailDuration(totalSeconds: Long): String {
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60
    return String.format("%02d:%02d", m, s)
}

 @Preview(showBackground = true)
@Composable
fun HistoryDetailPreview() {
    val fakePontos = listOf(
        RoutePointDto(38.7223, -9.1393, 50.0),
        RoutePointDto(38.7230, -9.1380, 55.0),
        RoutePointDto(38.7240, -9.1379, 60.0)
    )
    
    val fake = CorridaDetalheDto(
        corridaId = 1,
        userId = 1,
        data = "2025-11-29",
        distanciaKm = 5.2,
        duracaoSegundos = 45 * 60 + 32,
        paceMinPorKm = 8.75,
        kcal = 320,
        tipo = "CORRIDA",
        routeName = "Parque da Cidade",
        totalElevationGain = 45.0,
        pontos = fakePontos
    )

    RunupSetupTheme {
        DetalheConteudo(detalhe = fake)
    }
}