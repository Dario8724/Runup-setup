package pt.iade.ei.runupsetup.data.dto

import java.io.Serializable

data class RoutePointDto(
    val latitude: Double,
    val longitude: Double,
    val elevation: Double
) : Serializable