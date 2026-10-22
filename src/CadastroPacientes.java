import Models.Paciente;

import java.util.HashMap;
import java.util.Map;

// Cadastro de pacientes usando Map: a CHAVE é o CPF (só dígitos), porque
// é o dado que identifica uma pessoa de forma única, e o VALOR é o próprio
// objeto Paciente. Buscar por CPF é direto (sem percorrer uma lista) e o
// Map impede dois pacientes com o mesmo CPF.
public class CadastroPacientes {

    private Map<String, Paciente> pacientesPorCpf = new HashMap<>();

    public void cadastrar(Paciente paciente) {
        // getCpf() já devolve só dígitos (Paciente.setCpf normaliza)
        if (pacientesPorCpf.containsKey(paciente.getCpf())) {
            throw new IllegalArgumentException("Já existe paciente cadastrado com o CPF " + paciente.getCpf());
        }
        pacientesPorCpf.put(paciente.getCpf(), paciente);
    }

    // aceita "111.111.111-11" ou "11111111111"; devolve null se não achar
    public Paciente buscarPorCpf(String cpf) {
        if (cpf == null) {
            return null;
        }
        return pacientesPorCpf.get(cpf.replaceAll("\\D", ""));
    }

    public boolean remover(String cpf) {
        return cpf != null && pacientesPorCpf.remove(cpf.replaceAll("\\D", "")) != null;
    }

    public int total() {
        return pacientesPorCpf.size();
    }
}
