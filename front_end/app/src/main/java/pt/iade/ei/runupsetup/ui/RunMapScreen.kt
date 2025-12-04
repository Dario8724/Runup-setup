package pt.iade.ei.runupsetup.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
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
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.iade.ei.runupsetup.models.CorridaGeradaDto
import pt.iade.ei.runupsetup.models.FinalizarCorridaRequestDto
import pt.iade.ei.runupsetup.network.RetrofitClient

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunMapScreen(
    corridaGerada: CorridaGeradaDto?,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    val scope = rememberCoroutineScope()

    // --------- ESTADOS PRINCIPAIS ---------
    var hasLocationPermission by remember { mutableStateOf(false) }

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var lastLocation by remember { mutableStateOf<LatLng?>(null) }
    var userPath by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    var routePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    var duration by remember { mutableStateOf(0) }          // segundos
    var distanceMeters by remember { mutableStateOf(0.0) }  // metros
    var calories by remember { mutableStateOf(0) }

    var isRunning by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var showSummary by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState()

    // --------- PERMISSÃO DE LOCALIZAÇÃO ---------
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
    }

    LaunchedEffect(Unit) {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted) {
            hasLocationPermission = true
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // --------- CARREGAR ROTA GERADA (VERDE) ---------
    LaunchedEffect(corridaGerada) {
        if (corridaGerada != null && corridaGerada.pontos.isNotEmpty()) {
            val pts = corridaGerada.pontos.map {
                LatLng(it.latitude, it.longitude)
            }
            routePoints = pts

            // Centraliza a câmera na rota
            val boundsBuilder = LatLngBounds.builder()
            pts.forEach { boundsBuilder.include(it) }

            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100)
            )
        }
    }

    // --------- CALLBACK DE LOCALIZAÇÃO (TRACKING REAL) ---------
    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation ?: return
                val newLatLng = LatLng(loc.latitude, loc.longitude)

                userLocation = newLatLng

                // Só acumula distância se estiver correndo
                if (isRunning && !isPaused) {
                    lastLocation?.let { prev ->
                        val delta = distanceBetween(prev, newLatLng)
                        if (delta > 1) { // ignora ruído menor que 1m
                            distanceMeters += delta
                            userPath = userPath + newLatLng
                            // calorias bem simples: ~ 0.06 kcal por metro (≈ 60 kcal / km)
                            val distanceKm = distanceMeters / 1000.0
                            calories = (distanceKm * 60).toInt()
                        }
                    }
                    lastLocation = newLatLng
                } else {
                    // Se não estiver correndo, apenas posiciona a câmera a primeira vez
                    if (lastLocation == null) {
                        lastLocation = newLatLng
                        cameraPositionState.position =
                            CameraPosition.fromLatLngZoom(newLatLng, 16f)
                    }
                }
            }
        }
    }

    val locationRequest = remember {
        LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000L // 1 segundo
        )
            .setMinUpdateIntervalMillis(1000L)
            .build()
    }

    // Inicia / para updates de localização conforme estado (INICIAR / PAUSAR / PARAR)
    DisposableEffect(hasLocationPermission, isRunning, isPaused) {
        if (hasLocationPermission && isRunning && !isPaused) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }

        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    // --------- CONTADOR DE TEMPO ---------
    LaunchedEffect(isRunning, isPaused) {
        if (isRunning && !isPaused) {
            while (isRunning && !isPaused) {
                delay(1000)
                duration++
            }
        }
    }

    var alreadySent by remember { mutableStateOf(false) }

    // --------- UI ---------
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
                        isMyLocationEnabled = hasLocationPermission,
                        mapType = MapType.TERRAIN
                    ),
                    uiSettings = MapUiSettings(zoomControlsEnabled = false)
                ) {
                    // Rota sugerida (verde)
                    if (routePoints.isNotEmpty()) {
                        Polyline(
                            points = routePoints,
                            color = Color(0xFF22C55E),
                            width = 10f,
                            geodesic = true
                        )

                        Marker(
                            state = MarkerState(position = routePoints.first()),
                            title = "Início da rota",
                            icon = BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_GREEN
                            )
                        )
                    }

                    // Caminho percorrido pelo utilizador (azul)
                    if (userPath.size >= 2) {
                        Polyline(
                            points = userPath,
                            color = Color(0xFF007AFF),
                            width = 12f,
                            geodesic = true
                        )
                    }

                    // Marcador do usuário
                    userLocation?.let { pos ->
                        Marker(
                            state = MarkerState(position = pos),
                            title = "Você está aqui",
                            icon = BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_AZURE
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (!showSummary) {
                // Estatísticas em tempo real
                val distanceKm = distanceMeters / 1000.0

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
                            StatBox("Distância", "%.2f km".format(distanceKm))
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
                        onClick = {
                            if (!isRunning) {
                                // reset se quiser sempre começar do zero
                                duration = 0
                                distanceMeters = 0.0
                                calories = 0
                                userPath = emptyList()
                                lastLocation = null
                            }
                            isRunning = true
                            isPaused = false
                        },
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
                        onClick = {
                            if (isRunning) isPaused = !isPaused
                        },
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
                            isRunning = false
                            isPaused = false
                            showSummary = true

                            val corridaId = corridaGerada?.corridaId
                            val prefs = context.getSharedPreferences("runup_prefs", Context.MODE_PRIVATE)
                            val userId = prefs.getLong("logged_id", -1L)

                            if (!alreadySent && corridaId != null && userId != null) {
                                alreadySent = true
                                val distKm = distanceMeters / 1000.0
                                val req = FinalizarCorridaRequestDto(
                                    userId = userId.toInt(),
                                    distanciaRealKm = distKm,
                                    duracaoSegundos = duration.toLong(),
                                    kcal = calories
                                )

                                scope.launch {
                                    try {
                                        val resp = RetrofitClient.instance
                                            .finalizarCorrida(corridaId, req)
                                        if (!resp.isSuccessful) {
                                            print("Erro ao finalizar corrida: ${resp.code()}")
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        println("Falha na chamda finalizarCorrida: ${e.localizedMessage}" )
                                    }
                                }
                            }
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
                val distanceKm = distanceMeters / 1000.0
                SummaryScreen(duration, distanceKm, calories, onFinish)
            }
        }
    }
}

// --- COMPONENTES AUXILIARES ---

@Composable
fun StatBox(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6ECB63))
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.Gray)
    }
}

@Composable
fun SummaryScreen(duration: Int, distanceKm: Double, calories: Int, onFinish: () -> Unit) {
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
            StatBox("Distância", "%.2f km".format(distanceKm))
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

private fun distanceBetween(a: LatLng, b: LatLng): Double {
    val res = FloatArray(1)
    Location.distanceBetween(
        a.latitude, a.longitude,
        b.latitude, b.longitude,
        res
    )
    return res[0].toDouble()
}
