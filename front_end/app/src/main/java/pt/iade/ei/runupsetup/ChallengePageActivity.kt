package pt.iade.ei.runupsetup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

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
fun ChallengePageView(){
    val challenges = listOf(
        ChallengeItem(
            title = "Desafio 100km Outubro",
            participants = 1243,
            progress = 65,
            daysLeft = 14,
            reward = "Medalha Virtual"




}