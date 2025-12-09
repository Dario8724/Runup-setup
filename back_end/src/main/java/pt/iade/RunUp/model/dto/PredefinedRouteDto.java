package pt.iade.RunUp.model.dto;

public class PredefinedRouteDto {
    private Integer rotaId;
    private String nome;
    private Double distanciaKm;
    private String tipo;        // "Corrida" ou "Caminhada"
    private String dificuldade; // "Fácil", "Intermédio", etc
    private String descricao;   // texto curto para o card

    public Integer getRotaId() {
        return rotaId;
    }

    public void setRotaId(Integer rotaId) {
        this.rotaId = rotaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(String dificuldade) {
        this.dificuldade = dificuldade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
