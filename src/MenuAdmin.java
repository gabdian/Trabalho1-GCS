import data.DataStore;
import model.Autorizacao;
import model.Medico;
import model.Paciente;
import model.TipoExame;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MenuAdmin {

    // F6 pt2 - Estatísticas por tipo de exame
    public static void estatisticasPorExame() {
        List<Autorizacao> autorizacoes = DataStore.getAutorizacoes();

        System.out.println("\n === AUTORIZAÇÕES POR TIPO DE EXAME ===\n");
        System.out.println("  " + "-".repeat(70));

        for (TipoExame tipo : TipoExame.values()) {
            long total = autorizacoes.stream()
                    .filter(a -> a.getTipoExame() == tipo)
                    .count();

            if (total > 0) {
                long feitos = autorizacoes.stream()
                        .filter(a -> a.getTipoExame() == tipo && a.isRealizado())
                        .count();
                System.out.printf("  %-25s %2d autorizações (%d realizadas)%n",
                        tipo.getDescricao() + ":", total, feitos);
            }
        }
        System.out.println("  " + "-".repeat(70));
    }


    // F6 - Estatísticas gerais
    public static void estatisticasGerais() {
        List<Medico> medicos = DataStore.getMedicos();
        List<Paciente> pacientes = DataStore.getPacientes();
        List<Autorizacao> autorizacoes = DataStore.getAutorizacoes();

        int totalMedicos = medicos.size();
        int totalPacientes = pacientes.size();
        int totalAutorizacoes = autorizacoes.size();

        long realizadas = autorizacoes.stream()
                .filter(a -> a.isRealizado())
                .count();

        double percentual = totalAutorizacoes > 0
                ? (realizadas * 100.0) / totalAutorizacoes
                : 0.0;

        System.out.println("\n === ESTATÍSTICAS GERAIS ===\n");
        System.out.println("  " + "-".repeat(40));
        System.out.printf("  Número de médicos: %d%n", totalMedicos);
        System.out.printf("  Número de pacientes: %d%n", totalPacientes);
        System.out.printf("  Autorizações emitidas: %d%n", totalAutorizacoes);
        System.out.printf("  Exames realizados: %d de %d (%.1f%%)%n",
                realizadas, totalAutorizacoes, percentual);
        System.out.println("  " + "-".repeat(70));
    }

    // F7 — Listar todos os exames pendentes do sistema

    public static void listarExamesPendentes() {
        System.out.println("\n  === EXAMES PENDENTES DO SISTEMA ===\n");

        // Filtra autorizacoes nao realizadas (dataRealizacao == null)
        List<Autorizacao> pendentes = DataStore.getAutorizacoes().stream()
                .filter(a -> !a.isRealizado())
                .sorted(Comparator.comparing(Autorizacao::getDataCadastro))
                .collect(Collectors.toList());

        if (pendentes.isEmpty()) {
            System.out.println("  Nenhum exame pendente no sistema.");
            return;
        }

        System.out.printf("  Total de exames pendentes: %d%n%n", pendentes.size());

        // Agrupa por tipo de exame
        Map<String, List<Autorizacao>> porTipo = pendentes.stream()
                .collect(Collectors.groupingBy(a -> a.getTipoExame().getDescricao()));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.println("  " + "-".repeat(70));
        for (Map.Entry<String, List<Autorizacao>> entry : porTipo.entrySet()) {
            System.out.printf("  [%s] — %d pendente(s)%n",
                    entry.getKey(), entry.getValue().size());

            for (Autorizacao a : entry.getValue()) {
                // Calcula dias restantes ate o prazo de 30 dias expirar
                long diasRestantes = ChronoUnit.DAYS.between(
                        LocalDate.now(), a.getDataCadastro().plusDays(30));

                String aviso = diasRestantes <= 5
                        ? " *** VENCE EM " + diasRestantes + " DIA(S)! ***"
                        : " (vence em " + diasRestantes + " dia(s))";

                System.out.printf(
                        "     Cod %-4d | Solicitado: %s | Paciente: %-18s | Medico: %s%s%n",
                        a.getCodigo(),
                        a.getDataCadastro().format(fmt),
                        a.getPaciente().getNome(),
                        a.getMedicoSolicitante().getNome(),
                        aviso);
            }
            System.out.println("  " + "-".repeat(70));
        }
        long criticos = pendentes.stream()
                .filter(a -> ChronoUnit.DAYS.between(
                        LocalDate.now(), a.getDataCadastro().plusDays(30)) <= 5)
                .count();

        if (criticos > 0) {
            System.out.printf("%n  ATENCAO: %d exame(s) vencem em 5 dias ou menos!%n", criticos);
        }
    }

    /**
     * Valida e adiciona um paciente para a lista
     * @param pacientes Lista de destino
     * @param nome Nome do paciente a ser cadastrado
     */

    public static void incluirPaciente(List<Paciente> pacientes,
                                       String nome) {

        boolean existe = false;

        // Verifica se o nome já consta na lista de pacientes
        for (Paciente p : pacientes) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                existe = true;
            }
        }

        // Validações de regras de negócio antes da inserção
        if (nome.isEmpty()) {
            System.out.println("Nome inválido.");
        } else if (nome.length() < 3) {
            System.out.println("Nome muito curto.");
        } else if (existe) {
            System.out.println("OBS: Paciente já fora cadastrado.");
        } else {
            //Como Usuário é abstrata, instancia-se a subclasse concreta Paciente
            Paciente novoPaciente;
            novoPaciente = new Paciente(+1, nome, "");
            pacientes.add(novoPaciente);

            System.out.println("Paciente incluído com sucesso.");
        }
    }

    /**
     * Valida e adiciona um médico para a lista
     * @param medicos Lista de destino
     * @param nome Nome do médico a ser cadastrado
     */
    public static void incluirMedico(List<Medico> medicos,
                                     String nome) {

        boolean existe = false;

        //Verifica se o nome já existe na lista de medicos
        for (Medico m : medicos) {
            if (m.getNome().equalsIgnoreCase(nome)) {
                existe = true;
            }
        }

        //Validações de regras de negócio antes da inserção
        if (nome.isEmpty()) {
            System.out.println("Nome inválido.");
        } else if (nome.length() < 3) {
            System.out.println("Nome muito curto.");
        } else if (existe) {
            System.out.println("OBS: Médico já fora cadastrado.");
        } else {
            // instanciação da subclasse concreta Médico
            Medico novoMedico;
            novoMedico = new Medico(1, nome, "");
            medicos.add(novoMedico);

            System.out.println("Médico incluído com sucesso.");
        }
    }

    /**
     * Procura um paciente na lista pelo nome
     * @param pacientes Lista onde a busca será executada
     * @param nome Critério de busca
     */
    public static void buscarPacientePorNome(List<Paciente> pacientes, String nome) {

        //Percorre a lista comparando o parâmetro 'nome' com o atributo do objeto
        for (Paciente p : pacientes) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                System.out.println("Paciente encontrado: " + p.getNome());
            }
            System.out.println("Paciente não existe" + nome);
        }
    }

    /**
     * Procura um médico na lista pelo nome
     * @param medicos Lista onde a busca será executada
     * @param nome Critério de busca
     */
    public static void buscarMedicoPorNome(List<Medico> medicos, String nome) {

        //Percorre a lista comparando o parâmetro 'nome' com o atributo do objeto
        for (Medico m : medicos) {
            if (m.getNome().equalsIgnoreCase(nome)) {
                System.out.println("Médico encontrado: " + m.getNome());
            }
            System.out.println("Médico não existe" + nome);
        }
    }
}
