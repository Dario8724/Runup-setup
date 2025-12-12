package pt.iade.ei.runupsetup.data.dto

import java.io.Serializable

data class CorridaGeradaDto(
    val corridaId: Int,
    val userId: Int,
    val routeName: String?,
    val tipoAtividade: String,
    val distanceKm: Double,
    val estimatedDurationSeconds: Long,
    val paceMinPerKm: Double,
    val estimatedCalories: Int,
    val totalElevationGain: Double,
    val dataCriacao: String,
    val filtrosAplicados: List<String>?,
    val pontos: List<RoutePointDto>,
    val weatherCondition: String?,
    val ensolaradaAtendida: Boolean?
) : Serializable