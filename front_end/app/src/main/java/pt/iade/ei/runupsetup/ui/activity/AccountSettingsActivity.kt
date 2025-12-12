package pt.iade.ei.runupsetup.ui.activity

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import pt.iade.ei.runupsetup.data.network.RetrofitClient
import pt.iade.ei.runupsetup.data.dto.UpdateUserProfileRequestDto
import pt.iade.ei.runupsetup.data.dto.UserProfileDto
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class AccountSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences("runup_prefs", MODE_PRIVATE)
        val loggedId = prefs.getLong("logged_id", -1L)

        setContent {
            RunupSetupTheme {
                AccountSettingsScreen(loggedId = loggedId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsScreen(loggedId: Long) {
    val context = LocalContext.current
    val activity = context as? Activity
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Campos de perfil
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("Masculino") }
    var idade by remember { mutableStateOf("") }

    // Dialogs para eliminar conta
    var showFirstDeleteDialog by remember { mutableStateOf(false) }
    var showSecondDeleteDialog by remember { mutableStateOf(false) }
    var deleteConfirmText by remember { mutableStateOf("") }

    // --------- Carregar dados do utilizador ----------
    LaunchedEffect(loggedId) {
        if (loggedId <= 0L) {
            errorMessage = "Usuário não logado."
            isLoading = false
            return@LaunchedEffect
        }

        try {
            val resp = RetrofitClient.instance.getUserProfile(loggedId)
            if (resp.isSuccessful) {
                val body: UserProfileDto? = resp.body()
                if (body != null) {
                    nome = body.nome
                    email = body.email
                    dataNascimento = body.dataNascimento ?: ""
                    altura = body.alturaCm?.toInt()?.toString() ?: ""
                    peso = body.pesoKg?.toInt()?.toString() ?: ""
                    genero = body.sexo ?: "Masculino"
                    idade = calculateAgeFromDateString(dataNascimento)?.toString() ?: ""
                }
            } else {
                errorMessage = "Erro ao carregar dados (${resp.code()})"
            }
        } catch (e: Exception) {
            errorMessage = "Falha ao comunicar com o servidor."
        } finally {
            isLoading = false
        }
    }

    // --------- UI ----------
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuração da Conta", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color(0xFFB00020),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // ---------- Informações Pessoais ----------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Informações Pessoais", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(16.dp))

                    LabeledTextField(
                        label = "Nome Completo",
                        value = nome,
                        onValueChange = { nome = it },
                        enabled = !isSaving
                    )

                    Spacer(Modifier.height(12.dp))

                    LabeledTextField(
                        label = "Email",
                        value = email,
                        onValueChange = { },
                        enabled = false
                    )

                    Spacer(Modifier.height(12.dp))

                    LabeledTextField(
                        label = "Data de Nascimento (AAAA-MM-DD)",
                        value = dataNascimento,
                        onValueChange = {
                            dataNascimento = it
                            idade = calculateAgeFromDateString(it)?.toString() ?: ""
                        },
                        enabled = !isSaving
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ---------- Dados Físicos ----------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Dados Físicos", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            LabeledTextField(
                                label = "Altura (cm)",
                                value = altura,
                                onValueChange = { new ->
                                    altura = new.filter { it.isDigit() }.take(3)
                                },
                                keyboardType = KeyboardType.Number,
                                enabled = !isSaving
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            LabeledTextField(
                                label = "Peso (kg)",
                                value = peso,
                                onValueChange = { new ->
                                    peso = new.filter { it.isDigit() }.take(3)
                                },
                                keyboardType = KeyboardType.Number,
                                enabled = !isSaving
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Gênero", fontSize = 12.sp, color = Color.Gray)
                            Spacer(Modifier.height(4.dp))

                            var expanded by remember { mutableStateOf(false) }
                            val options = listOf("Masculino", "Feminino", "Outro")

                            Box {
                                OutlinedTextField(
                                    value = genero,
                                    onValueChange = {},
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { expanded = true },
                                    enabled = !isSaving,
                                    readOnly = true,
                                    colors = outlinedTextFieldColors(
                                        disabledTextColor = Color.Black,
                                        disabledBorderColor = Color(0xFFE0E0E0)
                                    )
                                )

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    options.forEach { opt ->
                                        DropdownMenuItem(
                                            text = { Text(opt) },
                                            onClick = {
                                                genero = opt
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            LabeledTextField(
                                label = "Idade",
                                value = idade,
                                onValueChange = {},
                                enabled = false
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ---------- Botão Guardar ----------
            Button(
                onClick = {
                    if (loggedId <= 0L) {
                        Toast.makeText(context, "Usuário não logado.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (nome.isBlank()) {
                        Toast.makeText(context, "Preencha o nome.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    scope.launch {
                        isSaving = true
                        try {
                            val body = UpdateUserProfileRequestDto(
                                nome = nome,
                                dataNascimento = dataNascimento.ifBlank { null },
                                sexo = genero,
                                alturaCm = altura.toDoubleOrNull(),
                                pesoKg = peso.toDoubleOrNull()
                            )

                            val resp = RetrofitClient.instance.updateUserProfile(loggedId, body)
                            if (resp.isSuccessful) {
                                val updated = resp.body()

                                val newName = nome
                                val newEmail = email

                                val prefs = context.getSharedPreferences("runup_prefs", MODE_PRIVATE)
                                prefs.edit()
                                    .putString("logged_name", newName)
                                    .putString("logged_email", newEmail)
                                    .apply()

                                Toast.makeText(context, "Alterações guardadas!", Toast.LENGTH_SHORT).show()

                                activity?.finish()
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
                    Text("Guardar Alterações", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(16.dp))

            // ---------- Terminar Sessão ----------
            OutlinedButton(
                onClick = {
                    // logout
                    val prefs = context.getSharedPreferences("runup_prefs", MODE_PRIVATE)
                    prefs.edit()
                        .remove("logged_email")
                        .remove("logged_id")
                        .remove("logged_name")
                        .apply()

                    val intent = Intent(context, UserLoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                    activity?.finish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF555555)
                )
            ) {
                Text("Terminar Sessão")
            }

            Spacer(Modifier.height(16.dp))

            // ---------- Eliminar Conta ----------
            TextButton(
                onClick = { showFirstDeleteDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar Conta", color = Color(0xFFD32F2F))
            }
        }
    }

    // --------- 1ª confirmação ---------
    if (showFirstDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showFirstDeleteDialog = false },
            title = { Text("Eliminar conta") },
            text = {
                Text("Tem a certeza que pretende eliminar a sua conta? Esta ação é irreversível.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showFirstDeleteDialog = false
                    showSecondDeleteDialog = true
                }) {
                    Text("Continuar", color = Color(0xFFD32F2F))
                }
            },
            dismissButton = {
                TextButton(onClick = { showFirstDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // --------- 2ª confirmação (texto ELIMINAR) ---------
    if (showSecondDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showSecondDeleteDialog = false },
            title = { Text("Confirmar eliminação") },
            text = {
                Column {
                    Text("Para confirmar, escreva \"ELIMINAR\" abaixo.")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = deleteConfirmText,
                        onValueChange = { deleteConfirmText = it },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                val canDelete = deleteConfirmText.equals("ELIMINAR", ignoreCase = true)
                TextButton(
                    onClick = {
                        if (!canDelete || loggedId <= 0L) return@TextButton
                        // chamada para apagar a conta
                        scope.launch {
                            try {
                                val resp = RetrofitClient.instance.deleteUser(loggedId)
                                if (resp.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Conta eliminada.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // limpa sessão
                                    val prefs = context.getSharedPreferences(
                                        "runup_prefs",
                                        MODE_PRIVATE
                                    )
                                    prefs.edit().clear().apply()

                                    val intent =
                                        Intent(context, UserLoginActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(intent)
                                    activity?.finish()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Erro ao eliminar conta (${resp.code()})",
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
                                showSecondDeleteDialog = false
                                deleteConfirmText = ""
                            }
                        }
                    },
                    enabled = canDelete
                ) {
                    Text("Eliminar", color = if (canDelete) Color(0xFFD32F2F) else Color.Gray)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showSecondDeleteDialog = false
                    deleteConfirmText = ""
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = enabled,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType
            )
        )
    }
}

private fun calculateAgeFromDateString(dateStr: String): Int? {
    if (dateStr.isBlank()) return null
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val birth = LocalDate.parse(dateStr, formatter)
        val today = LocalDate.now()
        Period.between(birth, today).years
    } catch (e: DateTimeParseException) {
        null
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountSettingsPreview() {
    RunupSetupTheme {
        AccountSettingsScreen(loggedId = 1L)
    }
}