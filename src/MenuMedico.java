import java.util.Scanner;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;

import data.DataStore;
import model.Autorizacao;
import model.Medico;
import model.Paciente;
import model.TipoExame;
import model.Usuario;

public class MenuMedico {

    public static void exibirMenu(Scanner scanner, Medico medicoAtual) {
        int opcao = -1;

        do {
            System.out.println("\n=== MENU DO MEDICO ===");
            System.out.println("1 - Nova autorizacao de exame");
            System.out.println("2 - Listar autorizacoes por paciente");
            System.out.println("3 - Listar autorizacoes por tipo de exame");
            System.out.println("4 - Cancelar autorizacao");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida.");
                continue;
            }

            switch (opcao) {
                case 1:
                    novaAutorizacao(scanner, medicoAtual);
                    break;
                case 2:
                    Paciente pacienteSelecionado = selecionaPaciente(scanner);
                    listarAutorizacoesPaciente(pacienteSelecionado);
                    break;
                case 3:
                    TipoExame exameSelecionado = selecionaExame(scanner);
                    listarAutorizacoesExame(exameSelecionado);
                    break;
                case 4:
                    cancelarAutorizacao(scanner, medicoAtual);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    public static void novaAutorizacao(Scanner scanner, Medico medicoAtual) {
        System.out.println("\n ====== NOVA AUTORIZAÇÃO ======\n");

        LocalDate dataCadastro = LocalDate.now();

        Medico medicoSelecionado = medicoAtual;
        System.out.println("Medico solicitante: " + medicoSelecionado.getNome());

        Paciente pacienteSelecionado = selecionaPaciente(scanner);

        TipoExame exameSelecionado = selecionaExame(scanner);

        if (pacienteSelecionado != null && exameSelecionado != null) {
            Autorizacao autorizacaoNova = new Autorizacao(dataCadastro, medicoSelecionado,
                    pacienteSelecionado, exameSelecionado);

            DataStore.adicionarAutorizacao(autorizacaoNova);

            System.out.println("\n === Autorizacão adicionada com sucesso === \n");
            System.out.println("Data da Autorização: " + dataCadastro);
            System.out.println("Medico Selecionado: " + medicoSelecionado);
            System.out.println("Paciente Selecionado : " + pacienteSelecionado);
            System.out.println("Exame Selecionado: " + exameSelecionado);
            System.out.println("\n=============================================\n");
        } else {
            System.out.println("\n === Autorizacão não foi adicionado, devido a falta de dados === \n");
        }
    }

    private static Paciente selecionaPaciente(Scanner scanner) {
        List<Paciente> pacientes = DataStore.getPacientes();

        int indice = getIndexSelecionado(scanner, pacientes, "Paciente");
        if (indice != -1) {
            return pacientes.get(indice);
        } else {
            return null;
        }
    }

    private static TipoExame selecionaExame(Scanner scanner) {
        TipoExame[] exames = TipoExame.values();

        int indice = getIndexSelecionado(scanner, Arrays.asList(exames), "Exame");
        if (indice != -1) {
            return exames[indice];
        } else {
            return null;
        }
    }

    private static void cancelarAutorizacao(Scanner scanner, Medico medicoAtual) {
        System.out.println("\n=== CANCELAR AUTORIZACAO ===\n");

        List<Autorizacao> minhas = new ArrayList<>();
        for (Autorizacao a : DataStore.getAutorizacoes()) {
            if (a.getMedicoSolicitante().equals(medicoAtual)
                    && !a.isRealizado() && !a.isCancelada()) {
                minhas.add(a);
            }
        }

        if (minhas.isEmpty()) {
            System.out.println("Voce nao tem autorizacoes pendentes para cancelar.");
            return;
        }

        int indice = getIndexSelecionado(scanner, minhas, "Autorizacao");
        if (indice == -1)
            return;

        Autorizacao selecionada = minhas.get(indice);

        System.out.print("Motivo do cancelamento: ");
        String motivo = scanner.nextLine().trim();

        if (selecionada.cancelar(motivo)) {
            System.out.println("Autorizacao #" + selecionada.getCodigo()
                    + " cancelada com sucesso.");
        } else {
            System.out.println("Nao foi possivel cancelar (prazo de 30 dias expirado).");
        }
    }

    private static <T> int getIndexSelecionado(Scanner scanner, List<T> lista, String nomeEntidade) {
        int tamanhoLista = lista.size();
        if (tamanhoLista == 0) {
            System.out.println("Não existe nenhum " + nomeEntidade);
            return -1;
        }

        System.out.println("Indice | " + nomeEntidade);

        for (int i = 0; i < tamanhoLista; i++) {
            System.out.println("  " + (i + 1) + "    - " + lista.get(i));
        }

        System.out.print("Digite o indice do " + nomeEntidade + ": ");
        int opcao = -1;
        while (opcao < 1 || opcao > tamanhoLista) {
            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());
                if (opcao < 1 || opcao > tamanhoLista) {
                    System.out.print("Digite um indice valido: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Entrada invalida. Digite um numero: ");
            }
        }

        return (opcao - 1);
    }

    private static void listarAutorizacoesExame(TipoExame exameAtual) {
        System.out.println("\n=== AUTORIZAÇÕES DO PACIENTE === \n");

        List<Autorizacao> minhasAutorizacoes = new ArrayList<>();

        // Passa pela lista global do DataStore e filtra com esse exame
        for (Autorizacao aut : DataStore.getAutorizacoes()) {
            if (aut.getTipoExame().equals(exameAtual)) {
                minhasAutorizacoes.add(aut);
            }
        }

        if (minhasAutorizacoes.isEmpty()) {
            System.out.println("Tipo de exame selecionado não possui nenhuma autorização");
            return;
        }
        minhasAutorizacoes.sort(Comparator.comparing(Autorizacao::getDataCadastro));

        for (Autorizacao aut : minhasAutorizacoes) {
            String status = aut.isRealizado() ? "Realizado" : "Pendente";
            System.out.println("Código: " + aut.getCodigo() +
                    "\n | Data Solicitação: " + aut.getDataCadastro() +
                    "\n | Médico Solicitante: " + aut.getMedicoSolicitante() +
                    "\n | Paciente: " + aut.getPaciente() +
                    "\n | Exame: " + aut.getTipoExame().getDescricao() +
                    "\n | Status: " + status +
                    "\n");
        }

    }

    private static void listarAutorizacoesPaciente (Usuario pacienteAtual) {
        System.out.println("\n=== AUTORIZAÇÕES DO PACIENTE === \n");

        List<Autorizacao> minhasAutorizacoes = new ArrayList<>();

        // Passa pela lista global do DataStore e filtra as do paciente
        for (Autorizacao aut : DataStore.getAutorizacoes()) {
            if (aut.getPaciente().equals(pacienteAtual)) {
                minhasAutorizacoes.add(aut);
            }
        }

        if (minhasAutorizacoes.isEmpty()) {
            System.out.println("Paciente selecionado não possui nenhuma autorização de exame.");
            return;
        }
        minhasAutorizacoes.sort(Comparator.comparing(Autorizacao::getDataCadastro));

        for (Autorizacao aut : minhasAutorizacoes) {
            String status = aut.isRealizado() ? "Realizado" : "Pendente";
            System.out.println("Código: " + aut.getCodigo() +
                    "\n | Data Solicitação: " + aut.getDataCadastro() +
                    "\n | Médico Solicitante: " + aut.getMedicoSolicitante() +
                    "\n | Paciente: " + aut.getPaciente() +
                    "\n | Exame: " + aut.getTipoExame().getDescricao() +
                    "\n | Status: " + status +
                    "\n");
        }
    }


}
