package pt.iade.ei.runupsetup.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay
import pt.iade.ei.runupsetup.models.RouteResponse

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunMapScreen(
    routeResponse: RouteResponse?,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var routePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    var duration by remember { mutableStateOf(0) }
    var distance by remember { mutableStateOf(0.0) }
    var calories by remember { mutableStateOf(0) }

    var isRunning by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var showSummary by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState()

    // Permissão de localização
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    userLocation = latLng
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 16f)
                }
            }
        }
    }

    // Pegar localização atual
    LaunchedEffect(Unit) {
        if (
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    userLocation = latLng
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 16f)
                }
            }
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Carregar rota
    LaunchedEffect(routeResponse) {
        if (routeResponse != null && routeResponse.polyline.isNotEmpty()) {
            try {
                routePoints = PolyUtil.decode(routeResponse.polyline)
                distance = routeResponse.distance

                val boundsBuilder = LatLngBounds.builder()
                routePoints.forEach { boundsBuilder.include(it) }

                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 50)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Contador de tempo
    LaunchedEffect(isRunning, isPaused) {
        if (isRunning && !isPaused) {
            while (isRunning && !isPaused) {
                delay(1000)
                duration++
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Corrida em andamento",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFAF7F2)
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFFAF7F2))
        ) {

            // 🗺️ MAPA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = true,
                        mapType = MapType.TERRAIN
                    ),
                    uiSettings = MapUiSettings(zoomControlsEnabled = false)
                ) {
                    if (routePoints.isNotEmpty()) {
                        // Rota principal
                        Polyline(
                            points = routePoints,
                            color = Color(0xFF007AFF),
                            width = 14f,
                            geodesic = true
                        )

                        // Marcador de início
                        Marker(
                            state = MarkerState(position = routePoints.first()),
                            title = "Início",
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                        )
                    }

                    // Marcador do usuário
                    if (userLocation != null) {
                        Marker(
                            state = MarkerState(position = userLocation!!),
                            title = "Você está aqui",
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (!showSummary) {
                // Estatísticas
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .shadow(6.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Estatísticas", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatBox("Tempo", formatTime(duration))
                            StatBox("Distância", "%.2f km".format(distance))
                            StatBox("Calorias", calories.toString())
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Botões
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { isRunning = true },
                        colors = ButtonDefaults.buttonColors(Color(0xFF6ECB63)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .padding(4.dp)
                    ) {
                        Text("Iniciar", color = Color.White, fontWeight = FontWeight.ExtraBold)
                    }

                    Button(
                        onClick = { if (isRunning) isPaused = !isPaused },
                        colors = ButtonDefaults.buttonColors(
                            if (isPaused) Color(0xFF34C759) else Color(0xFFFFE066)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .padding(4.dp)
                    ) {
                        Text(
                            if (isPaused) "Retomar" else "Pausar",
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Button(
                        onClick = {
                            showSummary = true
                            isRunning = false
                            isPaused = false
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFFFF6B6B)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .padding(4.dp)
                    ) {
                        Text("Parar", color = Color.White, fontWeight = FontWeight.ExtraBold)
                    }
                }

            } else {
                SummaryScreen(duration, distance, calories, onFinish)
            }
        }
    }
}

@Composable
fun StatBox(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6ECB63))
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.Gray)
    }
}

@Composable
fun SummaryScreen(duration: Int, distance: Double, calories: Int, onFinish: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Resumo da Corrida",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6ECB63)
            )
            Spacer(Modifier.height(16.dp))
            StatBox("Tempo Total", formatTime(duration))
            StatBox("Distância", "%.2f km".format(distance))
            StatBox("Calorias", calories.toString())
        }
        Button(
            onClick = { onFinish() },
            colors = ButtonDefaults.buttonColors(Color(0xFF6ECB63)),
            shape = RoundedCornerShape(40),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Voltar ao Início", color = Color.White, fontSize = 16.sp)
        }
    }
}

private fun formatTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return String.format("%02d:%02d", min, sec)
}
