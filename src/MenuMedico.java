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
        System.out.println("\n === NOVA AUTORIZAÇÃO ===\n");

        Scanner scanner = new Scanner(System.in);
        
        // 1) Pegar a data em que o cadastro da autorizacao esta sendo realizado
        LocalDate dataCadastro = LocalDate.now();
        
        System.out.println("Data da Autorização: " + dataCadastro);
        // 2) Interagir entre todos os elementos que ja existem que sao medicos e printar na tela
        // para o usuário selecionar qual medico ele deseja. (apenas 1)
        Medico medicoSelecionado = selecionaMedico(scanner);

        // 3) Interagir entre todos os elementos que ja existem que sao pacientes e printar na tela
        // para o usuário selecionar qual pacientes ele deseja. (apenas 1)
        List<Paciente> pacientes = DataStore.getPacientes();
        System.out.println("Pacientes: "+ pacientes);

        // 4) Interagir entre todos os elementos que ja existem que sao exame e printar na tela
        // para o usuário selecionar qual pacientes ele deseja. (apenas 1)
        TipoExame[] exames = TipoExame.values();
        System.out.println("Exames: " + Arrays.toString(exames));

    }

    public static Medico selecionaMedico(Scanner scanner) {
        List<Medico> medicos = DataStore.getMedicos();
        
        System.out.println("Indice | Médico");

        for (int i = 0; i < medicos.size(); i++) {
            System.out.println("  " + (i + 1) + "    - " + medicos.get(i));
        }

        System.out.print("Digite o indice do Médico: ");
        int opcaoMedico = scanner.nextInt();
        while (opcaoMedico < 1 ||
                opcaoMedico >= medicos.size()) {
            System.out.print("Digite um indice válido: ");
            opcaoMedico = scanner.nextInt();
        }
        Medico medicoSelecionado = medicos.get((opcaoMedico-1));
        return medicoSelecionado;
    }
}