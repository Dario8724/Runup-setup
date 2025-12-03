package pt.iade.RunUp.model.dto;

public class WeeklyStatsDto {
    private double distanciaTotalKm;
    private int caloriasTotais;
    private long tempoTotalSegundos;

    public double getDistanciaTotalKm() {
        return distanciaTotalKm;
    }

    public void setDistanciaTotalKm(double distanciaTotalKm) {
        this.distanciaTotalKm = distanciaTotalKm;
    }

    public int getCaloriasTotais() {
        return caloriasTotais;
    }

    public void setCaloriasTotais(int caloriasTotais) {
        this.caloriasTotais = caloriasTotais;
    }

    public long getTempoTotalSegundos() {
        return tempoTotalSegundos;
    }

    public void setTempoTotalSegundos(long tempoTotalSegundos) {
        this.tempoTotalSegundos = tempoTotalSegundos;
    }
}