package pt.iade.ei.runupsetup.ui.components
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.iade.ei.runupsetup.InitialPageActivity
import pt.iade.ei.runupsetup.R

@Composable
fun BottomBarItem(
    @DrawableRes icon : Int,
    label : String,
    onclick : () -> Unit = {}
){
    Button(onClick = {},
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Unspecified,
            containerColor = Color(0xF3EDF7)
        )
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally){
             Icon( painter = painterResource(icon),
                 contentDescription = label,
                 tint = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = Color.Black,
                fontSize = 7.5.sp
            )
        }
    }
}
// testing the
@Composable
fun BottomBar(
) {
    BottomAppBar(
        containerColor = Color(0xF3EDF7),
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically){
            BottomBarItem(
            onclick = {},
            icon = R.drawable.outline_home_24,
            label = "Início"
        )
            BottomBarItem(
            onclick = {},
            icon = R.drawable.outline_map_24,
            label = "Rotas"
        )
            BottomBarItem(
            onclick = {},
            icon = R.drawable.comunity_icon,
            label = "Comunidade"
        )
            BottomBarItem(
            onclick = {},
            icon = R.drawable.outline_history_24,
            label = "Histórico"
        )
            BottomBarItem(
            onclick = {},
            icon = R.drawable.outline_account_circle_24,
            label = "Perfil"
        )
    }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    BottomBar()
}
