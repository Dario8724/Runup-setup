package pt.iade.ei.runupsetup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.iade.ei.runupsetup.network.LoginRequestDto
import pt.iade.ei.runupsetup.network.RetrofitClient

class UserLoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginFormView(
                onLoginClick = { email, senha, remember ->
                    lifecycleScope.launch {

                        val request = LoginRequestDto(email = email, senha = senha)

                        val response = try {
                            RetrofitClient.instance.login(request)
                        } catch (e: Exception) {
                            Toast.makeText(this@UserLoginActivity, "Erro ao conectar ao servidor", Toast.LENGTH_SHORT).show()
                            return@launch
                        }

                        if (response.isSuccessful && response.body() != null) {
                            val user = response.body()!!

                            Toast.makeText(this@UserLoginActivity, "Bem-vindo, ${user.nome}", Toast.LENGTH_SHORT).show()

                            // Lembrar usuário
                            if (remember) {
                                val prefs = getSharedPreferences("runup_prefs", MODE_PRIVATE)
                                prefs.edit()
                                    .putString("logged_email", user.email)
                                    .putInt("logged_id", user.userId)
                                    .apply()
                            }

                            // Navegar para Home
                            val intent = Intent(this@UserLoginActivity, InitialPageActivity::class.java)
                            startActivity(intent)
                            finish()

                        } else {
                            Toast.makeText(this@UserLoginActivity, "Email ou senha incorretos", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onRegisterClick = {
                    val intent = Intent(this, RegisterPageActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun LoginFormView(
    onLoginClick: (String, String, Boolean) -> Unit = { _, _, _ -> },
    onRegisterClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFFAF7F2)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(28.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Título
            Column {
                Text(
                    text = "Bem-vindo de volta!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF222222)
                )
                Text(
                    text = "Entre para continuar sua jornada.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                // Campo Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Senha
                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    label = { Text("Senha") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { showPassword = !showPassword }) {
                            Text(
                                text = if (showPassword) "Ocultar" else "Mostrar",
                                color = Color(0xFF6ECB63)
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Lembrar-me
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it }
                    )
                    Text(text = "Lembrar de mim", fontSize = 15.sp)
                }
            }

            // Botões
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Button(
                    onClick = { onLoginClick(email, senha, rememberMe) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6ECB63),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Entrar", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }

                TextButton(
                    onClick = onRegisterClick,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Não tem conta? Registre-se",
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginFormPreview() {
    LoginFormView()
}
