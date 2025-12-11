package pt.iade.ei.runupsetup.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme

class PrivacyPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RunupSetupTheme {
                PrivacyPageView(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPageView(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Privacidade",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
        containerColor = Color(0xFFF7F7F7) // leve fundo para diferenciar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = "Política de Privacidade do RunUp",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "A RunUp está comprometida em proteger a sua privacidade. Esta política descreve como coletamos, usamos e protegemos as suas informações pessoais.",
                        fontSize = 15.sp,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Informações que Coletamos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Coletamos informações que você nos fornece diretamente, como nome, e-mail e dados de atividades físicas (distância, tempo, localização GPS, calorias e ritmo). Essas informações são necessárias para fornecer os serviços do aplicativo.",
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Como Usamos Suas Informações",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Utilizamos os seus dados para registrar e apresentar as suas atividades, calcular estatísticas, criar rotas personalizadas, permitir interação com a comunidade e melhorar continuamente a experiência do aplicativo.",
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Dados de localização são usados apenas quando você permite e para fornecer funcionalidades relacionadas ao treino e rotas.",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }




            Spacer(modifier = Modifier.height(20.dp))


        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacyPagePreview() {
    RunupSetupTheme {
        PrivacyPageView(onBack = {})
    }
}
