package pt.iade.ei.runupsetup.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.R
import pt.iade.ei.runupsetup.ui.components.BottomBarItem
import pt.iade.ei.runupsetup.ui.theme.RunupSetupTheme

class ComunityPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RunupSetupTheme {
                ComunityActivityView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComunityActivityView() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xFF7CCE6B),
                    titleContentColor = Color.White
                ),
                title = {
                    Column {
                        Text(
                            text = "Comunidade",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Funcionalidade disponível em breve",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, InitialPageActivity::class.java)
                            context.startActivity(intent)
                        },
                        icon = R.drawable.outline_home_24,
                        label = "Início"
                    )
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, RoutePageActivity::class.java)
                            context.startActivity(intent)
                        },
                        icon = R.drawable.outline_map_24,
                        label = "Rotas"
                    )
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, ComunityPageActivity::class.java)
                            context.startActivity(intent)
                        },
                        icon = R.drawable.comunity_icon,
                        label = "Comunidade"
                    )
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, HistoryPageActivity::class.java)
                            context.startActivity(intent)
                        },
                        icon = R.drawable.outline_history_24,
                        label = "Histórico"
                    )
                    BottomBarItem(
                        onclick = {
                            val intent = Intent(context, ProfilePageActivity::class.java)
                            context.startActivity(intent)
                        },
                        icon = R.drawable.outline_account_circle_24,
                        label = "Perfil"
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFFAF7F2)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Em breve",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7CCE6B)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComunityActivityViewPreview() {
    RunupSetupTheme {
        ComunityActivityView()
    }
}