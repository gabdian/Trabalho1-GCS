import java.util.Scanner;

import data.DataStore;
import model.Autorizacao;
import model.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class MenuPaciente {

    public static void exibirMenu(Usuario pacienteAtual) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n=== MENU DO PACIENTE ===");
            System.out.println("1 - Listar minhas autorizações");
            System.out.println("2 - Marcar exame como realizado");
            System.out.println("3 - Filtrar autorizações por médico");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    listarAutorizacoes(pacienteAtual);
                    break;
                case 2:
                    marcarExameComoRealizado(pacienteAtual);
                    break;
                case 3:
                    filtrarAutorizacoesPorMedico(pacienteAtual);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void filtrarAutorizacoesPorMedico(Usuario pacienteAtual) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do médico: ");
        String nomeMedico = scanner.nextLine();
        if (nomeMedico.isEmpty()) {
            System.out.println("Digite um nome válido.");
            return;
        }
        boolean encontrou = false;
        for (Autorizacao autorizacao : DataStore.getAutorizacoes()) {
            if (autorizacao.getPaciente().equals(pacienteAtual) &&
                    autorizacao.getMedicoSolicitante().getNome().toLowerCase().contains(nomeMedico.toLowerCase())) {
                System.out.println(autorizacao);
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Nenhuma autorização encontrada para esse médico.");
        }
    }
    private static void listarAutorizacoes(Usuario pacienteAtual) {
        System.out.println("\n=== MINHAS AUTORIZAÇÕES ===");

        List<Autorizacao> minhasAutorizacoes = new ArrayList<>();

        // Passa pela lista global do DataStore e filtra as do paciente
        for (Autorizacao aut : DataStore.getAutorizacoes()) {
            if (aut.getPaciente().equals(pacienteAtual)) {
                minhasAutorizacoes.add(aut);
            }
        }

        if (minhasAutorizacoes.isEmpty()) {
            System.out.println("Você não possui nenhuma autorização de exame.");
            return;
        }
            minhasAutorizacoes.sort(Comparator.comparing(Autorizacao::getDataCadastro));

            for (Autorizacao aut : minhasAutorizacoes) {
                String status = aut.isRealizado() ? "Realizado" : "Pendente";
                System.out.println("Código: " + aut.getCodigo() +
                        " | Data Solicitação: " + aut.getDataCadastro() +
                        " | Exame: " + aut.getTipoExame().getDescricao() +
                        " | Status: " + status);
            }
        }
    private static void marcarExameComoRealizado(Usuario pacienteAtual) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nDigite o código da autorização que deseja marcar como realizada: ");
        int codigoDigitado = scanner.nextInt();

        Autorizacao autorizacaoEncontrada = null;

        // Busca a autorização pelo código e verifica se é realmente deste paciente
        for (Autorizacao aut : DataStore.getAutorizacoes()) {
            if (aut.getCodigo() == codigoDigitado && aut.getPaciente().equals(pacienteAtual)) {
                autorizacaoEncontrada = aut;
                break;
            }
        }

        if (autorizacaoEncontrada == null) {
            System.out.println("Autorização não encontrada ou não pertence a você.");
            return;
        }

        if (autorizacaoEncontrada.isRealizado()) {
            System.out.println("Este exame já está marcado como realizado.");
            return;
        }
        System.out.println("Digite a data de realização do exame:");
        System.out.print("Ano (ex: 2026): ");
        int ano = scanner.nextInt();
        System.out.print("Mês (ex: 5): ");
        int mes = scanner.nextInt();
        System.out.print("Dia (ex: 15): ");
        int dia = scanner.nextInt();

        LocalDate dataInformada = LocalDate.of(ano, mes, dia);
        LocalDate dataSolicitacao = autorizacaoEncontrada.getDataCadastro();

        if (dataInformada.isBefore(dataSolicitacao)) {
            System.out.println("Erro: A data informada é anterior à solicitação (" + dataSolicitacao + ").");
            return;
        }
        long diasDiferenca = ChronoUnit.DAYS.between(dataSolicitacao, dataInformada);
        if (diasDiferenca > 30) {
            System.out.println("Erro: O prazo de 30 dias para realizar este exame já expirou.");
            return;
        }

        if (autorizacaoEncontrada.isCancelada()) {
            System.out.println("Erro: Esta autorização foi cancelada e não pode ser realizada.");
            System.out.println("Motivo do cancelamento: " + autorizacaoEncontrada.getMotivoCancelamento());
            return;
        }
        autorizacaoEncontrada.setDataRealizacao(dataInformada);

        System.out.println("Sucesso! Exame marcado como realizado.");
    }
}

