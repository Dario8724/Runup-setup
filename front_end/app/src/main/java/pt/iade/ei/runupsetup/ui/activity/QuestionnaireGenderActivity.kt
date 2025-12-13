package pt.iade.ei.runupsetup.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.utils.UserRegistrationState

class QuestionnaireGenderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuestionnaireGenderView(
                onBackClick = { finish() },
                onNextClick = { selectedGender ->

                    // Guardar no estado global
                    UserRegistrationState.sexo = selectedGender

                    // Ir para próxima tela
                    val next = Intent(this, QuestionnairePageActivity::class.java)
                    startActivity(next)
                }
            )
        }
    }
}


@Composable
fun QuestionnaireGenderView(
    onBackClick: () -> Unit = {},
    onNextClick: (String) -> Unit = {}
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }

    val options = listOf("Homem", "Mulher", "Outro")

    Scaffold(
        containerColor = Color(0xFFFAF7F2),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFAF7F2))
                    .padding(24.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(
                    onClick = {
                        if (selectedOption != null) onNextClick(selectedOption!!) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6ECB63),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                ) {
                    Text("Próximo", fontSize = 18.sp, fontWeight = FontWeight.Medium)
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
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Qual é o seu gênero?",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF222222),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Selecione uma opção:",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            options.forEach { option ->
                val isSelected = selectedOption == option

                OutlinedButton(
                    onClick = { selectedOption = option },
                    border = BorderStroke(
                        2.dp,
                        if (isSelected) Color.Black else Color(0xFFDDDDDD)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) Color(0xFFE8F5E9) else Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .height(55.dp)
                ) {
                    Text(
                        text = option,
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                    }
                }
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun QuestionnaireGenderPreview() {
    QuestionnaireGenderView()
}
