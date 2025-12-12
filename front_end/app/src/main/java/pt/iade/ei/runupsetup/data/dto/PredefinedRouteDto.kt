package pt.iade.ei.runupsetup.data.dto

data class PredefinedRouteDto(
    val rotaId: Int,
    val nome: String,
    val distanciaKm: Double,
    val tipo: String,
    val dificuldade: String,
    val descricao: String?
)