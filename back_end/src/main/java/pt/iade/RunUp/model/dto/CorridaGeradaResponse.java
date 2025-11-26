package pt.iade.RunUp.dto;

import java.time.LocalDate;
import java.util.List;

public class CorridaGeradaResponse {

    private Integer corridaId;
    private Integer userId;
    private String routeName;
    private TipoAtividade tipoAtividade;
    private Double distanceKm;

    private Long estimatedDurationSeconds;
    private Double paceMinPerKm;
    private Integer estimatedCalories;
    private Double totalElevationGain;

    private LocalDate dataCriacao;
    private List<FiltroRota> filtrosAplicados;
    private List<RoutePointDTO> pontos;

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

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public TipoAtividade getTipoAtividade() {
        return tipoAtividade;
    }

    public void setTipoAtividade(TipoAtividade tipoAtividade) {
        this.tipoAtividade = tipoAtividade;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Long getEstimatedDurationSeconds() {
        return estimatedDurationSeconds;
    }

    public void setEstimatedDurationSeconds(Long estimatedDurationSeconds) {
        this.estimatedDurationSeconds = estimatedDurationSeconds;
    }

    public Double getPaceMinPerKm() {
        return paceMinPerKm;
    }

    public void setPaceMinPerKm(Double paceMinPerKm) {
        this.paceMinPerKm = paceMinPerKm;
    }

    public Integer getEstimatedCalories() {
        return estimatedCalories;
    }

    public void setEstimatedCalories(Integer estimatedCalories) {
        this.estimatedCalories = estimatedCalories;
    }

    public Double getTotalElevationGain() {
        return totalElevationGain;
    }

    public void setTotalElevationGain(Double totalElevationGain) {
        this.totalElevationGain = totalElevationGain;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public List<FiltroRota> getFiltrosAplicados() {
        return filtrosAplicados;
    }

    public void setFiltrosAplicados(List<FiltroRota> filtrosAplicados) {
        this.filtrosAplicados = filtrosAplicados;
    }

    public List<RoutePointDTO> getPontos() {
        return pontos;
    }

    public void setPontos(List<RoutePointDTO> pontos) {
        this.pontos = pontos;
    }
}
