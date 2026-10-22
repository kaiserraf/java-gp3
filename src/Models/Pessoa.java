package Models;

// Superclasse do domínio: representa o que é comum entre qualquer pessoa
// que circula pela clínica (quem atende e quem é atendido).
// É abstract porque "Pessoa" sozinha não representa ninguém real da
// clínica: só existe como Medico ou como Paciente.
public abstract class Pessoa {

    private String nome;

    protected Pessoa(String nome) {
        this.setNome(nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        this.nome = nome;
    }

    // Método comum a toda Pessoa: versão simples, sem parâmetros.
    // Cada subclasse sobrescreve para mostrar seus próprios dados além do nome.
    public void exibirResumo() {
        System.out.println(nome);
    }

    @Override
    public String toString() {
        return "Pessoa{nome='" + nome + "'}";
    }
}
