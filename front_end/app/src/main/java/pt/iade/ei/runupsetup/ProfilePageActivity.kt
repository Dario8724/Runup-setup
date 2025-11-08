package pt.iade.ei.runupsetup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
            BottomAppBar(containerColor = Color.White) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 🔹 Botão "Início" que volta para InitialPageActivity
                    Button(
                        onClick = {
                            val intent = Intent(context, InitialPageActivity::class.java)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Início",
                                tint = Color.Black
                            )
                            Text("Início", fontSize = 10.sp, color = Color.Black)
                        }
                    }

                    BottomNavButton(icon = Icons.Default.LocationOn, text = "Rotas")
                    BottomNavButton(icon = Icons.Default.Person, text = "Comunidade")

                    // 🔹 Histórico (vai para HistoryDetailPage)
                    Button(
                        onClick = {
                            val intent = Intent(context, HistoryDetailPage::class.java)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Info, contentDescription = "Histórico", tint = Color.Black)
                            Text("Histórico", fontSize = 10.sp, color = Color.Black)
                        }
                    }

                    BottomNavButton(icon = Icons.Default.AccountCircle, text = "Perfil", selected = true)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
                .padding(16.dp),
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