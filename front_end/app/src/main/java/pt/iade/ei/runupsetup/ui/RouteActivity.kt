package pt.iade.ei.runupsetup.ui

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import pt.iade.ei.runupsetup.models.RouteRequest
import pt.iade.ei.runupsetup.models.RouteResponse
import pt.iade.ei.runupsetup.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                gerarRota(location)
            } else {
                Toast.makeText(this, "Não foi possível obter localização atual", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun gerarRota(location: Location) {
        val originLat = location.latitude
        val originLng = location.longitude
        val destLat = originLat + 0.02
        val destLng = originLng + 0.02

        // 🔹 Recebe filtros e parâmetros
        val nome = intent.getStringExtra("nome") ?: "Rota personalizada"
        val desiredDistance = intent.getDoubleExtra("distance", 5.0)
        val preferTrees = intent.getBooleanExtra("trees", false)
        val nearBeach = intent.getBooleanExtra("beach", false)
        val nearPark = intent.getBooleanExtra("park", false)
        val sunnyRoute = intent.getBooleanExtra("sunny", false)
        val tipo = intent.getStringExtra("tipo") ?: "corrida"

        // 🔹 Log antes da requisição
        Log.d("DEBUG_ROUTE", """
-------------------------
📤 ENVIANDO REQUISIÇÃO DE ROTA
Nome: $nome
Distância desejada: $desiredDistance km
Preferência Árvores: $preferTrees
Perto da Praia: $nearBeach
Perto de Parque: $nearPark
Rota ensolarada: $sunnyRoute
Tipo: $tipo
Origem: ($originLat, $originLng)
Destino: ($destLat, $destLng)
-------------------------
""".trimIndent())

        val request = RouteRequest(
            nome = nome,
            originLat = originLat,
            originLng = originLng,
            destLat = destLat,
            destLng = destLng,
            desiredDistanceKm = desiredDistance,
            preferTrees = preferTrees,
            nearBeach = nearBeach,
            nearPark = nearPark,
            sunnyRoute = sunnyRoute,
            avoidHills = false,
            tipo = tipo
        )

        // 🔹 Log do objeto completo
        Log.d("DEBUG_ROUTE", "📦 RouteRequest completo: $request")

        RetrofitClient.instance.generateRoute(request).enqueue(object : Callback<RouteResponse> {
            override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
                Log.d("DEBUG_ROUTE", """
✅ RESPOSTA DO SERVIDOR
Status: ${response.code()}
Corpo: ${response.body()}
-------------------------
""".trimIndent())

                if (response.isSuccessful && response.body() != null) {
                    val route = response.body()!!
                    Log.d("DEBUG_ROUTE", "✅ Rota recebida: $route")

                    val intent = Intent(this@RouteActivity, RunMapActivity::class.java)
                    intent.putExtra("routeResponse", route)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("DEBUG_ROUTE", "❌ Erro na resposta: ${response.code()}")
                    Toast.makeText(this@RouteActivity, "Erro ao gerar rota", Toast.LENGTH_LONG).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                Log.e("DEBUG_ROUTE", "❌ Falha ao gerar rota: ${t.message}")
                Toast.makeText(this@RouteActivity, "Falha ao gerar rota", Toast.LENGTH_LONG).show()
                finish()
            }
        })
    }
}
