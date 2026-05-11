import data.DataStore;
import model.Autorizacao;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MenuAdmin {

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

    public static void buscarPacientePorNome(String nome) {

        String nomeBusca = null;

        for (Paciente p : pacientes) {
            if (p.nome.equals(nomeBusca)) {
                System.out.println("Paciente encontrado: " + p.nome);
            }
        }
    }

    public static void buscarMedicoPorNome(String nome) {
        String nomeBusca = null;

        for (Medico m : medicos) {
            if (m.nome.equals(nomeBusca)) {
                System.out.println("Médico encontrado: " + m.nome);
            }
        }
    }
}
