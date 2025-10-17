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
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Label
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
import androidx.compose.ui.text.style.TextAlign
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
                // criar uma página experimental para o projeto
                //
                HistoryDetailPageView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailPageView() {
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
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,

                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.Unspecified
                            // I have made it unspecified for now so it´s decided with the team in class
                            // also the thing is unspecified anyway
                        )
                    ) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription ="Início",
                            tint = Color.Black
                        )
                    }

                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.Unspecified
                            // I have made it unspecified for now so it´s decided with the team in class
                            // also the thing is unspecified anyway
                        )
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Rotas",
                            //  tint = Color.Black
                        )
                        /* Label
                         {
                             Text(
                                 text = "Rotas"
                             )
                         }*/
                        // added the location simbol since I can´t find the comunity icon
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.Unspecified
                            // I have made it unspecified for now so it´s decided with the team in class
                            // also the thing is unspecified anyway
                        )
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Comunidade",
                            tint = Color.Black
                        )
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.Unspecified
                            // I have made it unspecified for now so it´s decided with the team in class
                            // also the thing is unspecified anyway
                            // added the info icon because i could not find the right icon
                        )
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Perfil",
                            tint = Color.Black
                        )
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.Unspecified
                            // I have made it unspecified for now so it´s decided with the team in class
                            // also the thing is unspecified anyway
                        )
                    ) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "Perfil",
                            tint = Color.Black
                        )
                    }
                }
            }
        },
        // Todo: Experiment making the app without the top app bar
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
            //.padding(30.dp)
        ) {
            Row(
                modifier = Modifier.padding(innerPadding),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text(
                    text = "As suas atividades recentes",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Black,
                    fontSize = 40.sp
                )
            }
            Row (

            ) {
                Text(
                    text = item.title,
                    fontFamily = FontFamily.SansSerif,
                    //fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            // Mapa da corrida com tamanho original
            Image (
                painter = painterResource(item.minimap),
                contentDescription = "Mapa da corrida",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(250.dp)
                    .width(width = 380.dp)
            )
            Spacer(
                modifier = Modifier.height(30.dp)
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
        }
    }
}
// TODO: add labels below the icons and finish the history page

@Preview(showBackground = true)
@Composable
fun HistoryDetailPagePreview() {
    RunupSetupTheme {
        HistoryDetailPageView()
    }
}
