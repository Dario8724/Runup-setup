package pt.iade.ei.runupsetup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import pt.iade.ei.runupsetup.ui.RouteActivity

class RouteFiltersActivity : AppCompatActivity(){

    private lateinit var chkTrees: CheckBox
    private lateinit var chkBeach: CheckBox
    private lateinit var chkHills: CheckBox
    private lateinit var btnGenerate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_filters)

        chkTrees = findViewById(R.id.chkTrees)
        chkBeach = findViewById(R.id.chkBeach)
        chkHills = findViewById(R.id.chkHills)
        btnGenerate = findViewById(R.id.btnGenerateRoute)

        btnGenerate.setOnClickListener {
            val intent = Intent(this, RouteActivity::class.java).apply {
                putExtra("preferTress", chkTrees.isChecked)
                putExtra("nearBeach", chkBeach.isChecked)
                putExtra("avoidHills", chkHills.isChecked)
            }
            startActivity(intent)
        }
    }
}