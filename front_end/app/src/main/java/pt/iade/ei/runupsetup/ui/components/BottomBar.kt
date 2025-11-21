package pt.iade.ei.runupsetup.ui.components
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
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
fun ButtonItem(
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
@Composable
fun BottomBar(
){
    BottomAppBar(
        containerColor = Color(0xF3EDF7),
    ) {
    Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Unspecified,
                    containerColor = Color(0xF3EDF7)
                )
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Icon(
                        painter = painterResource(R.drawable.outline_home_24),
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
                    contentColor = Color.Unspecified,
                    containerColor = Color(0xF3EDF7)
                )
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Icon(
                        painter = painterResource(pt.iade.ei.runupsetup.R.drawable.outline_map_24),
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
                    contentColor = Color.Unspecified,
                    containerColor = Color(0xF3EDF7)
                )
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(pt.iade.ei.runupsetup.R.drawable.comunity_icon),
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
                    contentColor = Color.Unspecified,
                    containerColor = Color(0xF3EDF7)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_history_24),
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
                    contentColor = Color.Unspecified,
                    containerColor = Color(0xF3EDF7)
                )
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Icon(
                        Icons.Outlined.AccountCircle,
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

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    BottomBar()
}
