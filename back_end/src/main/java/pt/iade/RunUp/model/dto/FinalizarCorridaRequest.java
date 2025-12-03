package pt.iade.RunUp.model.dto;

public class FinalizarCorridaRequest {
    private Integer userId;
    private Double distanciaRealKm;
    private Long duracaoSegundos;
    private Integer kcal;

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getDistanciaRealKm() {
        return distanciaRealKm;
    }
    public void setDistanciaRealKm(Double distanciaRealKm) {
        this.distanciaRealKm = distanciaRealKm;
    }

    public Long getDuracaoSegundos() {
        return duracaoSegundos;
    }
    public void setDuracaoSegundos(Long duracaoSegundos) {
        this.duracaoSegundos = duracaoSegundos;
    }

    public Integer getKcal() {
        return kcal;
    }
    public void setKcal(Integer kcal) {
        this.kcal = kcal;
    }
}