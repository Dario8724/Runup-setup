package pt.iade.ei.runupsetup

import android.icu.text.CaseMap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
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

    Column(modifier = Modifier.padding(26.dp)) {
    Text(text = "Desafio", fontSize = 26.sp, fontWeight = FontWeight.Black)

    Text(text = "Participe e ganhe premios exclusivos")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengePage() {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Comunidade") },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

    }
    )

    { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Text(
                text = "Conecte-se com outros corredores",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.fillMaxWidth(20.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            )

            {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                        .background(
                            Color(0XFF7CCE6B),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(vertical = 12.dp), contentAlignment = Alignment.Center
                ) {
                    Text("Feed", fontWeight = FontWeight.Bold)
                }


            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(12)
                    )
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Desafios", fontWeight = FontWeight.Bold)

            }
        }


        Spacer(Modifier.height(10.dp))


        ChallengeCardSimple(
            title = "Desafio 100km Outubro",
            participants = "1243 participantes",
            progress = 65,
            dayLeft = 14,
            reward = "Medalha Virtual"
        )

        Spacer(Modifier.height(12.dp))


        ChallengePageView(
            title = "Streak  de 7 Dias",
            participants = "856 participantes",
            progress = 42,
            dayLeft = 6,
            reward = "Badge Especial"
        )


        Spacer(Modifier.height(20.dp))


    }
}

}


 @Preview(showBackground = true)
@Composable
 fun PreviewChallengePageView() {
     ChallengePageView()
 }

