package pt.iade.RunUp.model.dto;

import java.util.List;

public class CreateCorridaRequest {

    private Integer userId;
    private String routeName;
    private String tipoNome;        
    private Double distanceKm;      
    private Long tempoSegundos;     
    private Integer kcal;           
    private Double elevacaoTotal;   
    private List<String> filtros;   
    private List<RoutePointDTO> pontos;

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

    public String getTipoNome() {
        return tipoNome;
    }

    public void setTipoNome(String tipoNome) {
        this.tipoNome = tipoNome;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Long getTempoSegundos() {
        return tempoSegundos;
    }

    public void setTempoSegundos(Long tempoSegundos) {
        this.tempoSegundos = tempoSegundos;
    }

    public Integer getKcal() {
        return kcal;
    }

    public void setKcal(Integer kcal) {
        this.kcal = kcal;
    }

    public Double getElevacaoTotal() {
        return elevacaoTotal;
    }

    public void setElevacaoTotal(Double elevacaoTotal) {
        this.elevacaoTotal = elevacaoTotal;
    }

    public List<String> getFiltros() {
        return filtros;
    }

    public void setFiltros(List<String> filtros) {
        this.filtros = filtros;
    }

    public List<RoutePointDTO> getPontos() {
        return pontos;
    }

    public void setPontos(List<RoutePointDTO> pontos) {
        this.pontos = pontos;
    }
}
