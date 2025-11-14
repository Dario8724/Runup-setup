package pt.iade.ei.runupsetup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme

class ProfilePageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RunupSetupTheme {
                ProfilePageView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePageView() {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Perfil", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF7CCE6B))
            )
        },
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
                            val intent = Intent(context, HistoryDetailPage::class.java)
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
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ----- Header -----
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF7CCE6B), RoundedCornerShape(16.dp))
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFB4E0A2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "RC", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Rafael Costa", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Membro desde Set 2025", color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StatCard("23", "Corridas")
                        StatCard("112", "km Total")
                        StatCard("18h", "Tempo")
                    }
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            // ----- Meta Semanal -----
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Meta Semanal", fontWeight = FontWeight.Bold)
                        Text("Editar", color = Color(0xFF7CCE6B), fontWeight = FontWeight.Medium)
                    }
                    Text("Distância a percorrer", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("18.5 / 25 km", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                    progress = { 18.5f / 25f },
                    modifier = Modifier
                                                .fillMaxWidth()
                                                .height(8.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                    color = Color(0xFF7CCE6B),
                    trackColor = Color.LightGray,
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Faltam 6.5 km para atingir sua meta! 💪",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ----- Estatísticas da Semana -----
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Estatísticas da Semana", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    StatRow("Distância Total", "18.5 km", "Últimos 7 dias")
                    Spacer(modifier = Modifier.height(10.dp))
                    StatRow("Calorias Queimadas", "1,240 kcal", "Últimos 7 dias")
                    Spacer(modifier = Modifier.height(10.dp))
                    StatRow("Tempo Total", "2h 45min", "Últimos 7 dias")
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Título e botão "+ Nova"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Minhas Metas",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            "+ Nova",
                            color = Color(0xFF7CCE6B),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    /*
                    // Meta 1
                    GoalProgressItem(
                        title = "Correr 5km sem parar",
                        progress = 0.75f
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Meta 2
                    GoalProgressItem(
                        title = "100km em Outubro",
                        progress = 0.65f
                    )*/

                }
            }
        }
    }
}

// ---------------- COMPONENTES REUTILIZÁVEIS ----------------

@Composable
fun StatCard(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
        Text(label, fontSize = 12.sp, color = Color.White)
    }
}

@Composable
fun StatRow(title: String, value: String, subtitle: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Text(value, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BottomNavButton(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, selected: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (selected) Color.Black else Color.Gray
        )
        Text(
            text = text,
            fontSize = 10.sp,
            color = if (selected) Color.Black else Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePagePreview() {
    RunupSetupTheme {
        ProfilePageView()
    }
}

// testing working with two previews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardTestView() {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(25.dp)
            ) {

                Column(modifier = Modifier.padding(20.dp)) {

                    // Título + Ícone
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recordes Pessoais",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            painter = painterResource(R.drawable.yellow_trophy),
                            contentDescription = "Ícone de troféu amarelo",
                            tint = Color.Unspecified
                        )
                    }

                    Spacer(modifier = Modifier.height(35.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        // ---- CARD: MAIOR DISTÂNCIA ----
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFF7E6) // Amarelo pastel igual ao mockup
                            ),
                            modifier = Modifier
                                .width(150.dp)
                                .height(95.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Maior Distância",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "12.3 km",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // ---- CARD: MELHOR RITMO ----
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF3F1FF) // Azul/lilás pastel do mockup
                            ),
                            modifier = Modifier
                                .width(150.dp)
                                .height(95.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Melhor Ritmo",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "6:38",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "min/km",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun CardPreview() {
    RunupSetupTheme {
        CardTestView()
    }
}
