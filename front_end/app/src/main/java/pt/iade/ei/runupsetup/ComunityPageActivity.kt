package pt.iade.ei.runupsetup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class QuestionnaireGenderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComunityActivityView()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComunityActivityView() {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7CCE6B),
                            contentColor = Color.Unspecified
                        )
                    ) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(
                                Icons.Default.Home,
                                contentDescription ="Botão para a página inicial",
                                tint = Color.Black,
                            )
                            Text(
                                text = "Início",
                                fontSize = 7.5.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7CCE6B),
                            contentColor = Color.Unspecified
                        )
                    ) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "Botão para a página de rotas",
                                tint = Color.Black
                            )
                            Text(
                                text = "Rotas",
                                fontSize = 7.5.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor =Color(0xFF7CCE6B),
                            contentColor = Color.Unspecified
                        )
                    ) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Botão para a página de comunidade",
                                tint = Color.Black
                            )
                            Text(
                                text = "Comunidade",
                                fontSize = 7.5.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7CCE6B),
                            contentColor = Color.Unspecified
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "Botão para a página de histórico",
                                tint = Color.Black
                            )
                            Text(
                                text = "Histórico",
                                fontSize = 7.5.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7CCE6B),
                            contentColor = Color.Unspecified
                        )
                    ) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = " Botão para a página de perfil",
                                tint = Color.Black
                            )
                            Text(
                                text = "Perfil",
                                fontSize = 7.5.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(
                text = "This is inner padding content",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComunityActivityViewPreview() {
    ComunityActivityView()
}
