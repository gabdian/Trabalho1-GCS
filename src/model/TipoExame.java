package model;

public enum TipoExame {
    RAIO_X("Raio-X"),
    TOMOGRAFIA("Tomografia"),
    RESSONANCIA_MAGNETICA("Ressonância Magnética"),
    ULTRASSONOGRAFIA("Ultrassonografia"),
    HEMOGRAMA("Hemograma"),
    ELETROCARDIOGRAMA("Eletrocardiograma"),
    ENDOSCOPIA("Endoscopia"),
    COLONOSCOPIA("Colonoscopia"),
    MAMOGRAFIA("Mamografia"),
    DENSITOMETRIA("Densitometria Óssea"),
    GLICEMIA("Glicemia em Jejum"),
    COLESTEROL("Colesterol Total");

    private final String descricao;

    TipoExame(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
