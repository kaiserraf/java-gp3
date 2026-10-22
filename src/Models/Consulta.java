package Models;

import java.time.LocalDateTime;

// Subclasse concreta de Atendimento: consulta médica comum.
public class Consulta extends Atendimento {

    private static final int DURACAO_MINUTOS = 30;

    public Consulta(String codigo, Paciente paciente, Medico medico, LocalDateTime data) {
        super(codigo, paciente, medico, data);
    }

    @Override
    public String getTipo() {
        return "Consulta";
    }

    @Override
    public int getDuracaoMinutos() {
        return DURACAO_MINUTOS;
    }

    // Sobrescrita: realizar uma consulta, além de mudar o status (feito
    // pela superclasse), soma no contador de consultas do paciente.
    // Exame não faz isso, por isso a regra fica aqui e não em Atendimento.
    @Override
    public void realizar() {
        super.realizar();
        getPaciente().registrarConsultaRealizada();
    }
}
