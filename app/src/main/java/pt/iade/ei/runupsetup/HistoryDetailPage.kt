package pt.iade.ei.runupsetup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.iade.ei.runupsetup.models.HistoryItemModel1
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme
import java.util.Calendar

class HistoryDetailPage : ComponentActivity () {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RunupSetupTheme {
                // criar a função DetailView e menciona-lo aqui
                DetailView ()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailView() {
    val item = HistoryItemModel1(
        title = "Corrida de Segunda",
        date = Calendar.getInstance(),
        distance = "5 km",
        duration = "00:30:45",
        calories = "250 kcal",
        minimumPace = "5'30\"/km",
        minimap = R.drawable.map_image
    )
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Row ( verticalAlignment = Alignment.CenterVertically)
                    {
                        Button(
                            onClick = {}
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                                // faltava criar <string name="back">Back</string> no arquivo strings.xml

                            )

                        }
                        Text(
                            text = item.title,
                            modifier = Modifier.padding(start = 17.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            // Mapa da corrida com tamanho original
            Image(
                painter = painterResource(R.drawable.map_image),
                contentDescription = "Mapa da corrida",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
            // descriptio area
            // probably not
            Card (
                modifier = Modifier.padding(horizontal = 10.dp),
                onClick =  {}
            ){
                // substituindo item.description por uma string manual (já que description não existe no model)
                // consertar essa atrocidade amanhã
                // aumentar o tamanho e fazer com que tudo seja mais escalável
                Text(
                    "Distância: ${item.distance} | Duração: ${item.duration} | Calorias: ${item.calories} | Ritmo Mínimo: ${item.minimumPace}",
                    modifier = Modifier.padding(15.dp)
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DetailViewPreview(){
    RunupSetupTheme {
        DetailView()
    }
}
