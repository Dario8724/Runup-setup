package pt.iade.RunUp.model.dto;

public class GoalDto {

    private String nome;
    private double total;
    private double progresso;

    public GoalDto(String nome, double total, double progresso) {
        this.nome = nome;
        this.total = total;
        this.progresso = progresso;
    }

    public String getNome() {
        return nome;
    }

    public double getTotal() {
        return total;
    }

    public double getProgresso() {
        return progresso;
    }
}
