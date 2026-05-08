package model;

public class Medico extends Usuario {
    public Medico(int id, String nome, String iniciais) {
        super(id, nome, iniciais);
    }

    @Override
    public String getTipo() {
        return "Médico";
    }
}
