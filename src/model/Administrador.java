package model;

public class Administrador extends Usuario {
    public Administrador(int id, String nome, String iniciais) {
        super(id, nome, iniciais);
    }

    @Override
    public String getTipo() {
        return "Administrador";
    }

}
