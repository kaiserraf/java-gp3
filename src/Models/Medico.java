package Models;

// Medico é uma Pessoa: além do nome (herdado), tem CRM, especialidade
// e disponibilidade — dados que só fazem sentido para quem atende.
public class Medico extends Pessoa {

    private String crm;
    private String especialidade;
    private boolean disponivel;

    public Medico(String nome, String crm, String especialidade) {
        super(nome); // delega para Pessoa a validação e o armazenamento do nome
        this.setCrm(crm);
        this.setEspecialidade(especialidade);
        this.disponivel = true;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        if (crm == null || crm.isBlank()) {
            throw new IllegalArgumentException("CRM não pode ser vazio");
        }
        this.crm = crm;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        if (especialidade == null || especialidade.isBlank()) {
            throw new IllegalArgumentException("Especialidade não pode ser vazia");
        }
        this.especialidade = especialidade;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    // métodos com intenção clara em vez de um setter genérico
    public void marcarComoOcupado() {
        this.disponivel = false;
    }

    public void marcarComoDisponivel() {
        this.disponivel = true;
    }

    // Sobrescrita (Atividade 8): mesma assinatura da superclasse, mas
    // o resumo de um médico também mostra especialidade e CRM.
    @Override
    public void exibirResumo() {
        System.out.println("Dr(a). " + getNome() + " - " + especialidade + " (CRM " + crm + ")");
    }

    // Sobrecarga (Atividade 5): mesmo nome, assinatura diferente (boolean).
    public void exibirResumo(boolean detalhado) {
        exibirResumo();
        if (detalhado) {
            System.out.println("  Disponível para novas consultas: " + (disponivel ? "sim" : "não"));
        }
    }

    @Override
    public String toString() {
        return "Medico{" +
                "nome='" + getNome() + '\'' +
                ", crm='" + crm + '\'' +
                ", especialidade='" + especialidade + '\'' +
                ", disponivel=" + disponivel +
                '}';
    }
}
