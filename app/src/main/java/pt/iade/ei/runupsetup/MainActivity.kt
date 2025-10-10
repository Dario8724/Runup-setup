
package pt.iade.ei.runupsetup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pt.iade.ei.runupsetup.ui.components.HistoryItem
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme
import java.util.Calendar
import pt.iade.ei.runupsetup.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RunupSetupTheme {
                // Optionally, you can display HistoryItem here as well
                HomePreview()
            }
        }
    }
}
@Preview
@Composable
fun whatever(){

}
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    RunupSetupTheme {
        HistoryItem(
            title = "Corrida de Segunda",
            date = Calendar.getInstance(),
            distance = "5 km",
            duration = 30, // 30 minutes
            calories = "250 kcal",
            minimap = R.drawable.map_image
        )
    }
}
