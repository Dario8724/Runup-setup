package pt.iade.RunUp.model.dto;

import java.time.LocalDate;
import java.util.List;

public class CorridaDetalheResponse {

    private Integer corridaId;
    private Integer userId;
    private LocalDate data;
    private double distanciaKm;
    private long duracaoSegundos;
    private double paceMinPorKm;
    private int kcal;
    private String tipo;
    private String routeName;
    private double totalElevationGain;
    private List<RoutePointDTO> pontos;

    // GETTERS E SETTERS

    public Integer getCorridaId() {
        return corridaId;
    }

    public void setCorridaId(Integer corridaId) {
        this.corridaId = corridaId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public long getDuracaoSegundos() {
        return duracaoSegundos;
    }

    public void setDuracaoSegundos(long duracaoSegundos) {
        this.duracaoSegundos = duracaoSegundos;
    }

    public double getPaceMinPorKm() {
        return paceMinPorKm;
    }

    public void setPaceMinPorKm(double paceMinPorKm) {
        this.paceMinPorKm = paceMinPorKm;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
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

    public double getTotalElevationGain() {
        return totalElevationGain;
    }

    public void setTotalElevationGain(double totalElevationGain) {
        this.totalElevationGain = totalElevationGain;
    }

    public List<RoutePointDTO> getPontos() {
        return pontos;
    }

    public void setPontos(List<RoutePointDTO> pontos) {
        this.pontos = pontos;
    }
}
