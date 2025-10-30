package pt.iade.ei.runupsetup.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.R
import java.util.Calendar

@Composable
fun HistoryItem(
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
        // Imagem do mapa
        Image(
            painter = painterResource(minimap),
            contentDescription = "Minimapa da corrida",
            modifier = Modifier
                .height(70.dp)
                .padding(end = 10.dp)
        )
        // Coluna com nome da corrida e a data
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            // Linha superior: nome e data
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "${date.get(Calendar.DAY_OF_MONTH)}/${date.get(Calendar.MONTH) + 1}/${date.get(Calendar.YEAR)}",
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Linha inferior: pace, tempo, distância e calorias
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Pace médio",
                        fontWeight = FontWeight.Black
                        )
                    Text(text = minimumPace)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Tempo",
                        fontWeight = FontWeight.Black
                        )
                    Text(text = duration)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Distância",
                        fontWeight = FontWeight.Black
                         )
                    Text(
                        text = distance
                        )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    Text(
                        text = "Calorias",
                        fontWeight = FontWeight.Black
                        )
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
        title = "Corrida de Segunda",
        date = Calendar.getInstance(),
        distance = "5 km",
        duration = "00:30:45",
        calories = "250 kcal",
        minimumPace = "5'30\"/km",
        minimap = R.drawable.map_image
    )
}
