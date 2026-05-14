import java.util.Scanner;
import java.util.List;
import data.DataStore;
import model.Usuario;
import model.Medico;
import model.Paciente;
import model.Administrador;

public class Main {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            Usuario atual = DataStore.getUsuarioAtual();

            System.out.println("\n==========================================");
            System.out.println("  SISTEMA DE AUTORIZAÇÃO DE EXAMES MÉDICOS");
            System.out.println("==========================================");
            System.out.println(" Usuário Atual: " + (atual != null ? atual.toString() : "Nenhum"));
            System.out.println("------------------------------------------");
            System.out.println("1 - Trocar Usuário");
            System.out.println("2 - Acessar Menu do Perfil (" + (atual != null ? atual.getTipo() : "Logue primeiro") + ")");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Erro: Digite um número válido.");
                continue;
            }

            switch (opcao) {
                case 1:
                    trocarUsuario(teclado);
                    break;
                case 2:
                    if (atual == null) {
                        System.out.println("Erro: Você precisa selecionar um usuário antes de acessar as funcionalidades.");
                    } else {
                        direcionarParaMenu(teclado, atual);
                    }
                    break;
                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void trocarUsuario(Scanner teclado) {
        List<Usuario> usuarios = DataStore.getTodosUsuarios();
        System.out.println("\n--- Selecione o Usuário ---");
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println((i + 1) + " - " + usuarios.get(i).toString());
        }
        System.out.print("Escolha o número do usuário: ");

        try {
            int escolha = Integer.parseInt(teclado.nextLine().trim());
            if (escolha > 0 && escolha <= usuarios.size()) {
                Usuario selecionado = usuarios.get(escolha - 1);
                DataStore.setUsuarioAtual(selecionado);
                System.out.println("Usuário alterado para: " + selecionado.getNome());
            } else {
                System.out.println("Opção inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Entrada inválida.");
        }
    }

    private static void direcionarParaMenu(Scanner teclado, Usuario usuario) {
        if (usuario instanceof Medico) {
            MenuMedico.exibirMenu(teclado, (Medico) usuario);
        } else if (usuario instanceof Paciente) {
            MenuPaciente.exibirMenu(usuario);
        } else if (usuario instanceof Administrador) {
            exibirMenuAdmin(teclado);
        }
    }

    private static void exibirMenuAdmin(Scanner teclado) {
        int opcao = -1;
        do {
            System.out.println("\n=== MENU DO ADMINISTRADOR ===");
            System.out.println("1 - Estatísticas Gerais");
            System.out.println("2 - Estatísticas por Tipo de Exame");
            System.out.println("3 - Listar Exames Pendentes");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            try {
                opcao = Integer.parseInt(teclado.nextLine().trim());
                switch (opcao) {
                    case 1: MenuAdmin.estatisticasGerais(); break;
                    case 2: MenuAdmin.estatisticasPorExame(); break;
                    case 3: MenuAdmin.listarExamesPendentes(); break;
                    case 0: break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        } while (opcao != 0);
    }
}
