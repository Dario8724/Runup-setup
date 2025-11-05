package pt.iade.ei.runupsetup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.models.HistoryItemModel1
import java.util.Calendar

class QuestionnaireGenderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComunityActivityView()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComunityActivityView() {
    val item1 = HistoryItemModel1(
        title = "Corrida de Segunda",
        date = Calendar.getInstance(),
        distance = "8.5 km",
        duration = "00:30:45",
        calories = "250 kcal",
        minimumPace = "5'30\"/km",
        minimap = R.drawable.map_image
    )
    val item2 = HistoryItemModel1(
        title = "Corrida aleatória",
        date = Calendar.getInstance(),
        distance = "12.3 km",
        duration = "1:45:20",
        calories = "250 kcal",
        minimumPace = "8'33\"/km",
        minimap = R.drawable.map_image
    )
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    //containerColor = Color.White
                     containerColor = Color(0xFF7CCE6B),
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
        },
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
                        onClick = {},
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
                        onClick = {},
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
                        onClick = {},
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(
                text = "Comunidade",
                fontSize = 25.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Conecte-se com outros corredores"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .padding(6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF7CCE6B)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ){
                    Text(
                        text = "Feed",
                        modifier = Modifier.padding(vertical = 10.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    }
                }
                Card(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .padding(6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF7CCE6B)
                    )
                ) {
                    Box(
                        modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "Desafios",
                            modifier = Modifier.padding(vertical = 10.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                }
            }
            Card(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                onClick = {},
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.Gray, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "MS", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "Maria Silva", fontWeight = FontWeight.SemiBold)
                            Text(
                                text = "há 2 horas",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Que manhã incrível! Consegui bater meu recorde pessoal 🎉🏃‍♀️",
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = item1.distance, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = "km", fontSize = 12.sp, color = Color.Gray)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = item1.duration, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = "tempo", fontSize = 12.sp, color = Color.Gray)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = item1.minimumPace, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = "min/km", fontSize = 12.sp, color = Color.Gray)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = item1.calories, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = "kcal", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    Row ( modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly){
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(
                                Icons.Default.FavoriteBorder,
                                contentDescription = "Like button"
                            )
                            Text(
                                text = "24",
                            )
                        }
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_mode_comment_24),
                                contentDescription = "Comment icon"
                            )
                            Text(
                                text = "5"
                            )
                        }
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = "Partilhar"
                            )
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
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.Gray, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "JC", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "João Costa", fontWeight = FontWeight.SemiBold)
                            Text(
                                text = "há 2 horas",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Trail running no fim de semana é tudo! Vista incrível lá do topo \uD83C\uDFD4\uFE0F",
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent, RoundedCornerShape(12.dp))
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = item2.distance, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = "km", fontSize = 12.sp, color = Color.Gray)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = item2.duration, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = "tempo", fontSize = 12.sp, color = Color.Gray)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = item2.minimumPace, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = "min/km", fontSize = 12.sp, color = Color.Gray)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = item2.calories, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = "kcal", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
                Row ( modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly){
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(
                            Icons.Default.FavoriteBorder,
                            contentDescription = "Like button"
                        )
                        Text(
                            text = "24",
                        )
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_mode_comment_24),
                            contentDescription = "Comment icon"
                        )
                        Text(
                            text = "5"
                        )
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Partilhar"
                        )
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ComunityActivityViewPreview() {
    ComunityActivityView()
}
