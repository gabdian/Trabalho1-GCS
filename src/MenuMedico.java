import java.util.Scanner;
import java.time.LocalDate;
import java.util.List;

import data.DataStore;
import model.Autorizacao;
import model.Medico;

public class MenuMedico {
    public static void novaAutorizacao() {
        // Imprime a tela inicial para fazer a Nova Autorizacao
        System.out.println("\n === NOVA AUTORIZAÇÃO ===\n");
        System.out.println("  " + "-".repeat(70));

        Scanner scanner = new Scanner(System.in);
        
        // 1) Pegar a data em que o cadastro da autorizacao esta sendo realizado
        LocalDate dataCadastro = LocalDate.now();

        System.out.println("Data de hoje: " + dataCadastro);
        // 2) Interagir entre todos os elementos que ja existem que sao medicos e printar na tela
        // para o usuário selecionar qual medico ele deseja. (apenas 1)

        // 3) Interagir entre todos os elementos que ja existem que sao pacientes e printar na tela
        // para o usuário selecionar qual pacientes ele deseja. (apenas 1)

        // 4) Interagir entre todos os elementos que ja existem que sao exame e printar na tela
        // para o usuário selecionar qual pacientes ele deseja. (apenas 1)


    }
}