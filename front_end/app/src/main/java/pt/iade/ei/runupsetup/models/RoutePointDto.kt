package pt.iade.ei.runupsetup.models

import java.io.Serializable

data class RoutePointDto(
    val latitude: Double,
    val longitude: Double,
    val elevation: Double
) : Serializable