package pt.iade.ei.runupsetup.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff


class RegisterPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterView(
                onBackClick = { finish() },
                onNextClick = { nome, email, senha, nascimento ->
                    UserRegistrationState.nome = nome
                    UserRegistrationState.email = email
                    UserRegistrationState.senha = senha
                    UserRegistrationState.dataAniversario = nascimento
                    val intent = Intent(this, QuestionnaireGenderActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(
    onBackClick: () -> Unit = {},
    onNextClick: (String, String, String, String) -> Unit = { _, _, _, _ -> }
) {
    // Estados dos campos
    val nome = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val senha = remember { mutableStateOf("") }
    val confirmarSenha = remember { mutableStateOf("") }
    val nascimento = remember { mutableStateOf("") }

    val errorMessage = remember { mutableStateOf("") }
    val senhaVisivel = remember { mutableStateOf(false) }
    val confirmarSenhaVisivel = remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        containerColor = Color(0xFFFAF7F2),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0XFFFAF7F2)
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFAF7F2))
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Button(
                    onClick = {
                        // validações simples
                        errorMessage.value = ""
                        if (nome.value.isBlank() || email.value.isBlank() ||
                            senha.value.isBlank() || confirmarSenha.value.isBlank() ||
                            nascimento.value.isBlank()
                        ) {
                            errorMessage.value = "Preencha todos os campos."
                            return@Button
                        }
                        if (senha.value != confirmarSenha.value) {
                            errorMessage.value = "As senhas não coincidem."
                            return@Button
                        }
                        // validação simples da data YYYY-MM-DD
                        val dateRegex = Regex("^\\d{4}-\\d{2}-\\d{2}$")
                        if (!dateRegex.matches(nascimento.value)) {
                            errorMessage.value = "Data no formato inválido. Use YYYY-MM-DD."
                            return@Button
                        }
                        // validação simples de email
                        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
                        if (!emailRegex.matches(email.value)) {
                            errorMessage.value = "Email inválido."
                            return@Button
                        }

                        onNextClick(
                            nome.value,
                            email.value,
                            senha.value,
                            nascimento.value
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6ECB63),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "Próximo",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Crie sua conta",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = nome.value,
                onValueChange = { nome.value = it },
                label = { Text("Nome completo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = senha.value,
                onValueChange = { senha.value = it },
                label = { Text("Senha") },
                singleLine = true,
                visualTransformation = if (senhaVisivel.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (senhaVisivel.value) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { senhaVisivel.value = !senhaVisivel.value }) {
                        Icon(imageVector = icon, contentDescription = "Mostrar/ocultar senha")
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = confirmarSenha.value,
                onValueChange = { confirmarSenha.value = it },
                label = { Text("Confirmar senha") },
                singleLine = true,
                visualTransformation = if (confirmarSenhaVisivel.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (confirmarSenhaVisivel.value) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { confirmarSenhaVisivel.value = !confirmarSenhaVisivel.value }) {
                        Icon(imageVector = icon, contentDescription = "Mostrar/ocultar senha")
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = nascimento.value,
                onValueChange = { nascimento.value = it },
                label = { Text("Data de nascimento (YYYY-MM-DD)") },
                singleLine = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Selecionar data"
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)

            )

            // mostre erro, se houver
            if (errorMessage.value.isNotEmpty()) {
                Text(
                    text = errorMessage.value,
                    color = Color(0xFFB00020),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    fontSize = 14.sp
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterViewPreview() {
    RegisterView()
}