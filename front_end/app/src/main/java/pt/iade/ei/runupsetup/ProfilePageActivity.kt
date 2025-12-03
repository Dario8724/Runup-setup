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
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.network.GoalDto
import pt.iade.ei.runupsetup.network.PersonalRecordDto
import pt.iade.ei.runupsetup.network.RetrofitClient
import pt.iade.ei.runupsetup.network.UserStatsDto
import pt.iade.ei.runupsetup.network.WeeklyStatsDto
import pt.iade.ei.runupsetup.ui.components.BottomBarItem
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
                var weeklyGoal by remember { mutableStateOf<GoalDto?>(null) }
                var monthlyGoal by remember { mutableStateOf<GoalDto?>(null) }
                var errorMessage by remember { mutableStateOf<String?>(null) }
                var isLoading by remember { mutableStateOf(true) }
                var stats by remember { mutableStateOf<UserStatsDto?>(null) }
                var weeklyStats by remember { mutableStateOf<WeeklyStatsDto?>(null) }
                var personalRecord by remember { mutableStateOf<PersonalRecordDto?>(null) }

                // carrega metas + stats do backend assim que a tela abrir
                LaunchedEffect(loggedId) {
                    if (loggedId <= 0L) {
                        isLoading = false
                        errorMessage = "Usuário não logado."
                        return@LaunchedEffect
                    }

                    try {
                        // 1) Metas (semanal e mensal)
                        val goalsResponse = RetrofitClient.instance.getGoals(loggedId)
                        if (goalsResponse.isSuccessful) {
                            val goals = goalsResponse.body().orEmpty()

                            weeklyGoal = goals.firstOrNull {
                                it.nome.contains("semanal", ignoreCase = true)
                            }

                            monthlyGoal = goals.firstOrNull {
                                it.nome.contains("mensal", ignoreCase = true)
                            }
                        } else {
                            errorMessage = "Erro ao carregar metas (${goalsResponse.code()})"
                        }

                        // 2) Estatísticas gerais do usuário (totais)
                        val statsResponse = RetrofitClient.instance.getUserStats(loggedId)
                        if (statsResponse.isSuccessful) {
                            stats = statsResponse.body()
                        } else {
                            println("Erro ao carregar stats (${statsResponse.code()})")
                        }

                        // 3) Estatísticas da semana (últimos 7 dias)
                        val weeklyStatsResponse = RetrofitClient.instance.getWeeklyStats(loggedId)
                        if (weeklyStatsResponse.isSuccessful) {
                            weeklyStats = weeklyStatsResponse.body()
                        } else {
                            println("Erro ao carregar weekly stats (${weeklyStatsResponse.code()})")
                        }

                        // 4) Recorde pessoal (maior distância)
                        val recordResponse = RetrofitClient.instance.getPersonalRecords(loggedId)
                        if (recordResponse.isSuccessful) {
                            personalRecord = recordResponse.body()
                        } else {
                            println("Erro ao carregar recorde pessoal (${recordResponse.code()})")
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
                    weeklyGoal = weeklyGoal,
                    monthlyGoal = monthlyGoal,
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    stats = stats,
                    weeklyStats = weeklyStats,
                    personalRecord = personalRecord
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
    weeklyGoal: GoalDto? = null,
    monthlyGoal: GoalDto? = null,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    stats: UserStatsDto? = null,
    weeklyStats: WeeklyStatsDto? = null,
    personalRecord: PersonalRecordDto? = null
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
            BottomAppBar(
                containerColor = Color.White
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, InitialPageActivity::class.java)
                            context.startActivity(intent)
                        },
                        icon = R.drawable.outline_home_24,
                        label = "Início"
                    )
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, RoutePageActivity::class.java)
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
                        label = "Comunidade"
                    )
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, HistoryPageActivity::class.java)
                            context.startActivity(intent)
                        },
                        icon = R.drawable.outline_history_24,
                        label = "Histórico"
                    )
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, ProfilePageActivity::class.java)
                            context.startActivity(intent)
                        },
                        icon = R.drawable.outline_account_circle_24,
                        label = "Perfil"
                    )
                }
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
                weeklyGoal = weeklyGoal,
                isLoading = isLoading,
                errorMessage = errorMessage
            )
            Spacer(modifier = Modifier.height(20.dp))
            WeeklyStatsCard(weeklyStats)
            Spacer(modifier = Modifier.height(20.dp))
            PersonalRecordsCard(personalRecord)
            Spacer(modifier = Modifier.height(20.dp))
            MyGoalsCard(
                weeklyGoal = weeklyGoal,
                monthlyGoal = monthlyGoal,
                isLoading = isLoading
            )
            PrivacyCard(
                onClick = {
                    val intent = Intent(context, PrivacyPageActivity::class.java)
                    context.startActivity(intent)
                }
            )
            TrainingPreferenciesCard ()
            AccountSettingsCard ()
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
    weeklyGoal: GoalDto?,
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

                weeklyGoal != null -> {
                    val current = weeklyGoal.progresso.coerceAtLeast(0.0)
                    val total = weeklyGoal.total.coerceAtLeast(0.0)
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
fun WeeklyStatsCard(weeklyStats: WeeklyStatsDto?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Estatísticas da Semana", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            if (weeklyStats == null) {
                Text(
                    "Sem dados dos últimos 7 dias.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            } else {
                val distText = String.format("%.1f km", weeklyStats.distanciaTotalKm)
                val kcalText = "${weeklyStats.caloriasTotais} kcal"

                val horas = weeklyStats.tempoTotalSegundos / 3600
                val minutos = (weeklyStats.tempoTotalSegundos % 3600) / 60
                val tempoText = "${horas}h ${minutos}min"

                StatRow("Distância Total", distText, "Últimos 7 dias")
                Spacer(modifier = Modifier.height(10.dp))

                StatRow("Calorias Queimadas", kcalText, "Últimos 7 dias")
                Spacer(modifier = Modifier.height(10.dp))

                StatRow("Tempo Total", tempoText, "Últimos 7 dias")
            }
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
fun PersonalRecordsCard(personalRecord: PersonalRecordDto?) {
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
                Text(
                    "Recordes Pessoais",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Icon(
                    painter = painterResource(R.drawable.yellow_trophy),
                    contentDescription = "Ícone de troféu",
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7E6)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Maior Distância", fontSize = 14.sp)

                    Spacer(Modifier.height(4.dp))

                    val distancia = personalRecord?.maiorDistanciaKm ?: 0.0
                    Text(
                        text = String.format("%.1f km", distancia),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    personalRecord?.dataCorrida?.let { dataStr ->
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Em $dataStr",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
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
fun MyGoalsCard(
    weeklyGoal: GoalDto?,
    monthlyGoal: GoalDto?,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text("Minhas Metas", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Text("Carregando metas...", color = Color.Gray)
                return@Column
            }

            // Meta semanal
            weeklyGoal?.let {
                GoalItem(title = "Meta Semanal", meta = it)
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Meta mensal
            monthlyGoal?.let {
                GoalItem(title = "Meta Mensal", meta = it)
            }

            if (weeklyGoal == null && monthlyGoal == null) {
                Text("Nenhuma meta encontrada.", color = Color.Gray)
            }
        }
    }
}

@Composable
fun GoalItem(title: String, meta: GoalDto) {
    val current = meta.progresso
    val total = meta.total
    val progress = if (total > 0) (current / total).toFloat() else 0f

    Column {
        Text(title, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("${"%.1f".format(current)} / ${"%.1f".format(total)} km", fontSize = 14.sp)
        Spacer(Modifier.height(8.dp))

        LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(8.dp)),
        color = Color(0xFF7CCE6B),
        trackColor = Color.LightGray,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )

        Spacer(Modifier.height(4.dp))
        Text("${(progress * 100).toInt()}%", fontSize = 12.sp, color = Color.Gray)
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
