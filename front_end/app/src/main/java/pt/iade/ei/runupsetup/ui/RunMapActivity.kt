package pt.iade.ei.runupsetup.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import pt.iade.ei.runupsetup.models.RouteResponse

class RunMapActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Recupera a rota enviada pelo RouteActivity
        val routeResponse = intent.getSerializableExtra("routeResponse") as? RouteResponse

        // 🧩 Renderiza o mapa com Compose
        setContent {
            MaterialTheme {
                RunMapScreen(
                    routeResponse = routeResponse,
                    onFinish = { finish() } // Fecha a tela ao terminar
                )
            }
        }
    }
}
