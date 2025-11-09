package pt.iade.ei.runupsetup.ui


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import pt.iade.ei.runupsetup.R
import pt.iade.ei.runupsetup.RouteFiltersActivity



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNovaCorrida = findViewById<Button>(R.id.btnNovaCorrida)
        btnNovaCorrida.setOnClickListener {
            val intent = Intent(this, RouteFiltersActivity::class.java)
            startActivity(intent)
        }

        val btnHistorico = findViewById<Button>(R.id.btnHistorico)
        btnHistorico.setOnClickListener {
            // Aqui futuramente podemos abrir uma tela de histórico
        }
    }
}
