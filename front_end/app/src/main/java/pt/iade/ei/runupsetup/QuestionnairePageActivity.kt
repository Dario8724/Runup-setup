package pt.iade.ei.runupsetup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.iade.ei.runupsetup.network.RetrofitClient
import pt.iade.ei.runupsetup.network.UserRegisterDto

class QuestionnairePageActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val snackbarHostState = remember { SnackbarHostState() }

            Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {

                QuestionnairePageView(
                    onBackClick = { finish() },

                    onNextClick = { alturaCm, pesoKg, experiencia ->

                        // Salvar no estado global
                        UserRegistrationState.altura = alturaCm / 100.0
                        UserRegistrationState.peso = pesoKg.toDouble()
                        UserRegistrationState.experiencia = experiencia

                        val dto = UserRegisterDto(
                            nome = UserRegistrationState.nome,
                            email = UserRegistrationState.email,
                            senha = UserRegistrationState.senha,
                            data_aniversario = UserRegistrationState.dataAniversario,
                            sexo = UserRegistrationState.sexo,
                            peso = UserRegistrationState.peso,
                            altura = UserRegistrationState.altura,
                            experiencia = UserRegistrationState.experiencia
                        )

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val response = RetrofitClient.instance.cadastrarUsuario(dto)

                                if (response.isSuccessful) {
                                    withContext(Dispatchers.Main) {
                                        startActivity(
                                            Intent(
                                                this@QuestionnairePageActivity,
                                                RegistrationSuccessActivity::class.java
                                            )
                                        )
                                        finish()
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        snackbarHostState.showSnackbar(
                                            "Erro ao salvar usuário: ${response.code()}"
                                        )
                                    }
                                }

                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    snackbarHostState.showSnackbar(
                                        "Falha ao comunicar com o servidor."
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionnairePageView(
    onBackClick: () -> Unit = {},
    onNextClick: (height: Int, weight: Int, experience: String) -> Unit = { _, _, _ -> }
) {
    val backgroundColor = Color(0xFFFAF7F2)
    val accentColor = Color(0xFF6ECB63)
    val focusManager = LocalFocusManager.current

    var heightText by rememberSaveable { mutableStateOf("") }
    var weightText by rememberSaveable { mutableStateOf("") }
    var experience by rememberSaveable { mutableStateOf<String?>(null) }
    val expOptions = listOf("Iniciante", "Intermediário", "Avançado")
    var expanded by remember { mutableStateOf(false) }

    val heightValid = heightText.toIntOrNull()?.let { it in 50..250 } == true
    val weightValid = weightText.toIntOrNull()?.let { it in 20..300 } == true
    val expValid = !experience.isNullOrBlank()
    val canProceed = heightValid && weightValid && expValid

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(24.dp)
            ) {
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onNextClick(
                            heightText.toInt(),
                            weightText.toInt(),
                            experience ?: ""
                        )
                    },
                    enabled = canProceed,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(accentColor)
                ) {
                    Text("Próximo", color = Color.White, fontSize = 18.sp)
                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Algumas perguntas rápidas",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF222222),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Preencha suas informações para personalizarmos sua experiência:",
                fontSize = 16.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // ALTURA
            OutlinedTextField(
                value = heightText,
                onValueChange = { input ->
                    val onlyDigits = input.filter { it.isDigit() }
                    heightText = onlyDigits.take(3)
                },
                label = { Text("Qual sua altura? (cm)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = heightText.isNotEmpty() && !heightValid,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // PESO
            OutlinedTextField(
                value = weightText,
                onValueChange = { input ->
                    val onlyDigits = input.filter { it.isDigit() }
                    weightText = onlyDigits.take(3)
                },
                label = { Text("Qual seu peso? (kg)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = weightText.isNotEmpty() && !weightValid,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // EXPERIÊNCIA
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = experience ?: "",
                    onValueChange = {},
                    label = { Text("Qual sua experiência com corrida?") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    expOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                experience = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuestionnairePageView() {
    QuestionnairePageView()
}
