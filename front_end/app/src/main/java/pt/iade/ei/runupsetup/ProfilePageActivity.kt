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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.network.RetrofitClient
import pt.iade.ei.runupsetup.network.UserStatsDto
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme

class ProfilePageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // pega usuário logado
        val prefs = getSharedPreferences("runup_prefs", MODE_PRIVATE)
        val loggedEmail = prefs.getString("logged_email", null)
        val loggedName = prefs.getString("logged_name", null)
        val loggedId = prefs.getLong("logged_id", -1L)

        setContent {
            RunupSetupTheme {

                // estados que vão ser carregados do backend
                var weeklyTotal by remember { mutableStateOf<Double?>(null) }
                var weeklyProgress by remember { mutableStateOf<Double?>(null) }
                var errorMessage by remember { mutableStateOf<String?>(null) }
                var isLoading by remember { mutableStateOf(true) }
                var stats by remember { mutableStateOf<UserStatsDto?>(null) }

                // carrega metas + stats do backend assim que a tela abrir
                LaunchedEffect(loggedId) {
                    if (loggedId <= 0L) {
                        isLoading = false
                        errorMessage = "Usuário não logado."
                        return@LaunchedEffect
                    }

                    try {
                        // 1) Metas
                        val goalsResponse = RetrofitClient.instance.getGoals(loggedId)
                        if (goalsResponse.isSuccessful) {
                            val goals = goalsResponse.body().orEmpty()
                            val weeklyGoal = goals.firstOrNull {
                                it.nome.contains("semanal", ignoreCase = true)
                            }
                            weeklyTotal = weeklyGoal?.total
                            weeklyProgress = weeklyGoal?.progresso
                        } else {
                            errorMessage = "Erro ao carregar metas (${goalsResponse.code()})"
                        }

                        // 2) Estatísticas do usuário
                        val statsResponse = RetrofitClient.instance.getUserStats(loggedId)
                        if (statsResponse.isSuccessful) {
                            stats = statsResponse.body()
                        } else {
                            // não bloqueia a tela, só registra o erro
                            println("Erro ao carregar stats (${statsResponse.code()})")
                        }

                    } catch (e: Exception) {
                        errorMessage = "Falha ao conectar ao servidor."
                    } finally {
                        isLoading = false
                    }
                }

                ProfilePageView(
                    userEmail = loggedEmail,
                    userName = loggedName,
                    weeklyTotal = weeklyTotal,
                    weeklyProgress = weeklyProgress,
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    stats = stats
                )
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
fun ProfilePageView(
    userEmail: String? = null,
    userName: String? = null,
    weeklyTotal: Double? = null,
    weeklyProgress: Double? = null,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    stats: UserStatsDto? = null
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF7CCE6B)),
                actions = {
                    TextButton(
                        onClick = {
                            val prefs = context.getSharedPreferences("runup_prefs", android.content.Context.MODE_PRIVATE)
                            prefs.edit()
                                .remove("logged_email")
                                .remove("logged_id")
                                .apply()

                            val intent = Intent(context, UserLoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                        }
                    ) {
                        Text("Logout", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
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
                    onClick = { navigateTo(context, HistoryPageActivity::class.java) }
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
            ProfileHeader(userEmail = userEmail, userName = userName, stats = stats)
            Spacer(modifier = Modifier.height(24.dp))
            WeeklyGoalCard(
                weeklyTotal = weeklyTotal,
                weeklyProgress = weeklyProgress,
                isLoading = isLoading,
                errorMessage = errorMessage
            )
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
fun getInitials(name: String?): String {
    if (name.isNullOrBlank()) return "--"

    val clean = name.substringBefore("@")

    val parts = clean.trim().split(" ")

    return when{
        parts.size == 2 ->
            (parts[0].take(1) + parts[1].take(1)).uppercase()

        clean.length >= 2 ->
            clean.take(2).uppercase()

        else -> "--"
    }
}

@Composable
fun ProfileHeader(
    userName: String?,
    userEmail: String?,
    stats: UserStatsDto?
) {
    val initials = getInitials(userName)

    val totalCorridas = stats?.totalCorridas ?: 0
    val totalKm = stats?.totalKm ?: 0.0
    val totalHoras = ((stats?.totalTempoSegundos ?: 0L) / 3600)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF7CCE6B), RoundedCornerShape(16.dp))
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // círculo com iniciais
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFB4E0A2)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = initials, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // email ou nome
            Text(
                text = userName ?: userEmail ?: "Usuário",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // estatísticas reais
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatCard(totalCorridas.toString(), "Corridas")
                StatCard(totalKm.toInt().toString(), "km Total")
                StatCard("${totalHoras}h", "Tempo")
            }
        }
    }
}

@Composable
fun StatCard(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 18.sp, color = Color.White)
        Text(label, fontSize = 12.sp, color = Color.White)
    }
}

@Composable
fun WeeklyGoalCard(
    weeklyTotal: Double?,
    weeklyProgress: Double?,
    isLoading: Boolean,
    errorMessage: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Meta Semanal", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
            Text("Distância a percorrer", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))

            when {
                isLoading -> {
                    Text("Carregando metas...", fontSize = 14.sp, color = Color.Gray)
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        color = Color(0xFF7CCE6B),
                        trackColor = Color.LightGray
                    )
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage,
                        fontSize = 14.sp,
                        color = Color(0xFFB00020)
                    )
                }

                weeklyTotal != null && weeklyProgress != null -> {
                    val current = weeklyProgress.coerceAtLeast(0.0)
                    val total = weeklyTotal.coerceAtLeast(0.0)
                    val progress = if (total > 0) (current / total).toFloat() else 0f
                    val restante = (total - current).coerceAtLeast(0.0)

                    Text(
                        text = String.format("%.1f / %.1f km", current, total),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progress },
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
                        text = "Faltam ${"%.1f".format(restante)} km para atingir sua meta! 💪",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }

                else -> {
                    Text("Nenhuma meta semanal encontrada.", fontSize = 14.sp, color = Color.Gray)
                }
            }
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
            Text("Estatísticas da Semana", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
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
            Text(title, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Text(value, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
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
                Text("Recordes Pessoais", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 16.sp)
                Icon(
                    painter = painterResource(R.drawable.yellow_trophy),
                    contentDescription = "Ícone de troféu",
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
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
                Text("Minhas Metas", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 16.sp)
                Text("+ Nova", color = Color(0xFF7CCE6B), fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            GoalCard("Correr 5km sem parar", progress = 0.75f, percent = "75%")
            Spacer(modifier = Modifier.height(12.dp))
            GoalCard("100km em Outubro", progress = 0.65f, percent = "65%")
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
            Text(title, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyCard(onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(60.dp),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Privacidade",
                fontSize = 16.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Ir",
                tint = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingPreferenciesCard(onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(60.dp),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Preferências de Treino",
                fontSize = 16.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Ir",
                tint = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsCard(onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(60.dp),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Configurações da Conta",
                fontSize = 16.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Ir",
                tint = Color.Gray
            )
        }
    }
}

// ---------- Previews ----------
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfilePagePreview() {
    RunupSetupTheme {
        ProfilePageView(
            userEmail = "user@test.com",
            userName = "Rafael Costa",
            weeklyTotal = 25.0,
            weeklyProgress = 18.5,
            isLoading = false,
            errorMessage = null,
            stats = UserStatsDto(
                totalCorridas = 23,
                totalKm = 112.0,
                totalTempoSegundos = 18 * 3600L
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    RunupSetupTheme {
        ProfileHeader(
            userName = "Rafael Costa",
            userEmail = "user@test.com",
            stats = UserStatsDto(
                totalCorridas = 23,
                totalKm = 112.0,
                totalTempoSegundos = 18 * 3600L
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyGoalCardPreview() {
    RunupSetupTheme {
        WeeklyGoalCard(
            weeklyTotal = 25.0,
            weeklyProgress = 18.5,
            isLoading = false,
            errorMessage = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyStatsCardPreview() {
    RunupSetupTheme {
        WeeklyStatsCard()
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalRecordsCardPreview() {
    RunupSetupTheme {
        PersonalRecordsCard()
    }
}

@Preview(showBackground = true)
@Composable
fun GoalCardPreview() {
    RunupSetupTheme {
        GoalCard(title = "Correr 5km sem parar", progress = 0.75f, percent = "75%")
    }
}

@Preview(showBackground = true)
@Composable
fun RecordCardPreview() {
    RunupSetupTheme {
        RecordCard(title = "Maior Distância", value = "12.3 km", bgColor = Color(0xFFFFF7E6))
    }
}

@Preview(showBackground = true)
@Composable
fun MyGoalsCardPreview() {
    RunupSetupTheme {
        MyGoalsCard()
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacyCardPreview() {
    RunupSetupTheme {
        PrivacyCard()
    }
}