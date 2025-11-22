package pt.iade.ei.runupsetup
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.models.HistoryItemModel1
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme
import java.util.Calendar

class HistoryPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RunupSetupTheme {
                HistoryPageView()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryPageView() {
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
                    //titleContentColor = MaterialTheme.colorScheme.primary,
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
                    Button(
                        onClick = {
                            val intent = Intent(context, InitialPageActivity::class.java)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7CCE6B),
                            contentColor = Color.Unspecified
                        )
                    ) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(
                                Icons.Outlined.Home,
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
                            contentColor = Color.Black
                        )
                    ) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(
                                painter = painterResource(R.drawable.outline_map_24),
                                contentDescription = "Botão para a página de rotas",
                            )
                            Text(
                                text = "Rotas",
                                fontSize = 7.5.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Button(
                        onClick = {
                            val intent = Intent(context, ComunityPageActivity::class.java)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor =Color(0xFF7CCE6B),
                            contentColor = Color.Unspecified
                        )
                    ) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.comunity_icon),
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
                        onClick = {
                            val intent = Intent(context, HistoryPage::class.java)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7CCE6B),
                            contentColor = Color.Unspecified
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_history_24),
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
                        onClick = {
                            val intent = Intent(context, ProfilePageActivity::class.java)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7CCE6B),
                            contentColor = Color.Unspecified
                        )
                    ) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(
                                Icons.Outlined.AccountCircle,
                                contentDescription = " Botão para a página de perfil",
                                tint = Color.Black
                            )
                            Text(
                                text = "Perfil",
                                fontSize = 7.5.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(start = 4.dp, end = 4.dp),

        ) {
            Text(
                text = "Histórico ",
                fontSize = 25.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Suas atividades recentes"
            )
            Spacer(modifier = Modifier.height(4.dp))
            Card (
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                shape = RectangleShape,
                elevation = CardDefaults.cardElevation(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF7CCE6B)
                )
            ){
                Row (
                    modifier = Modifier
                                    .padding(horizontal = 24.dp, vertical = 20.dp)
                                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "23",
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Atividades",
                            color = Color.White
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "112" ,
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Text(
                            text = "km Total",
                            color = Color.White
                        )
                    }
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                        text = "18h",
                            fontSize = 22.sp,
                            color = Color.White
                    )
                        Text(
                            text = "Tempo Total",
                            color = Color.White
                        )
                    }
                }
            }

            Row (
                modifier = Modifier
                    .padding(start = 5.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                // this needs changing
                Text(
                    text = DateFormat.format("MMMM 'de' yyyy", item.date).toString()
                )
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFF7CCE6B)
                    )
                ) {
                    Icon(
                    Icons.Outlined.DateRange,
                    contentDescription = "Filtrar datas"
                )
                }
            }
            Card(
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
            ) {
                Column( modifier = Modifier.padding(16.dp)) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ){
                        Text(
                            text = "Very serious text "
                        )
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "seta para proseguir",
                            tint = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row ( modifier = Modifier.padding(vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = "Localização"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Parque da Cidade")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
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
            Card(
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
            ) {
                Column( modifier = Modifier.padding(16.dp)) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                       horizontalArrangement = Arrangement.SpaceBetween,
                    ){
                        Text(
                            text = "Very serious text "
                        )
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "seta para proseguir",
                            tint = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row ( modifier = Modifier.padding(vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = "Localização"
                        )
                            Spacer(modifier = Modifier.width(6.dp))
                        Text("Parque da Cidade")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
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
            Card(
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
            ) {
                Column( modifier = Modifier.padding(16.dp)) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ){
                        Text(
                            text = "Very serious text "
                        )
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "seta para proseguir",
                            tint = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row ( modifier = Modifier.padding(vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = "Localização"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Parque da Cidade")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
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
            Card(
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
            ) {
                Column( modifier = Modifier.padding(16.dp)) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ){
                        Text(
                            text = "Very serious text "
                        )
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "seta para proseguir",
                            tint = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row ( modifier = Modifier.padding(vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = "Localização"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Parque da Cidade")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
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
}
// TODO: polish this page so it looks better
// add some cards with the histories and maybe add total time and kilometer

@Preview(showBackground = true)
@Composable
fun HistoryDetailPagePreview() {
    RunupSetupTheme {
        HistoryPageView()
    }
}
