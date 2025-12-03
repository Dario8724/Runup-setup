package pt.iade.ei.runupsetup.models

data class CorridaDetalheDto(
    val corridaId: Int,
    val userId: Int?,
    val data: String,
    val distanciaKm: Double,
    val duracaoSegundos: Long,
    val paceMinPorKm: Double,
    val kcal: Int,
    val tipo: String,
    val routeName: String,
    val totalElevationGain: Double,
    val pontos: List<RoutePointDto>
)