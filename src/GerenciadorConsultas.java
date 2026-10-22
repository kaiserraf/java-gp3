import Models.Atendimento;
import Models.HorarioIndisponivelException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

// Gerencia a agenda de atendimentos (Consulta e Exame) da clínica.
public class GerenciadorConsultas {

    // List: a agenda precisa de ORDEM (inserção) e aceita percorrer tudo;
    // aqui ficam os atendimentos confirmados, sem conflito
    private List<Atendimento> agenda = new ArrayList<>();

    // List usada como fila (FIFO): quem entrou primeiro na espera é o
    // primeiro a ser remanejado quando uma vaga abre
    private List<Atendimento> listaEspera = new ArrayList<>();

    // Set: o código de um atendimento não pode se repetir na clínica.
    // O add() do Set devolve false se já existia, o que dá a checagem de
    // duplicidade em uma linha. Códigos não são reaproveitados, mesmo
    // depois de cancelamento (continuam identificando aquele registro).
    private Set<String> codigosRegistrados = new HashSet<>();

    // Tenta agendar. Se o médico já tem algo no intervalo, LANÇA a exceção
    // (a regra de negócio é verificada aqui); quem decide o que fazer
    // depois (ex.: lista de espera) é quem chamou, no try/catch do Main.
    public void agendar(Atendimento atendimento) throws HorarioIndisponivelException {
        if (codigosRegistrados.contains(atendimento.getCodigo())) {
            throw new IllegalArgumentException("Código de atendimento repetido: " + atendimento.getCodigo());
        }

        Atendimento conflitante = buscarConflito(atendimento);
        if (conflitante != null) {
            throw new HorarioIndisponivelException("Dr(a). " + atendimento.getMedico().getNome()
                    + " já tem " + conflitante.getTipo() + " em " + conflitante.getData()
                    + " (" + conflitante.getDuracaoMinutos() + " min), conflita com "
                    + atendimento.getTipo() + " de " + atendimento.getPaciente().getNome());
        }

        codigosRegistrados.add(atendimento.getCodigo());
        agenda.add(atendimento);
        System.out.println("Agendado: " + atendimento.getTipo() + " de " + atendimento.getPaciente().getNome()
                + " com Dr(a). " + atendimento.getMedico().getNome() + " em " + atendimento.getData());
    }

    public void entrarNaListaEspera(Atendimento atendimento) {
        if (!codigosRegistrados.add(atendimento.getCodigo())) {
            throw new IllegalArgumentException("Código de atendimento repetido: " + atendimento.getCodigo());
        }
        atendimento.entrarEmListaEspera();
        listaEspera.add(atendimento);
        System.out.println(atendimento.getPaciente().getNome() + " entrou na lista de espera ("
                + atendimento.getTipo() + ", Dr(a). " + atendimento.getMedico().getNome() + ")");
    }

    // procura na agenda algum atendimento AGENDADO que conflite com o dado
    private Atendimento buscarConflito(Atendimento novo) {
        for (Atendimento existente : agenda) {
            if (existente != novo
                    && existente.getStatus() == Atendimento.Status.AGENDADA
                    && novo.conflitaCom(existente)) {
                return existente;
            }
        }
        return null;
    }

    // Cancela e, se uma vaga abriu, remaneja da lista de espera.
    // Pode lançar IllegalStateException se o atendimento já foi realizado
    // (isso vem de Atendimento.cancelar(), antes de mexer nas listas).
    public void cancelar(Atendimento atendimento) {
        atendimento.cancelar();
        boolean estavaNaAgenda = agenda.remove(atendimento);
        listaEspera.remove(atendimento);
        System.out.println("Cancelado: " + atendimento.getTipo() + " de " + atendimento.getPaciente().getNome());

        if (estavaNaAgenda) {
            remanejarListaEspera();
        }
    }

    // Percorre a lista de espera com Iterator: é a forma segura de
    // REMOVER elementos durante o percurso (remover com for-each ou
    // list.remove() dentro do laço causaria ConcurrentModificationException).
    // Em ordem de chegada, promove todo mundo que agora cabe na agenda;
    // a cada promoção o conflito é recalculado, então não há sobreposição.
    private void remanejarListaEspera() {
        Iterator<Atendimento> it = listaEspera.iterator();
        while (it.hasNext()) {
            Atendimento espera = it.next();
            if (buscarConflito(espera) == null) {
                it.remove();
                espera.confirmar();
                agenda.add(espera);
                System.out.println("Vaga liberada! " + espera.getPaciente().getNome()
                        + " foi remanejado(a) da lista de espera: " + espera.getTipo());
            }
        }
    }

    // Outro uso de Iterator: limpa da agenda o que já foi realizado.
    // Devolve quantos foram removidos.
    public int removerAtendimentosRealizados() {
        int removidos = 0;
        Iterator<Atendimento> it = agenda.iterator();
        while (it.hasNext()) {
            if (it.next().getStatus() == Atendimento.Status.REALIZADA) {
                it.remove();
                removidos++;
            }
        }
        return removidos;
    }

    public void listarAgenda() {
        System.out.println("Agenda (" + agenda.size() + "):");
        for (Atendimento a : agenda) {
            System.out.println("  " + a); // polimorfismo: toString usa getTipo() de cada subclasse
        }
        System.out.println("Lista de espera (" + listaEspera.size() + "):");
        for (Atendimento a : listaEspera) {
            System.out.println("  " + a);
        }
    }

    // visões somente leitura: quem está fora não altera as listas internas
    public List<Atendimento> getAgenda() {
        return Collections.unmodifiableList(agenda);
    }

    public List<Atendimento> getListaEspera() {
        return Collections.unmodifiableList(listaEspera);
    }

    public Set<String> getCodigosRegistrados() {
        return Collections.unmodifiableSet(codigosRegistrados);
    }
}
