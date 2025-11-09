package pt.iade.ei.runupsetup.models

data class RouteResponse(
    val id: Long,
    val name: String,
    val distance: Double,
    val duration: Double,
    val polyline: String
)
