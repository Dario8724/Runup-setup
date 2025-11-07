package pt.iade.ei.runupsetup.models

data class RouteRequest(
    val originLat: Double,
    val originLng: Double,
    val destLat: Double,
    val destLng: Double,
    val preferTrees: Boolean,
    val nearBeach: Boolean,
    val avoidHills: Boolean
)
