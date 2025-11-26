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

    @RequiresPermission(
        allOf = [
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ]
    )
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

        val request = RouteRequest(
            nome = intent.getStringExtra("nome") ?: "Rota personalizada",
            originLat = originLat,
            originLng = originLng,
            destLat = destLat,
            destLng = destLng,
            desiredDistanceKm = intent.getDoubleExtra("distance", 5.0),
            preferTrees = intent.getBooleanExtra("trees", false),
            nearBeach = intent.getBooleanExtra("beach", false),
            nearPark = intent.getBooleanExtra("park", false),
            sunnyRoute = intent.getBooleanExtra("sunny", false),
            avoidHills = false,
            tipo = intent.getStringExtra("tipo") ?: "corrida"
        )

        RetrofitClient.instance.generateRoute(request)
            .enqueue(object : Callback<RouteResponse> {
                override fun onResponse(
                    call: Call<RouteResponse>,
                    response: Response<RouteResponse>
                ) {

                    if (response.isSuccessful && response.body() != null) {
                        val route = response.body()!!

                        val intent = Intent(this@RouteActivity, RunMapActivity::class.java)
                        intent.putExtra("routeResponse", route)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@RouteActivity, "Erro ao gerar rota", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                    Toast.makeText(this@RouteActivity, "Falha ao conectar ao servidor", Toast.LENGTH_LONG).show()
                    finish()
                }
            })
    }
}
