package pt.iade.ei.runupsetup.ui.components

import android.icu.text.CaseMap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import java.sql.Date
import java.util.Calendar
import kotlin.time.Duration

@Composable
fun HistoryItem(
    // o title nesse caso é para o nome da corrida
    // ex: corrida de segunda a noite
    title: String,
    date: Calendar,
    distance: String,
    duration: Int, // se calhar devo mudar isso aqui para o tipo de dados equivalente
    //TO Do: pesquisar o seu tipo de dados
    calories : String,
    minimumPace : String,
    @DrawableRes minimap: Int
){
    Row {  }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painter = painterResource(minimap),
            contentDescription = "Minimapa da corrida",
            modifier = Modifier
                .height(70.dp)
                .padding(end = 10.dp)
        )
        Column {
        Text(
            text = title,
        )
            Text(
                text = minimumPace
            )
        }

    }
    // dario is working on this branch
}