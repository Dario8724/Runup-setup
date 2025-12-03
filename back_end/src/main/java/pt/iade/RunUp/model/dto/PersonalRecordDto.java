package pt.iade.RunUp.model.dto;

import java.time.LocalDate;

public class PersonalRecordDto {
    private Double maiorDistanciaKm;
    private LocalDate dataCorrida;
    private Integer corridaId;

    public Double getMaiorDistanciaKm() {
        return maiorDistanciaKm;
    }

    public void setMaiorDistanciaKm(Double maiorDistanciaKm) {
        this.maiorDistanciaKm = maiorDistanciaKm;
    }

    public LocalDate getDataCorrida() {
        return dataCorrida;
    }

    public void setDataCorrida(LocalDate dataCorrida) {
        this.dataCorrida = dataCorrida;
    }

    public Integer getCorridaId() {
        return corridaId;
    }

    public void setCorridaId(Integer corridaId) {
        this.corridaId = corridaId;
    }
}