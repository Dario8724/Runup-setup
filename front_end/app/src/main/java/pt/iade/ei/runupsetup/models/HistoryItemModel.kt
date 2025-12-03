package pt.iade.ei.runupsetup.models

import androidx.annotation.DrawableRes
import androidx.constraintlayout.utils.widget.MotionLabel
import java.util.Calendar
import java.time.LocalDate

data class HistoryItemModel(
    val corridaId: Int,
    val title : String,
    val date: Calendar,
    val distance: String,
    val duration: String,
    val calories : String,
    val minimumPace : String,
    val tipoLabel: String,
)
