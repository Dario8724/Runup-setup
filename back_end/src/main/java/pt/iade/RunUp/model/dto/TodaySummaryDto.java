package pt.iade.RunUp.model.dto;

public class TodaySummaryDto {

    private double distanciaTotalKm;
    private long tempoTotalSegundos;
    private double paceMedioSegundosPorKm;
    private int caloriasTotais;

    public TodaySummaryDto() {
    }

    public TodaySummaryDto(double distanciaTotalKm,
                           long tempoTotalSegundos,
                           double paceMedioSegundosPorKm,
                           int caloriasTotais) {
        this.distanciaTotalKm = distanciaTotalKm;
        this.tempoTotalSegundos = tempoTotalSegundos;
        this.paceMedioSegundosPorKm = paceMedioSegundosPorKm;
        this.caloriasTotais = caloriasTotais;
    }

    public double getDistanciaTotalKm() {
        return distanciaTotalKm;
    }

    public void setDistanciaTotalKm(double distanciaTotalKm) {
        this.distanciaTotalKm = distanciaTotalKm;
    }

    public long getTempoTotalSegundos() {
        return tempoTotalSegundos;
    }

    public void setTempoTotalSegundos(long tempoTotalSegundos) {
        this.tempoTotalSegundos = tempoTotalSegundos;
    }

    public double getPaceMedioSegundosPorKm() {
        return paceMedioSegundosPorKm;
    }

    public void setPaceMedioSegundosPorKm(double paceMedioSegundosPorKm) {
        this.paceMedioSegundosPorKm = paceMedioSegundosPorKm;
    }

    public int getCaloriasTotais() {
        return caloriasTotais;
    }

    public void setCaloriasTotais(int caloriasTotais) {
        this.caloriasTotais = caloriasTotais;
    }
}