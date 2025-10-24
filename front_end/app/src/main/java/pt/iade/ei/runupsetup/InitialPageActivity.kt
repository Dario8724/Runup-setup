package pt.iade.ei.runupsetup


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
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

/*
imports duvidosos
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Target
 */

class InitialPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RunupSetupTheme {
                InitialPageView()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialPageView() {
    val item = HistoryItemModel1(
        title = "Corrida de Segunda",
        date = java.util.Calendar.getInstance(),
        distance = "5 km",
        duration = "00:30:45",
        calories = "250 kcal",
        minimumPace = "5'30\"/km",
        minimap = R.drawable.map_image
    )
    // most of the content is not needed
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xFF7CCE6B),
                    //titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                                //  contentColor = Color.Unspecified
                                // not necessary
                            )
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
        ,
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
                            containerColor = Color(0xFF7CCE6B),
                            contentColor = Color.Unspecified
                            // I have made it unspecified for now so it´s decided with the team in class
                            // also the thing is unspecified anyway
                        )
                    ) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(
                                Icons.Default.Home,
                                contentDescription ="Botão para a página inicial",
                                tint = Color.Black,
                            )
                            Text(
                                text = "Início",
                                fontSize = 7.5.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7CCE6B),
                            contentColor = Color.Unspecified
                            // I have made it unspecified for now so it´s decided with the team in class
                            // also the thing is unspecified anyway
                        )
                    ) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "Botão para a página de rotas",
                                tint = Color.Black
                            )
                            Text(
                                text = "Rotas",
                                fontSize = 7.5.sp,
                                color = Color.Black
                            )
                        }
                        // added the location simbol since I can´t find the comunity icon
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor =Color(0xFF7CCE6B),
                            contentColor = Color.Unspecified
                            // I have made it unspecified for now so it´s decided with the team in class
                            // also the thing is unspecified anyway
                        )
                    ) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Botão para a página de comunidade",
                                tint = Color.Black
                            )
                            Text(
                                text = "Comunidade",
                                fontSize = 7.5.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7CCE6B),
                            contentColor = Color.Unspecified
                            // I have made it unspecified for now so it´s decided with the team in class
                            // also the thing is unspecified anyway
                            // added the info icon because i could not find the right icon
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "Botão para a página de histórico",
                                tint = Color.Black
                            )
                            Text(
                                text = "Histórico",
                                fontSize = 7.5.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7CCE6B),
                            contentColor = Color.Unspecified
                            // I have made it unspecified for now so it´s decided with the team in class
                            // also the thing is unspecified anyway
                        )
                    ) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally

                        ){
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = " Botão para a página de perfil",
                                tint = Color.Black
                            )
                            Text(
                                text = "Perfil",
                                fontSize = 7.5.sp,
                                color = Color.Black
                            )
                            // repair the profile button
                            // perhaps reduce the fontsize
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box{
                Image(
                    painter = painterResource(R.drawable.corredor_ao_por_do_sol),
                    contentDescription = "Imagem de um corredor ao pôr do sol",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
                Column(
                    modifier = Modifier
                        .align (Alignment.TopStart)
                        .padding(start = 20.dp, top = 40.dp)
                ) {
                    Text(
                        text = "Olá, Corredor",
                        fontWeight = FontWeight.Black,
                        fontSize = 25.sp
                    )
                    Text(
                        text = "Comece a se mover e alcance hoje mesmo suas metas de corrida!",
                        fontWeight = FontWeight.Black,
                        fontSize = 25.sp
                    )
                }
            }
            Card (
                onClick = {},
                modifier = Modifier.padding(horizontal = 10.dp),
                enabled = false,
                //.copy(alpha = 0.95f))
                // add later to the other buttons and cards that lead somewhere
                // false for now because it doesn't lead anywhere
            ){
                Column {
                    Row {
                        Text(
                            text = "Resumo de hoje",
                            fontSize = 25.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
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
            Text(
                text = "Atalhos rápidos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )
            // card for routes
            Card (
                modifier = Modifier.padding(10.dp),
                onClick = {},
                enabled = false
                // false for now
            ){
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp )
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Icone do mapa"
                    )
                    Column {
                        Text(
                            text = "Explorar rotas"
                        )
                        Text(
                            text = "Acompanhe seu progresso "
                        )
                    }
                    // seta para proseguir
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "seta para proseguir",
                        tint = Color.Black
                    ) }
            }
            // card for goals
            Card(
                modifier = Modifier.padding(10.dp),
                onClick = {},
                enabled = false
                // false for now
            )
            {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp )
                ){
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Botão para a página de metas"
                    )
                    Column {
                        Text(
                            text = "Metas pessoais",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Acompanhe seu progresso"
                        )
                    }
                    // seta para proseguir
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "seta para proseguir",
                        tint = Color.Black
                    )
                }
            }
            // card for history
            Card (
                modifier = Modifier.padding(10.dp),
                onClick = {},
                enabled = false,
            )
            {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Botão para a página de históricos"
                    )
                    Column {
                        Text(
                            text = "Histórico",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Veja as suas atividades"
                        )
                    }
                    // seta para proseguir
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "seta para proseguir",
                        tint = Color.Black
                    )
                }
            }

            // probable card for conquests
            /*
            Card (
                modifier
            ){
            }
             */
        }
        // Todo: add all the details to this area so the start button goes to the bottom
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp)
        ) {
            // Botão de iniciar
            Button(
                onClick = {},
                modifier = Modifier.height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7CCE6B),
                    contentColor = Color.Unspecified
                )
            ) {
                //val text = stringResource("Start")
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start button"
                    )
                    Text(
                        text = "Start",
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun InitialPagePreview() {
    RunupSetupTheme {
        InitialPageView()
    }
}