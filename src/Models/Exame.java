package Models;

import java.time.LocalDateTime;

// Subclasse concreta de Atendimento: exame solicitado/realizado com um
// médico. Tem dados próprios (nome do exame, jejum) que não existem em
// Consulta, e duração diferente.
public class Exame extends Atendimento {

    private static final int DURACAO_MINUTOS = 20;

    private String nomeExame;
    private boolean exigeJejum;

    public Exame(String codigo, Paciente paciente, Medico medico, LocalDateTime data,
                 String nomeExame, boolean exigeJejum) {
        super(codigo, paciente, medico, data);
        if (nomeExame == null || nomeExame.isBlank()) {
            throw new IllegalArgumentException("Nome do exame não pode ser vazio");
        }
        this.nomeExame = nomeExame;
        this.exigeJejum = exigeJejum;
    }

    public String getNomeExame() {
        return nomeExame;
    }

    public boolean isExigeJejum() {
        return exigeJejum;
    }

    @Override
    public String getTipo() {
        return "Exame (" + nomeExame + ")";
    }

    @Override
    public int getDuracaoMinutos() {
        return DURACAO_MINUTOS;
    }
}
