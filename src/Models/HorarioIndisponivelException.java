package Models;

// Exceção personalizada (Etapa 4), ligada a uma regra real da clínica:
// um médico não pode ter dois atendimentos sobrepostos. É "checked"
// (extends Exception) de propósito: quem chama agendar() é OBRIGADO a
// decidir o que fazer (ex.: mandar o paciente para a lista de espera).
public class HorarioIndisponivelException extends Exception {
    public HorarioIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
