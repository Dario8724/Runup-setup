package pt.iade.ei.runupsetup.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.tooling.preview.Preview
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
import androidx.compose.material3.*
import androidx.compose.ui.text.font.Font

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

    // Atualização de tempo
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
                        textAlign = TextAlign.Center,
                        color = Color(0xFF222222),
                        lineHeight = 30.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFAF7F2)
            ))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFFAF7F2))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
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
                // Estatísticas
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF7F2))
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
                        onClick = {
                            if (!isRunning) {
                                isRunning = true
                                fetchRoute(
                                    userLocation?.latitude ?: 0.0,
                                    userLocation?.longitude ?: 0.0
                                ) { routePoints = it }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFF6ECB63)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            .padding(4.dp)
                            .border(2.dp, Color.Gray, RoundedCornerShape(50))
                    ) { Text("Iniciar", color = Color.White, fontWeight = FontWeight.ExtraBold) }

                    Button(
                        onClick = { if (isRunning) isPaused = !isPaused },
                        colors = ButtonDefaults.buttonColors(
                            if (isPaused) Color(0xFF34C759) else Color(0xFFFFC300)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            .padding(4.dp)
                            .border(2.dp,Color.Gray,RoundedCornerShape(50))
                    ) { Text(if (isPaused) "Retomar" else "Pausar", color = Color.White, fontWeight = FontWeight.ExtraBold) }

                    Button(
                        onClick = {
                            showSummary = true
                            isRunning = false
                            isPaused = false
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFFFF4C4C)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            .padding(4.dp)
                            .border(2.dp,Color.Gray,RoundedCornerShape(50))
                    ) { Text("Parar", color = Color.White, fontWeight = FontWeight.ExtraBold) }
                }
            } else {
                // Tela de resumo
                SummaryScreen(duration, distance, calories)
            }
        }
    }
}

@Composable
fun StatBox(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6ECB63))
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold ,color = Color.Gray)
    }
}

@Composable
fun SummaryScreen(duration: Int, distance: Double, calories: Int) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Resumo da Corrida",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6ECB63)
            )

            StatBoxSmall("Tempo Total", formatTime(duration))
            StatBoxSmall("Distância", "%.2f km".format(distance))
            StatBoxSmall("Calorias", calories.toString())
        }

        Button(
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF6ECB63)),
            shape = RoundedCornerShape(40),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(40)) // 👈 contorno cinza
        ) {
            Text("Voltar ao Início", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun StatBoxSmall(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6ECB63)
        )
        Text(
            label,
            fontSize = 16.sp,
            color = Color.Gray
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

        override fun onFailure(call: Call<RouteResponse>, t: Throwable) = t.printStackTrace()
    })
}

@Preview(showBackground = true)
@Composable
fun RunMapScreenPreview() {
    RunMapScreen()
}
