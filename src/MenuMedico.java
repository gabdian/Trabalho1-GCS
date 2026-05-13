import java.util.Scanner;
import java.time.LocalDate;
import java.util.List;
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

    // Pra teste
    public static void main(String[] args) {
        List<Medico> medicos = DataStore.getMedicos();
        Medico medicoAtual = medicos.get(0);
        listarAutorizacoes(medicoAtual);
    }

    public static void novaAutorizacao() {
        // Imprime a tela inicial para fazer a Nova Autorizacao
        System.out.println("\n ====== NOVA AUTORIZAÇÃO ======\n");

        Scanner scanner = new Scanner(System.in);
        
        // 1) Pegar a data em que o cadastro da autorizacao esta sendo realizado
        LocalDate dataCadastro = LocalDate.now();
        
        // 2) Interagir entre todos os elementos que ja existem que sao medicos e printar na tela
        // para o usuário selecionar qual medico ele deseja. (apenas 1)
        Medico medicoSelecionado = selecionaMedico(scanner);

        // 3) Interagir entre todos os elementos que ja existem que sao pacientes e printar na tela
        // para o usuário selecionar qual pacientes ele deseja. (apenas 1)
        Paciente pacienteSelecionado = selecionaPaciente(scanner);

        // 4) Interagir entre todos os elementos que ja existem que sao exame e printar na tela
        // para o usuário selecionar qual pacientes ele deseja. (apenas 1)
        TipoExame exameSelecionado = selecionaExame(scanner);

        // 5) Salvar a autorizacao na lista de autorizacoes
        if (medicoSelecionado != null || pacienteSelecionado != null || exameSelecionado != null){
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

    public static void listarAutorizacoes(Medico medicoAtual) {
        // Imprime a tela inicial para fazer a Nova Autorizacao
        System.out.println("\n ====== Listar Autorizações ======\n");

        Scanner scanner = new Scanner(System.in);
        // 1) Interagir entre todos os elementos que ja existem que sao pacientes e printar na tela
        // para o usuário selecionar qual pacientes ele deseja. (apenas 1)
        //Paciente pacienteSelecionado = selecionaPaciente(scanner);
        //listarAutorizacoesPaciente(pacienteSelecionado);

        TipoExame exameSelecionado = selecionaExame(scanner);
        listarAutorizacoesExame(exameSelecionado);


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


    private static Medico selecionaMedico(Scanner scanner) {
        List<Medico> medicos = DataStore.getMedicos();
        
        int indice = getIndexSelecionado(scanner, medicos, "Médico");
        if (indice != -1){
            return medicos.get(indice);
        } else {
            return null;
        }
    }

    private static Paciente selecionaPaciente(Scanner scanner) {
        List<Paciente> pacientes = DataStore.getPacientes();
        
        int indice = getIndexSelecionado(scanner, pacientes, "Paciente");
        if (indice != -1){
            return pacientes.get(indice);
        } else {
            return null;
        }
    }

    private static TipoExame selecionaExame(Scanner scanner) {
        TipoExame[] exames = TipoExame.values();

        int indice = getIndexSelecionado(scanner, Arrays.asList(exames), "Exame");
        if (indice != -1){
            return exames[indice];
        } else {
            return null;
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

        System.out.print("Digite o indice do "+ nomeEntidade +" desejados: ");
        int opcao = scanner.nextInt();
        while (opcao < 1 ||
                opcao > tamanhoLista) {
            System.out.print("Digite um indice válido: ");
            opcao = scanner.nextInt();
        }

        return (opcao - 1);
    }
}