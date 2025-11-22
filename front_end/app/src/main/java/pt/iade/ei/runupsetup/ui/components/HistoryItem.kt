package pt.iade.ei.runupsetup.ui.components

import android.text.format.DateFormat
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.R
import java.util.Calendar

@Composable
fun HistoryItem(
    // improve this item so its reusable in the history page and looks exactly like the mockups
    // todo: make as reusable as bottombar item
    title: String, // nome da atividade
    date: Calendar, // data da atividade
    distance: String, // distância percorrida
    duration: String,  // duração da atividade
    calories: String,  // calorias gastas
    minimumPace: String, // pace médio
    @DrawableRes minimap: Int // mapa da atividade

) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Card (
                    modifier = Modifier.padding(all=5.dp),
                    colors = CardDefaults.cardColors(Color(0xFF7CCE6B))){
                    Text(
                    text = title,
                    fontSize = 15.sp
                ) }
                Text(
                    text = DateFormat.format("dd ' ' MMM 'de' yyyy '•' kk ':' mm" , date).toString(),
                    fontSize = 15.sp
                )
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "seta para proseguir",
                    tint = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Row ( modifier = Modifier.padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    Icons.Outlined.LocationOn,
                    contentDescription = "Localização"
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Parque da Cidade")
            }
            Spacer(modifier = Modifier.height(18.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.outline_arrow_24),
                            contentDescription = "Arrow")
                        Text(
                            text = "Distância"
                        ) }
                    Text(
                        text = distance
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(R.drawable.outline_history_24),
                            contentDescription = "History Icon")
                        Text(
                        text = "Tempo"
                    )
                    }
                    Text(text = duration)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.outline_arrow_24),
                            contentDescription = "Arrow")
                        Text(
                            text = "Pace médio"
                        )
                    }
                    Text(text = minimumPace)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(R.drawable.outline_fire_24),
                            contentDescription = "Flames icon")
                        Text(
                        text = "Calorias"
                    )
                    }
                    Text(text = calories)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryItemPreview() {
    HistoryItem(
        title = "Corrida",
        date = Calendar.getInstance(),
        distance = "5 km",
        duration = "00:30:45",
        calories = "250 kcal",
        minimumPace = "5'30\"/km",
        minimap = R.drawable.map_image
    )
}
