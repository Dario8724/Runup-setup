package pt.iade.ei.runupsetup.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import pt.iade.ei.runupsetup.data.dto.CorridaGeradaDto
import pt.iade.ei.runupsetup.ui.screen.RunMapScreen

class RunMapActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val corrida= intent.getSerializableExtra("corridaGerada") as? CorridaGeradaDto

        setContent {
            MaterialTheme {
                RunMapScreen(
                    corridaGerada = corrida,
                    onFinish = {
                        val intent = Intent(this, InitialPageActivity::class.java)
                        intent.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                    Intent.FLAG_ACTIVITY_NEW_TASK
                        )
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}