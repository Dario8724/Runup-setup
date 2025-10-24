package pt.iade.ei.runupsetup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import pt.iade.ei.runupsetup.models.HistoryItemModel1
import pt.iade.ei.runupsetup.ui.components.HistoryItem
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RunupSetupTheme {
                MainView()

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView() {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "RunUp",
                        fontWeight = FontWeight.Black
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "...",
                )
            }
        }
    ) { innerPadding ->

        // Cria um item base
        val item = HistoryItemModel1(
            title = "Corrida de Segunda",
            date = Calendar.getInstance(),
            distance = "5 km",
            duration = "00:30:45",
            calories = "250 kcal",
            minimumPace = "5'30\"/km",
            minimap = R.drawable.map_image
        )

        // Exibir corridas automaticamente
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            HistoryItem(
                title = item.title,
                date = item.date,
                distance = item.distance,
                duration = item.duration,
                calories = item.calories,
                minimumPace = item.minimumPace,
                minimap = item.minimap
            )
            // Geração automática de 5 corridas
            for (i in 1..5) {
                HistoryItem(
                    title = "Corrida nº $i", // por agora fica assim
                    date = Calendar.getInstance(),
                    distance = "${5 + i} km",
                    duration = "00:${25 + i}:00",
                    calories = "${200 + i * 30} kcal",
                    minimumPace = "5'3${i}\"/km",
                    minimap = R.drawable.map_image
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    RunupSetupTheme {
        MainView()
        /*HistoryItem(
            title = "Corrida de Segunda",
            date = Calendar.getInstance(),
            distance = "5 km",
            duration = "00:30:45",
            calories = "250 kcal",
            minimumPace = "5'30\"/km",
            minimap = R.drawable.map_image

        )*/
    }
}
