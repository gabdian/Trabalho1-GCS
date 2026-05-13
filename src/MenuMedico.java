import java.util.Scanner;
import java.time.LocalDate;
import java.util.List;
import java.util.Arrays;

import data.DataStore;
import model.Autorizacao;
import model.Medico;
import model.Paciente;
import model.TipoExame;

public class MenuMedico {
    public static void novaAutorizacao() {
        System.out.println("\n ====== NOVA AUTORIZAÇÃO ======\n");

        Scanner scanner = new Scanner(System.in);

        LocalDate dataCadastro = LocalDate.now();
        Medico medicoSelecionado = selecionaMedico(scanner);

        Paciente pacienteSelecionado = selecionaPaciente(scanner);

        TipoExame exameSelecionado = selecionaExame(scanner);

        if (medicoSelecionado != null && pacienteSelecionado != null && exameSelecionado != null) {
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

    private static Medico selecionaMedico(Scanner scanner) {
        List<Medico> medicos = DataStore.getMedicos();

        int indice = getIndexSelecionado(scanner, medicos, "Médico");
        if (indice != -1) {
            return medicos.get(indice);
        } else {
            return null;
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
        int opcao = scanner.nextInt();
        while (opcao < 1 ||
                opcao > tamanhoLista) {
            System.out.print("Digite um indice válido: ");
            opcao = scanner.nextInt();
        }

        return (opcao - 1);
    }
}