package pt.iade.ei.runupsetup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme
import androidx.compose.ui.tooling.preview.Preview

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

// ---------------------------- NAVIGATION ----------------------------
fun navigateTo(context: android.content.Context, destination: Class<*>) {
    context.startActivity(Intent(context, destination))
}

// ---------------------------- PROFILE PAGE ----------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePageView() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF7CCE6B))
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Início") },
                    label = { Text("Início") },
                    selected = false,
                    onClick = { navigateTo(context, InitialPageActivity::class.java) }
                )
                NavigationBarItem(
                    icon = { Icon(painterResource(R.drawable.outline_map_24), contentDescription = "Rotas") },
                    label = { Text("Rotas") },
                    selected = false,
                    onClick = { /* Navegação Rotas */ }
                )
                NavigationBarItem(
                    icon = { Icon(painterResource(R.drawable.comunity_icon), contentDescription = "Comunidade") },
                    label = { Text("Comunidade") },
                    selected = false,
                    onClick = { navigateTo(context, ComunityPageActivity::class.java) }
                )
                NavigationBarItem(
                    icon = { Icon(painterResource(R.drawable.outline_history_24), contentDescription = "Histórico") },
                    label = { Text("Histórico") },
                    selected = false,
                    onClick = { navigateTo(context, HistoryDetailPage::class.java) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.AccountCircle, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = true,
                    onClick = { /* Já está na página de perfil */ }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF8F8F8))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeader()
            Spacer(modifier = Modifier.height(24.dp))
            WeeklyGoalCard()
            Spacer(modifier = Modifier.height(20.dp))
            WeeklyStatsCard()
            Spacer(modifier = Modifier.height(20.dp))
            PersonalRecordsCard()
            Spacer(modifier = Modifier.height(20.dp))
            MyGoalsCard()
        }
    }
}

// ---------------------------- COMPONENTS ----------------------------
@Composable
fun ProfileHeader() {
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
                Text("RC", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Rafael Costa", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Membro desde Set 2025", color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                StatCard("23", "Corridas")
                StatCard("112", "km Total")
                StatCard("18h", "Tempo")
            }
        }
    }
}

@Composable
fun StatCard(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
        Text(label, fontSize = 12.sp, color = Color.White)
    }
}

@Composable
fun WeeklyGoalCard() {
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
                progress = 18.5f / 25f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = Color(0xFF7CCE6B),
                trackColor = Color.LightGray,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Faltam 6.5 km para atingir sua meta! 💪",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun WeeklyStatsCard() {
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
fun PersonalRecordsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recordes Pessoais", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Icon(
                    painter = painterResource(R.drawable.yellow_trophy),
                    contentDescription = "Ícone de troféu",
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                RecordCard("Maior Distância", "12.3 km", Color(0xFFFFF7E6))
                RecordCard("Melhor Ritmo", "6:38 min/km", Color(0xFFF3F1FF))
            }
        }
    }
}

@Composable
fun RecordCard(title: String, value: String, bgColor: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = Modifier.height(100.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MyGoalsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Minhas Metas", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("+ Nova", color = Color(0xFF7CCE6B), fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            GoalCard("Correr 5km sem parar", 0.75f, "75%")
            Spacer(modifier = Modifier.height(12.dp))
            GoalCard("100km em Outubro", 0.65f, "65%")
        }
    }
}

@Composable
fun GoalCard(title: String, progress: Float, percent: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = Color(0xFF7CCE6B),
                trackColor = Color.LightGray,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(percent, fontSize = 12.sp, color = Color.Gray)
        }
    }
}



// ---------- Preview da tela completa ----------
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfilePagePreview() {
    RunupSetupTheme {
        ProfilePageView()
    }
}

// ---------- Preview do Header ----------
@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    RunupSetupTheme {
        ProfileHeader()
    }
}

// ---------- Preview da Meta Semanal ----------
@Preview(showBackground = true)
@Composable
fun WeeklyGoalCardPreview() {
    RunupSetupTheme {
        WeeklyGoalCard()
    }
}

// ---------- Preview das Estatísticas da Semana ----------
@Preview(showBackground = true)
@Composable
fun WeeklyStatsCardPreview() {
    RunupSetupTheme {
        WeeklyStatsCard()
    }
}

// ---------- Preview dos Recordes Pessoais ----------
@Preview(showBackground = true)
@Composable
fun PersonalRecordsCardPreview() {
    RunupSetupTheme {
        PersonalRecordsCard()
    }
}

// ---------- Preview do Card de Meta Individual ----------
@Preview(showBackground = true)
@Composable
fun GoalCardPreview() {
    RunupSetupTheme {
        GoalCard(title = "Correr 5km sem parar", progress = 0.75f, percent = "75%")
    }
}

// ---------- Preview do Card de Recorde Individual ----------
@Preview(showBackground = true)
@Composable
fun RecordCardPreview() {
    RunupSetupTheme {
        RecordCard(title = "Maior Distância", value = "12.3 km", bgColor = Color(0xFFFFF7E6))
    }
}

// ---------- Preview do MyGoalsCard ----------
@Preview(showBackground = true)
@Composable
fun MyGoalsCardPreview() {
    RunupSetupTheme {
        MyGoalsCard()
    }
}
