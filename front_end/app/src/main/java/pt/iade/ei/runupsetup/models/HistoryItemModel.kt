package pt.iade.ei.runupsetup.models

import java.util.Calendar

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
