@file:OptIn(ExperimentalMaterial3Api::class)

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuestionnairePageView(
    currentStep: Int = 2,
    totalSteps: Int = 8,
    onBack: () -> Unit = {},
    onNext: (heightCm: Int, weightKg: Int, experience: String) -> Unit = { _, _, _ -> }
) {
    val backgroundColor = Color(0xFFFAF7F2)
    val accentColor = Color(0xFF6ECB63)

    val focusManager = LocalFocusManager.current

    // estados dos campos (salváveis ao recriar composição)
    var heightText by rememberSaveable { mutableStateOf("") }
    var weightText by rememberSaveable { mutableStateOf("") }
    var experience by rememberSaveable { mutableStateOf<String?>(null) }

    // dropdown (experience)
    val experienceOptions = listOf("Iniciante", "Intermediário", "Avançado")
    var expanded by remember { mutableStateOf(false) }

    // progresso animado
    val targetProgress = (currentStep.toFloat() / totalSteps.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = targetProgress)

    // validação simples para habilitar o botão Next
    val heightValid = heightText.toIntOrNull()?.let { it in 50..250 } == true
    val weightValid = weightText.toIntOrNull()?.let { it in 20..300 } == true
    val experienceValid = !experience.isNullOrBlank()
    val canProceed = heightValid && weightValid && experienceValid

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(horizontal = 24.dp, vertical = 18.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedButton(
                        onClick = {
                            focusManager.clearFocus()
                            onBack()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Voltar", fontSize = 16.sp)
                    }

                    Spacer(Modifier.width(16.dp))

                    Button(
                        onClick = {
                            // confirma novamente e devolve tipos convertidos
                            val h = heightText.toIntOrNull() ?: 0
                            val w = weightText.toIntOrNull() ?: 0
                            focusManager.clearFocus()
                            onNext(h, w, experience ?: "")
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        enabled = canProceed
                    ) {
                        Text("Próximo", color = Color.White, fontSize = 16.sp)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // contador + barra de progresso
            Text(text = "$currentStep/$totalSteps", color = Color.Gray, fontSize = 14.sp)
            LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .padding(top = 8.dp, bottom = 18.dp),
            color = accentColor,
            trackColor = Color(0xFFE0E0E0),
            strokeCap = StrokeCap.Round,
            )

            // título / instrução
            Text(
                text = "Algumas perguntas rápidas",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF222222),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Preencha as informações abaixo para personalizarmos sua experiência:",
                fontSize = 14.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // --- Campo: Altura ---
            OutlinedTextField(
                value = heightText,
                onValueChange = { input ->
                    // aceitar apenas números
                    val filtered = input.filter { it.isDigit() }
                    heightText = if (filtered.length <= 3) filtered else filtered.take(3)
                },
                label = { Text("Qual sua altura? (cm)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = heightText.isNotEmpty() && !heightValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            if (heightText.isNotEmpty() && !heightValid) {
                Text(
                    text = "Informe uma altura válida (ex.: 170)",
                    color = Color(0xFFB00020),
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }

            // --- Campo: Peso ---
            OutlinedTextField(
                value = weightText,
                onValueChange = { input ->
                    val filtered = input.filter { it.isDigit() }
                    weightText = if (filtered.length <= 3) filtered else filtered.take(3)
                },
                label = { Text("Qual seu peso? (kg)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = weightText.isNotEmpty() && !weightValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            if (weightText.isNotEmpty() && !weightValid) {
                Text(
                    text = "Informe um peso válido (ex.: 75)",
                    color = Color(0xFFB00020),
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }

            // --- Campo: Experiência (dropdown) ---
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = experience ?: "",
                    onValueChange = { },
                    label = { Text("Qual é a sua experiência com corrida?") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    experienceOptions.forEach { selection ->
                        DropdownMenuItem(
                            text = { Text(selection) },
                            onClick = {
                                experience = selection
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
fun QuestionnairePageViewPreview() {
    QuestionnairePageView(
        currentStep = 2,
        totalSteps = 8,
        onBack = {},
        onNext = { h, w, exp -> /* preview no-op */ }
    )
}
