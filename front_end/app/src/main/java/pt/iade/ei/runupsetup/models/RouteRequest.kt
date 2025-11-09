package pt.iade.ei.runupsetup.models

import java.io.Serializable

data class RouteRequest(
    val nome: String,
    val originLat: Double,
    val originLng: Double,
    val destLat: Double,
    val destLng: Double,
    val desiredDistanceKm: Double,
    val preferTrees: Boolean,
    val nearBeach: Boolean,
    val nearPark: Boolean,
    val sunnyRoute: Boolean,
    val avoidHills: Boolean,
    val tipo: String
) : Serializable
