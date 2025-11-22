package pt.iade.ei.runupsetup.models

import java.io.Serializable

data class RouteResponse(
    val id: Long,
    val name: String,
    val distance: Double,
    val duration: Double,
    val polyline: String
): Serializable
