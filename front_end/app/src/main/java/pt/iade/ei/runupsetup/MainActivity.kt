package pt.iade.ei.runupsetup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.iade.ei.runupsetup.models.HistoryItemModel1
import pt.iade.ei.runupsetup.ui.components.HistoryItem
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RunupSetupTheme {
                MainView(
                    onGenerateRouteClick = {
                        val intent = Intent(this, pt.iade.ei.runupsetup.ui.RouteActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(onGenerateRouteClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("RunUp", fontWeight = FontWeight.Black) },
                actions = {
                    Button(
                        onClick = onGenerateRouteClick,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Nova Rota")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Histórico de Corridas"
                )
            }
        }
    ) { innerPadding ->
        val item = HistoryItemModel1(
            title = "Corrida de Segunda",
            date = Calendar.getInstance(),
            distance = "5 km",
            duration = "00:30:45",
            calories = "250 kcal",
            minimumPace = "5'30\"/km",
            minimap = R.drawable.map_image
        )

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
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

            for (i in 1..3) {
                HistoryItem(
                    title = "Corrida nº $i",
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
        MainView(onGenerateRouteClick = {})
    }
}
