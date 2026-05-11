import data.DataStore;
import model.Autorizacao;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MenuAdmin {

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
    }

    public static void incluirUsuario(List<Usuario> usuarios,
                                      String nome) {

        boolean existe = false;

        for (String usuario : usuarios) {
            if (usuario.equalsIgnoreCase(nome)) {
                existe = true;
            }
        }

        if (nome.isEmpty()) {
            System.out.println("Nome inválido.");
        } else if (nome.length() < 3) {
            System.out.println("Nome muito curto.");
        } else if (existe) {
            System.out.println("OBS: Usuário já fora cadastrado.");
        } else {
            usuarios.add(nome);

            System.out.println("Usuário incluído com sucesso.");
        }
    }
}