package pt.iade.ei.runupsetup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.models.HistoryItemModel1
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme
import java.util.Calendar

class HistoryDetailPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RunupSetupTheme {
                // criar a função DetailView e menciona-lo aqui
                DetailView()
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
                    containerColor = Color(0xFF7CCE6B)
                    // unnecessary
                   //  navigationIconContentColor = Color.Black
                    // unnecessary
                    //titleContentColor = Color.Black,
                ),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = {},
                            // Cor do botão
                            // atenção que isso não muda a cor do vetor
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.Unspecified
                            ),
                            // unnecessary
                            // review later
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                                // faltava criar <string name="back">Back</string> no arquivo strings.xml
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                //.padding(30.dp)
        ) {
            Row (
            // modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = item.title,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            Row {
                Text(
                    text = " Data : ${item.date.get(Calendar.DAY_OF_MONTH)}/${item.date.get(Calendar.MONTH) + 1}/${item.date.get(Calendar.YEAR)}",
                    fontWeight = FontWeight.Black
                )
            }
            // Todo: adicionar Spacer entre a data e o mapa da corrida
            Spacer(modifier = Modifier.height(20.dp))
            // Mapa da corrida com tamanho original
            Image (
                painter = painterResource(item.minimap),
                contentDescription = "Mapa da corrida",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(250.dp)
                    .width(width = 380.dp)
                   // .fillMaxWidth()
                  // maybe it´s better for me to leave it like this instead of the first option
            )
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Pace médio",
                                fontWeight = FontWeight.Black
                            )
                            Text(text = item.minimumPace)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Tempo",
                                fontWeight = FontWeight.Black
                            )
                            Text(text = item.duration)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Distância",
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = item.distance
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally){
                            Text(
                                text = "Calorias",
                                fontWeight = FontWeight.Black
                            )
                            Text(text = item.calories)
                        }
                    }
                }
            // Todo: put this button in the middle of the page
            Button(
                onClick = {},
                modifier = Modifier.height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7CCE6B),
                    contentColor = Color.Unspecified
                ),
            ) {
               Row (
                   
               ){
                   Text(text = "whatever i don´t know what to put here ") }
            }
            /*
            Button(
                onClick = { },
                modifier = Modifier.height(50.dp)
            ) {
                val text = stringResource(R.string.add_calendar)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = text
                    )

                    Text(
                        text = text,
                        modifier = Modifier.padding(start = 5.dp)
                    )
*/

                }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailViewPreview() {
    RunupSetupTheme {
        DetailView()
    }
}
