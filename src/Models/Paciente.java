package Models;

// Paciente é uma Pessoa: além do nome (herdado), tem idade, CPF,
// convênio e um contador de consultas realizadas.
public class Paciente extends Pessoa {

    private int idade;
    private String cpf;
    private boolean possuiConvenio;
    private int totalConsultasRealizadas;

    public Paciente(String nome, int idade, String cpf, boolean possuiConvenio) {
        super(nome); // delega para Pessoa a validação e o armazenamento do nome
        this.setIdade(idade);
        this.setCpf(cpf);
        this.possuiConvenio = possuiConvenio;
        this.totalConsultasRealizadas = 0;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        if (idade < 0 || idade > 130) {
            throw new IllegalArgumentException("Idade inválida");
        }
        this.idade = idade;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null) {
            throw new IllegalArgumentException("CPF inválido");
        }
        // aceita "11111111111" ou "111.111.111-11": remove tudo que não é dígito
        String somenteDigitos = cpf.replaceAll("\\D", "");
        if (!somenteDigitos.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF inválido, deve conter 11 dígitos");
        }
        this.cpf = somenteDigitos;
    }

    public boolean isPossuiConvenio() {
        return possuiConvenio;
    }

    public void setPossuiConvenio(boolean possuiConvenio) {
        this.possuiConvenio = possuiConvenio;
    }

    public int getTotalConsultasRealizadas() {
        return totalConsultasRealizadas;
    }

    // sem setter público: o número só sobe por aqui, chamado pela Consulta
    public void registrarConsultaRealizada() {
        this.totalConsultasRealizadas++;
    }

    // Sobrescrita (Atividade 8)
    @Override
    public void exibirResumo() {
        System.out.println(getNome() + ", " + idade + " anos"
                + (possuiConvenio ? " (com convênio)" : " (sem convênio)"));
    }

    // Sobrecarga (Atividade 5)
    public void exibirResumo(boolean detalhado) {
        exibirResumo();
        if (detalhado) {
            System.out.println("  CPF: " + cpf + " | Consultas realizadas: " + totalConsultasRealizadas);
        }
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "nome='" + getNome() + '\'' +
                ", idade=" + idade +
                ", cpf='" + cpf + '\'' +
                ", possuiConvenio=" + possuiConvenio +
                ", totalConsultasRealizadas=" + totalConsultasRealizadas +
                '}';
    }
}
