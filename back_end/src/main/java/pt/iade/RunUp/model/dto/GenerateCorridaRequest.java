package pt.iade.RunUp.model.dto;

import java.util.List;

public class GenerateCorridaRequest {

    private Integer userId;
    private String routeName;
    private TipoAtividade tipoAtividade;   
    private Double distanceKm;             
    private Double startLatitude;
    private Double startLongitude;
    private List<FiltroRota> filtros;

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

    public Double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(Double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public Double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(Double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public List<FiltroRota> getFiltros() {
        return filtros;
    }

    public void setFiltros(List<FiltroRota> filtros) {
        this.filtros = filtros;
    }
}
