package pt.iade.ei.runupsetup.data.dto

data class HistoryItemDto(
    val corridaId: Int,
    val data: String,
    val distanciaKm: Double,
    val duracaoSegundos: Long,
    val paceMinPorKm: Double,
    val kcal: Int,
    val tipo: String,
    val routeName: String,
    val totalElevationGain: Double
)