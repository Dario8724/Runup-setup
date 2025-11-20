package pt.iade.ei.runupsetup.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import pt.iade.ei.runupsetup.InitialPageActivity
import pt.iade.ei.runupsetup.RouteFiltersActivity
import pt.iade.ei.runupsetup.models.RouteResponse

class RunMapActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val routeResponse = intent.getSerializableExtra("routeResponse") as? RouteResponse

        setContent {
            MaterialTheme {
                RunMapScreen(
                    routeResponse = routeResponse,
                    onFinish = {
                        val intent = Intent(this, InitialPageActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}
