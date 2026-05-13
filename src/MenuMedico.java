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
        
    }

    private static Medico selecionaMedico(Scanner scanner) {
        List<Medico> medicos = DataStore.getMedicos();
        
        int indice = getIndexSelecionado(scanner, medicos, "Médico");
        return medicos.get(indice);
    }

    private static Paciente selecionaPaciente(Scanner scanner) {
        List<Paciente> pacientes = DataStore.getPacientes();
        
        int indice = getIndexSelecionado(scanner, pacientes, "Paciente");
        return pacientes.get(indice);
    }
    
    private static <T> int getIndexSelecionado(Scanner scanner, List<T> lista, String nomeEntidade) {
        System.out.println("Indice | " + nomeEntidade);

        for (int i = 0; i < lista.size(); i++) {
            System.out.println("  " + (i + 1) + "    - " + lista.get(i));
        }

        System.out.print("Digite o indice do "+ nomeEntidade +": ");
        int opcao = scanner.nextInt();
        while (opcao < 1 ||
                opcao > lista.size()) {
            System.out.print("Digite um indice válido: ");
            opcao = scanner.nextInt();
        }

        return (opcao - 1);
    }
}