package pt.iade.ei.runupsetup.models

data class GenerateCorridaRequestDto(
    val userId: Int,
    val routeName: String,
    val tipoAtividade: String,
    val startLatitude: Double,
    val startLongitude: Double,
    val distanceKm: Double,
    val filtros: List<String>?
)