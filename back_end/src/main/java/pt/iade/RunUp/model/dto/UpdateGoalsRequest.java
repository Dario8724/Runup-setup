package pt.iade.RunUp.model.dto;

public class UpdateGoalsRequest {
    private Double metaSemanalKm;
    private Double metaMensalKm;

    public Double getMetaSemanalKm() {
        return metaSemanalKm;
    }

    public void setMetaSemanalKm(Double metaSemanalKm) {
        this.metaSemanalKm = metaSemanalKm;
    }

    public Double getMetaMensalKm() {
        return metaMensalKm;
    }

    public void setMetaMensalKm(Double metaMensalKm) {
        this.metaMensalKm = metaMensalKm;
    }
}