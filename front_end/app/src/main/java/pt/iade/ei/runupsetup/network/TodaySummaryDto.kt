package pt.iade.ei.runupsetup.network

data class TodaySummaryDto(
    val distanciaTotalKm: Double,
    val tempoTotalSegundos: Long,
    val caloriasTotais: Int,
    val paceMedioSegundosPorKm: Double
)