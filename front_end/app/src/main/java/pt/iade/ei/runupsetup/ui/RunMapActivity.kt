package pt.iade.ei.runupsetup.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.iade.ei.runupsetup.models.RouteRequest
import pt.iade.ei.runupsetup.models.RouteResponse
import pt.iade.ei.runupsetup.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class RunMapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RunMapScreen() }
    }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunMapScreen() {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var routePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var distance by remember { mutableStateOf(0.0) }
    var duration by remember { mutableStateOf(0) }
    var calories by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var showSummary by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    var lastLocation by remember { mutableStateOf<Location?>(null) }
    val cameraPositionState = rememberCameraPositionState()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
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
    )

    // Solicita permissão
    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        userLocation = latLng
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 16f)
                    }
                }
            }
            else -> locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Atualização de localização durante a corrida
    DisposableEffect(isRunning && !isPaused) {
        if (isRunning && !isPaused) {
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val newLocation = locationResult.lastLocation ?: return
                    val newLatLng = LatLng(newLocation.latitude, newLocation.longitude)
                    userLocation = newLatLng

                    scope.launch {
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLng(newLatLng),
                            durationMs = 1000
                        )
                    }

                    lastLocation?.let { last ->
                        val results = FloatArray(1)
                        Location.distanceBetween(
                            last.latitude, last.longitude,
                            newLocation.latitude, newLocation.longitude,
                            results
                        )
                        distance += results[0] / 1000
                        calories = (distance * 60).roundToInt()
                    }
                    lastLocation = newLocation
                }
            }

            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                2000L
            ).build()

            fusedLocationClient.requestLocationUpdates(request, locationCallback, null)
            onDispose { fusedLocationClient.removeLocationUpdates(locationCallback) }
        } else onDispose {}
    }

    // Cronômetro
    LaunchedEffect(isRunning, isPaused) {
        if (isRunning && !isPaused) {
            while (isRunning && !isPaused) {
                delay(1000)
                duration++
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Corrida em andamento") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Mapa ocupa 40% da tela
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(zoomControlsEnabled = false)
                ) {
                    if (routePoints.isNotEmpty()) {
                        Polyline(points = routePoints, color = Color(0xFF007AFF), width = 10f)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!showSummary) {
                // Estatísticas da corrida
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Estatísticas da Corrida",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

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

                Spacer(modifier = Modifier.height(32.dp))

                // Botões grandes e espaçados
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            if (!isRunning) {
                                isRunning = true
                                fetchRoute(userLocation?.latitude ?: 0.0, userLocation?.longitude ?: 0.0) {
                                    routePoints = it
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .padding(4.dp)
                    ) {
                        Text("Iniciar", color = Color.White, fontSize = 18.sp)
                    }

                    Button(
                        onClick = { if (isRunning) isPaused = !isPaused },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPaused) Color(0xFF34C759) else Color(0xFFFF9500)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .padding(4.dp)
                    ) {
                        Text(if (isPaused) "Retomar" else "Pausar", color = Color.White, fontSize = 18.sp)
                    }

                    Button(
                        onClick = {
                            showSummary = true
                            isRunning = false
                            isPaused = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .padding(4.dp)
                    ) {
                        Text("Parar", color = Color.White, fontSize = 18.sp)
                    }
                }
            } else {
                // Tela de resumo
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Resumo da Corrida",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF007AFF),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    StatBox("Tempo Total", formatTime(duration))
                    StatBox("Distância", "%.2f km".format(distance))
                    StatBox("Calorias", calories.toString())

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = {
                            showSummary = false
                            duration = 0
                            distance = 0.0
                            calories = 0
                            routePoints = emptyList()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text("Nova Corrida", color = Color.White, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun StatBox(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = value,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF007AFF),
            textAlign = TextAlign.Center
        )
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )
    }
}

private fun formatTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return String.format("%02d:%02d", min, sec)
}

private fun fetchRoute(lat: Double, lng: Double, onResult: (List<LatLng>) -> Unit) {
    val request = RouteRequest(
        nome = "Corrida",
        originLat = lat,
        originLng = lng,
        destLat = lat + 0.01,
        destLng = lng + 0.01,
        desiredDistanceKm = 5.0,
        preferTrees = true,
        nearBeach = false,
        nearPark = true,
        sunnyRoute = true,
        avoidHills = false,
        tipo = "corrida"
    )

    RetrofitClient.instance.generateRoute(request).enqueue(object : Callback<RouteResponse> {
        override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
            response.body()?.let { route ->
                val decodedPath = PolyUtil.decode(route.polyline)
                onResult(decodedPath)
            }
        }

        override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
            t.printStackTrace()
        }
    })
}
