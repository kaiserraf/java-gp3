package Models;

import java.time.LocalDateTime;

// Classe abstrata (Etapa 4): generaliza tudo que é comum entre Consulta
// e Exame (paciente, médico, data, status e as transições de estado).
// É abstract porque "um atendimento qualquer" não existe na clínica:
// ou é uma Consulta ou é um Exame, cada um com sua duração e seu tipo.
// Implementa Agendavel: por herança, as subclasses também são Agendavel.
public abstract class Atendimento implements Agendavel {

    // enum em vez de String solta: evita valores digitados errado
    public enum Status {
        AGENDADA,
        EM_ESPERA,
        REALIZADA,
        CANCELADA
    }

    private String codigo; // identificador único (controlado por um Set no gerenciador)
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime data;
    private Status status;

    protected Atendimento(String codigo, Paciente paciente, Medico medico, LocalDateTime data) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Atendimento precisa de um código");
        }
        if (paciente == null) {
            throw new IllegalArgumentException("Atendimento precisa de um paciente");
        }
        if (medico == null) {
            throw new IllegalArgumentException("Atendimento precisa de um médico");
        }
        this.codigo = codigo;
        this.paciente = paciente;
        this.medico = medico;
        this.setData(data);
        this.status = Status.AGENDADA;
    }

    // ---- métodos abstratos: cada subclasse é obrigada a implementar ----

    // "Consulta", "Exame"... usado para exibir o atendimento
    public abstract String getTipo();

    // duração em minutos; também cumpre o contrato de Agendavel
    @Override
    public abstract int getDuracaoMinutos();

    // ---- getters ----

    public String getCodigo() {
        return codigo;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    @Override
    public Medico getMedico() {
        return medico;
    }

    @Override
    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        if (data == null) {
            throw new IllegalArgumentException("Data não pode ser nula");
        }
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    // ---- transições de estado (sem setStatus público) ----

    public void entrarEmListaEspera() {
        if (status == Status.REALIZADA || status == Status.CANCELADA) {
            throw new IllegalStateException(getTipo() + " " + status + " não pode ir para a lista de espera");
        }
        this.status = Status.EM_ESPERA;
    }

    // usado pelo gerenciador quando uma vaga abre e o atendimento sai
    // da lista de espera para ser efetivamente agendado
    public void confirmar() {
        if (status == Status.REALIZADA || status == Status.CANCELADA) {
            throw new IllegalStateException(getTipo() + " " + status + " não pode ser confirmada");
        }
        this.status = Status.AGENDADA;
    }

    // subclasses podem sobrescrever e chamar super.realizar() (ver Consulta)
    public void realizar() {
        if (status != Status.AGENDADA) {
            throw new IllegalStateException("Só é possível realizar um atendimento que está agendado");
        }
        this.status = Status.REALIZADA;
    }

    public void cancelar() {
        if (status == Status.REALIZADA) {
            throw new IllegalStateException("Não é possível cancelar um atendimento já realizado");
        }
        this.status = Status.CANCELADA;
    }

    @Override
    public String toString() {
        return getTipo() + "{codigo=" + codigo +
                ", paciente=" + paciente.getNome() +
                ", medico=" + medico.getNome() +
                ", data=" + data +
                ", duracao=" + getDuracaoMinutos() + "min" +
                ", status=" + status +
                '}';
    }
}
