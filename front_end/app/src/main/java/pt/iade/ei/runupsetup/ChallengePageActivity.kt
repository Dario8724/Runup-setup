package pt.iade.ei.runupsetup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import okhttp3.Challenge

class ChallengePageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChallengePageView()
        }

    }

}

data class ChallengeItem(
    val title: String,
    val participants: Int,
    val progress: Int,
    val daysLeft: Int,
    val reward: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengePageView() {
    val challenges = listOf(
        ChallengeItem(
            title = "Desafio 100km Outubro",
            participants = 1243,
            progress = 65,
            daysLeft = 14,
            reward = "Medalha Virtual"

        ), ChallengeItem(
            title = "Streak de 7 Dias",
            participants = 856,
            progress = 42,
            daysLeft = 6,
            reward = "Badge Especial"
        )
    )

    Column(modifier = Modifier.padding(26.dp)) {}
    Text(text = "Desafio", fontSize = 26.sp, fontWeight = FontWeight.Black)

    Text(text = "Participe e ganhe premios exclusivos")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengePage() {
    Scaffold ( topBar = {  TopAppBar(title = {Text("Comunidade") },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White) )

}
