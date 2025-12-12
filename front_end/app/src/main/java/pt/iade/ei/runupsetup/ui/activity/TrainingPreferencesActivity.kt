package pt.iade.ei.runupsetup.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import pt.iade.ei.runupsetup.data.network.RetrofitClient
import pt.iade.ei.runupsetup.data.dto.UpdateGoalsRequestDto
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme

class TrainingPreferencesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences("runup_prefs", MODE_PRIVATE)
        val loggedId = prefs.getLong("logged_id", -1L)

        setContent {
            RunupSetupTheme {
                TrainingPreferencesScreen(loggedId = loggedId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingPreferencesScreen(loggedId: Long) {
    val context = LocalContext.current
    val activity = context as? Activity
    val scope = rememberCoroutineScope()

    var weeklyText by rememberSaveable { mutableStateOf("") }
    var monthlyText by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Carrega metas atuais
    LaunchedEffect(loggedId) {
        if (loggedId <= 0L) {
            errorMessage = "Usuário não logado."
            isLoading = false
            return@LaunchedEffect
        }

        try {
            val response = RetrofitClient.instance.getGoals(loggedId)
            if (response.isSuccessful) {
                val goals = response.body().orEmpty()
                val weeklyGoal = goals.firstOrNull { it.nome.contains("semanal", ignoreCase = true) }
                val monthlyGoal = goals.firstOrNull { it.nome.contains("mensal", ignoreCase = true) }

                weeklyText = weeklyGoal?.total?.toInt()?.toString() ?: ""
                monthlyText = monthlyGoal?.total?.toInt()?.toString() ?: ""
            } else {
                errorMessage = "Erro ao carregar metas (${response.code()})"
            }
        } catch (e: Exception) {
            errorMessage = "Falha ao conectar ao servidor."
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preferências de Treino", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Card Meta Semanal
            GoalPreferenceCard(
                title = "Meta Semanal",
                label = "Meta semanal =",
                valueText = weeklyText,
                onValueChange = { input ->
                    val onlyDigits = input.filter { it.isDigit() }
                    weeklyText = onlyDigits.take(3)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Card Meta Mensal
            GoalPreferenceCard(
                title = "Meta Mensal",
                label = "Meta mensal =",
                valueText = monthlyText,
                onValueChange = { input ->
                    val onlyDigits = input.filter { it.isDigit() }
                    monthlyText = onlyDigits.take(3)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color(0xFFB00020),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            Button(
                onClick = {
                    if (loggedId <= 0L) {
                        Toast.makeText(context, "Usuário não logado.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val weekly = weeklyText.toDoubleOrNull()
                    val monthly = monthlyText.toDoubleOrNull()

                    if (weekly == null || monthly == null) {
                        Toast.makeText(context, "Preencha valores válidos em km.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    scope.launch {
                        isSaving = true
                        try {
                            val body = UpdateGoalsRequestDto(
                                metaSemanalKm = weekly,
                                metaMensalKm = monthly
                            )
                            val resp = RetrofitClient.instance.updateGoals(loggedId, body)
                            if (resp.isSuccessful) {
                                Toast.makeText(context, "Preferências atualizadas!", Toast.LENGTH_SHORT).show()
                                activity?.let{ act ->
                                    val intent = Intent(act, ProfilePageActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                    act.startActivity(intent)
                                    act.finish()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Erro ao guardar (${resp.code()})",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Falha ao comunicar com o servidor.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } finally {
                            isSaving = false
                        }
                    }
                },
                enabled = !isLoading && !isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6ECB63),
                    contentColor = Color.White
                )
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Guardar Preferências", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun GoalPreferenceCard(
    title: String,
    label: String,
    valueText: String,
    onValueChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(label, fontSize = 14.sp, color = Color.Gray)

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE8F5E9))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    TextField(
                        value = valueText,
                        onValueChange = onValueChange,
                        modifier = Modifier.width(60.dp),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = Color(0xFF6ECB63)
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))
                Text("km", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrainingPreferencesPreview() {
    RunupSetupTheme {
        TrainingPreferencesScreen(loggedId = 1L)
    }
}