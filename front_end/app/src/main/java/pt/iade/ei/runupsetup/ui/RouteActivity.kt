package pt.iade.ei.runupsetup.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import pt.iade.ei.runupsetup.R
import pt.iade.ei.runupsetup.models.RouteRequest
import pt.iade.ei.runupsetup.models.RouteResponse
import pt.iade.ei.runupsetup.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var textDistance: TextView
    private lateinit var textDuration: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        textDistance = findViewById(R.id.textDistance)
        textDuration = findViewById(R.id.textDuration)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Coordenadas de teste (Lisboa)
        val originLat = 38.7169
        val originLng = -9.1399
        val destLat = 38.7083
        val destLng = -9.1408

        val request = RouteRequest(
            nome = "Rota Teste",
            originLat = originLat,
            originLng = originLng,
            destLat = destLat,
            destLng = destLng,
            desiredDistanceKm = 5.0,
            preferTrees = true,
            nearBeach = false,
            nearPark = false,
            sunnyRoute = false,
            avoidHills = false,
            tipo = "corrida"
        )

        RetrofitClient.instance.generateRoute(request).enqueue(object : Callback<RouteResponse> {
            override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
                if (response.isSuccessful) {
                    val route = response.body()
                    route?.let {
                        textDistance.text = "Distância: ${it.distance} km"
                        textDuration.text = "Duração: ${it.duration} min"

                        if (it.polyline.isNotEmpty()) {
                            val points = PolyUtil.decode(it.polyline)
                            map.addPolyline(
                                PolylineOptions()
                                    .addAll(points)
                                    .width(10f)
                                    .color(0xFF1976D2.toInt())
                            )
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(points.first(), 14f))
                        }
                    }
                } else {
                    textDistance.text = "Erro: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                textDistance.text = "Falha: ${t.message}"
            }
        })
    }
}
