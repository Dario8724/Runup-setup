package pt.iade.ei.runupsetup

import android.R.attr.id
import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.models.HistoryItemModel
import pt.iade.ei.runupsetup.ui.components.HistoryItem
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme
import java.util.Calendar

class LoginPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                LoginView()
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(){
    Scaffold(
        containerColor = Color(0xFFFAF7F2), //cor do fundo
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFAF7F2),
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            //Logo central
            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.logo_runup),
                contentDescription = "Logo RunUp",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 32.dp)
                    .aspectRatio(1f)
            )

            //Texto Central
            Text(
                text = "COMECE A SE MOVER E\nALCANCE HOJE MESMO\nSUAS METAS DE CORRIDA!",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = Color(0xFF222222),
                lineHeight = 30.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            //Botões inferiores
            //Botão Registar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Button(
                    onClick = { /* TODO: ação de login */},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6ECB63),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Registrar-se", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }

                //Botão Login
                OutlinedButton(
                    onClick = { /* TODO: ação de registrar */},
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Login", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginViewPrevieew() {
        LoginView()
    }
