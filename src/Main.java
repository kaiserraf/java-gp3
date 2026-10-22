import Models.Atendimento;
import Models.Consulta;
import Models.Exame;
import Models.HorarioIndisponivelException;
import Models.Medico;
import Models.Paciente;
import Models.Pessoa;

import java.time.LocalDateTime;

public class Main {

    // Ponto de tratamento da exceção (fluxo de interação do programa):
    // o gerenciador LANÇA HorarioIndisponivelException ao detectar o
    // conflito; aqui decidimos o que fazer com ela: avisar e mandar o
    // atendimento para a lista de espera. Separar assim mantém a regra no
    // gerenciador e a decisão de "o que fazer" com quem conversa com o usuário.
    private static void tentarAgendar(GerenciadorConsultas gerenciador, Atendimento atendimento) {
        try {
            gerenciador.agendar(atendimento);
        } catch (HorarioIndisponivelException e) {
            System.out.println("Erro: " + e.getMessage());
            gerenciador.entrarNaListaEspera(atendimento);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Medico drCarlos = new Medico("Carlos", "12345", "Cardiologia");
        Paciente joao = new Paciente("João", 40, "111.111.111-11", false);
        Paciente maria = new Paciente("Maria", 35, "222.222.222-22", false);

        System.out.println("--- Atividade 9: polimorfismo no Main ---");
        Pessoa pessoaA = drCarlos;
        Pessoa pessoaB = joao;
        pessoaA.exibirResumo();
        pessoaB.exibirResumo();

        System.out.println("\n--- Atividade 5: sobrecarga de exibirResumo ---");
        drCarlos.exibirResumo(true);
        joao.exibirResumo(true);

        // ---------------- Etapa 4: Map (pacientes por CPF) ----------------
        System.out.println("\n--- Map: cadastro de pacientes por CPF ---");
        CadastroPacientes cadastro = new CadastroPacientes();
        cadastro.cadastrar(joao);   // inclusão
        cadastro.cadastrar(maria);
        System.out.println("Pacientes cadastrados: " + cadastro.total());
        System.out.println("Busca por CPF 222.222.222-22: " + cadastro.buscarPorCpf("222.222.222-22").getNome()); // consulta

        try { // cenário inválido 1: CPF duplicado
            cadastro.cadastrar(new Paciente("João Clone", 41, "11111111111", false));
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        try { // cenário inválido 2: CPF mal formado (validação do próprio Paciente)
            cadastro.cadastrar(new Paciente("Ana", 20, "123", false));
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        // ---------------- Agendamento: List, Set, exceção ----------------
        System.out.println("\n--- Agendamento (Consulta e Exame são Atendimento/Agendavel) ---");
        GerenciadorConsultas gerenciador = new GerenciadorConsultas();
        LocalDateTime horario = LocalDateTime.of(2026, 8, 25, 14, 0);

        Consulta consultaJoao = new Consulta("C-001", joao, drCarlos, horario);
        Consulta consultaMaria = new Consulta("C-002", maria, drCarlos, horario);
        Exame exameJoao = new Exame("E-001", joao, drCarlos, horario.plusMinutes(15), "Eletrocardiograma", false);
        Exame exameMaria = new Exame("E-002", maria, drCarlos, horario.plusHours(1), "Ecocardiograma", true);

        tentarAgendar(gerenciador, consultaJoao);  // cenário válido: horário livre
        tentarAgendar(gerenciador, consultaMaria); // inválido: mesmo médico e horário -> espera
        tentarAgendar(gerenciador, exameJoao);     // inválido: 14:15 cai dentro da consulta das 14:00 (30 min) -> espera
        tentarAgendar(gerenciador, exameMaria);    // válido: 15:00, médico livre

        System.out.println("\n--- Set: código de atendimento repetido ---");
        tentarAgendar(gerenciador, new Consulta("C-001", maria, drCarlos, horario.plusHours(2))); // código já usado

        System.out.println("\n--- Percurso da agenda (List) ---");
        gerenciador.listarAgenda();
        System.out.println("Códigos registrados (Set): " + gerenciador.getCodigosRegistrados().size());

        // ---------------- Cancelamento: Iterator ----------------
        System.out.println("\n--- Cancelando a consulta do João (Iterator remaneja a espera) ---");
        gerenciador.cancelar(consultaJoao);
        // Maria entra (14:00 livre); o exame do João (14:15) continua esperando,
        // pois agora conflita com a consulta da Maria
        gerenciador.listarAgenda();

        // ---------------- Realização e erro de estado ----------------
        System.out.println("\n--- Maria realiza a consulta remanejada ---");
        consultaMaria.realizar();
        maria.exibirResumo(true); // totalConsultasRealizadas deve aparecer como 1

        System.out.println("\n--- Tentando cancelar consulta já realizada (cenário inválido) ---");
        try {
            gerenciador.cancelar(consultaMaria);
        } catch (IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("\n--- Iterator: removendo atendimentos realizados da agenda ---");
        int removidos = gerenciador.removerAtendimentosRealizados();
        System.out.println("Removidos: " + removidos);
        gerenciador.listarAgenda();
    }
}
