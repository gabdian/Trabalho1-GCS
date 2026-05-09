import java.util.Scanner;
import model.Usuario;

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
                    //listarAutorizacoes(pacienteAtual);
                    break;
                case 2:
                    //marcarExameComoRealizado(pacienteAtual);
                    break;
                case 3:
                    //filtrarAutorizacoesPorMedico(pacienteAtual);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void filtrarAutorizacoesPorMedico(Usuario pacienteAtual) {;
    }
}
