package Models;

import java.time.LocalDateTime;

// Contrato (Etapa 4): tudo que pode ocupar um espaço na agenda de um
// médico. Consulta e Exame são coisas diferentes, mas ambas "se agendam"
// do mesmo jeito: têm médico, início e duração. É uma interface (e não
// herança) porque descreve uma CAPACIDADE, não um "é um".
public interface Agendavel {

    LocalDateTime getData();

    Medico getMedico();

    // cada tipo de atendimento dura um tempo diferente
    int getDuracaoMinutos();

    // método default: regra única de conflito, reaproveitada por todos
    // que implementam a interface. Dois itens conflitam quando são do
    // MESMO médico e os intervalos [início, fim) se sobrepõem.
    // (resolve a limitação antiga de só detectar horário idêntico)
    default boolean conflitaCom(Agendavel outro) {
        if (this.getMedico() != outro.getMedico()) {
            return false;
        }
        LocalDateTime inicio = this.getData();
        LocalDateTime fim = inicio.plusMinutes(this.getDuracaoMinutos());
        LocalDateTime inicioOutro = outro.getData();
        LocalDateTime fimOutro = inicioOutro.plusMinutes(outro.getDuracaoMinutos());
        return inicio.isBefore(fimOutro) && inicioOutro.isBefore(fim);
    }
}
