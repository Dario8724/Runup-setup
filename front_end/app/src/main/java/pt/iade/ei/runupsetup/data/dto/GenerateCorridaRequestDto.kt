package pt.iade.ei.runupsetup.data.dto

data class GenerateCorridaRequestDto(
    val userId: Int,
    val routeName: String,
    val tipoAtividade: String,
    val startLatitude: Double,
    val startLongitude: Double,
    val distanceKm: Double,
    val filtros: List<String>?
)