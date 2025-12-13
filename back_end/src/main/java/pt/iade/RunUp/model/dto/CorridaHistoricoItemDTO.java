package pt.iade.RunUp.model.dto;

import java.time.LocalDate;

public class CorridaHistoricoItemDTO {

    private Integer corridaId;
    private LocalDate data;
    private Double distanciaKm;
    private Long duracaoSegundos;
    private Double paceMinPorKm;
    private Integer kcal;
    private String tipo;
    private String routeName;
    private Double totalElevationGain;

    public Integer getCorridaId() {
        return corridaId;
    }

    public void setCorridaId(Integer corridaId) {
        this.corridaId = corridaId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public Long getDuracaoSegundos() {
        return duracaoSegundos;
    }

    public void setDuracaoSegundos(Long duracaoSegundos) {
        this.duracaoSegundos = duracaoSegundos;
    }

    public Double getPaceMinPorKm() {
        return paceMinPorKm;
    }

    public void setPaceMinPorKm(Double paceMinPorKm) {
        this.paceMinPorKm = paceMinPorKm;
    }

    public Integer getKcal() {
        return kcal;
    }

    public void setKcal(Integer kcal) {
        this.kcal = kcal;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Double getTotalElevationGain() {
        return totalElevationGain;
    }

    public void setTotalElevationGain(Double totalElevationGain) {
        this.totalElevationGain = totalElevationGain;
    }
}
