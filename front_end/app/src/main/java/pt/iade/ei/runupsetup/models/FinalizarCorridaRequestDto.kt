package pt.iade.ei.runupsetup.models

data class FinalizarCorridaRequestDto(
    val userId: Int,
    val distanciaRealKm: Double,
    val duracaoSegundos: Long,
    val kcal: Int
)