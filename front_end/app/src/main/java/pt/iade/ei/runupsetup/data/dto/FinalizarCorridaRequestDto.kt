package pt.iade.ei.runupsetup.data.dto

data class FinalizarCorridaRequestDto(
    val userId: Int,
    val distanciaRealKm: Double,
    val duracaoSegundos: Long,
    val kcal: Int
)