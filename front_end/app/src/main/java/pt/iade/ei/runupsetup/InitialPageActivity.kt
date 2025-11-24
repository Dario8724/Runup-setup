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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.models.HistoryItemModel1
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme
import java.util.Calendar
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
import pt.iade.ei.runupsetup.ui.components.BottomBarItem


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
                    containerColor = Color(0xFF7CCE6B),
                ),
                title = {}
            )
        }
        ,
        bottomBar = {
            val context = LocalContext.current
            BottomAppBar(
                containerColor = Color.White,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, InitialPageActivity::class.java)
                            context.startActivity(intent)},
                        icon = R.drawable.outline_home_24,
                        label = "Início"
                    )
                    BottomBarItem(
                        onclick = {val intent = Intent(context, RoutePageActivity::class.java)
                                    context.startActivity(intent)
                             },
                        icon = R.drawable.outline_map_24,
                        label = "Rotas"
                    )
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, ComunityPageActivity::class.java)
                            context.startActivity(intent)
                        },
                        icon = R.drawable.comunity_icon,
                        label = "Comunidade")
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, HistoryDetailPage::class.java)
                            context.startActivity(intent)},
                        icon = R.drawable.outline_history_24,
                        label = "Histórico"
                    )
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, ProfilePageActivity::class.java)
                            context.startActivity(intent)},
                        icon = R.drawable.outline_account_circle_24,
                        label = "Perfil"
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // function containing the header
            InitialPageHeader()
            Card (
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                onClick = {},
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 12.dp
                )
            ){
                Column (modifier = Modifier.padding(16.dp)){
                    Row {
                        Text(
                            text = "Resumo de hoje",
                            fontSize = 25.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 8.dp )
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
            Column {
                    ShortCutCard(
                        icon = R.drawable.outline_map_24,
                        title = "Explorar rotas",
                        subtitle = "Descubra novos caminhos"
                    )
                ShortCutCard(
                    icon = R.drawable.outline_circle_circle_24,
                    title = "Metas pessoais",
                    subtitle = "Acompanhe seu progresso "
                )
                ShortCutCard(
                    icon = R.drawable.outline_history_24,
                    title = "Histórico",
                    subtitle = "Veja as suas atividades"
                )
                // probable card for conquests
                ShortCutCard(
                    icon = R.drawable.outline_map_24,
                    title = "Explorar rotas",
                    subtitle = "Acompanhe seu progresso "
                )
            }
        }
    }
}
@Composable
fun InitialPageHeader(){
    Box{

        Image(
            painter = painterResource(R.drawable.corredor_ao_por_do_sol),
            contentDescription = "Imagem de um corredor ao pôr do sol",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
        )
        Column(
            modifier = Modifier
                .align (Alignment.TopStart)
                .padding(start = 20.dp, top = 40.dp)
        ) {
            Text(
                text = "Olá, Corredor",
                fontWeight = FontWeight.Black,
                fontSize = 25.sp,
                color = Color.White
            )
            Text(
                text = "Comece a se mover e alcance hoje mesmo suas metas de corrida!",
                fontWeight = FontWeight.Black,
                fontSize = 25.sp,
                color = Color.White
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp)
                .align (Alignment.BottomEnd)
                .padding(start = 20.dp, top = 40.dp)
        ) {
            StartButton()
        }
    }
}
// Botão de iniciar
@Composable
fun StartButton(){
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(context, RouteFiltersActivity::class.java)
           context.startActivity(intent)
        },
        modifier = Modifier.height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF7CCE6B),
            contentColor = Color.Unspecified
        )
    ) {
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
@Composable
fun ShortCutCard(
    @DrawableRes icon : Int,
    title : String,
    subtitle : String,
    onclick : () -> Unit = {}
){
    Card (
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth(),
        onClick = {},
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            pressedElevation = 12.dp
        )
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp )
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "",
                tint = Color.Black
            )
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = subtitle
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "seta para proseguir",
                tint = Color.Black
            )
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
