package pt.iade.RunUp.model.dto;

public class UserStatsDto {
    private long totalCorridas;
    private double totalKm;
    private long totalTempoSegundos;

    public long getTotalCorridas() {
        return totalCorridas;
    }

    public void setTotalCorridas(long totalCorridas) {
        this.totalCorridas = totalCorridas;
    }

    public double getTotalKm() {
        return totalKm;
    }

    public void setTotalKm(double totalKm) {
        this.totalKm = totalKm;
    }

    public long getTotalTempoSegundos() {
        return totalTempoSegundos;
    }

    public void setTotalTempoSegundos(long totalTempoSegundos) {
        this.totalTempoSegundos = totalTempoSegundos;
    }
}