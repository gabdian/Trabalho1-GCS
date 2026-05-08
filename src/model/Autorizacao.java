package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Autorizacao {
    private static int contadorCodigo = 1000;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private int codigo;
    private LocalDate dataCadastro;
    private Medico medicoSolicitante;
    private Paciente paciente;
    private TipoExame tipoExame;
    private LocalDate dataRealizacao; // null se ainda não realizado

    public Autorizacao(LocalDate dataCadastro, Medico medicoSolicitante,
            Paciente paciente, TipoExame tipoExame) {
        this.codigo = contadorCodigo++;
        this.dataCadastro = dataCadastro;
        this.medicoSolicitante = medicoSolicitante;
        this.paciente = paciente;
        this.tipoExame = tipoExame;
        this.dataRealizacao = null;
    }

    public int getCodigo() {
        return codigo;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public Medico getMedicoSolicitante() {
        return medicoSolicitante;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public TipoExame getTipoExame() {
        return tipoExame;
    }

    public LocalDate getDataRealizacao() {
        return dataRealizacao;
    }

    public boolean isRealizado() {
        return dataRealizacao != null;
    }

    // --- Lógica de marcar como realizado ---

    /**
     * Marca o exame como realizado na data informada.
     *
     * @param data a data em que o exame foi realizado
     * @return true se marcou com sucesso, false se a data é inválida
     */
    public boolean marcarComoRealizado(LocalDate data) {
        if (data.isBefore(dataCadastro)) {
            return false;
        }
        if (data.isAfter(dataCadastro.plusDays(30))) {
            return false;
        }
        this.dataRealizacao = data;
        return true;
    }

    public static void setContadorCodigo(int valor) {
        contadorCodigo = valor;
    }

    public static int getContadorCodigo() {
        return contadorCodigo;
    }

    @Override
    public String toString() {
        String status = isRealizado()
                ? "Realizado em " + dataRealizacao.format(FORMATO_DATA)
                : "Pendente";
        return String.format(
                "Autorizacao #%d | %s | %s | Medico: %s | Paciente: %s | [%s]",
                codigo,
                dataCadastro.format(FORMATO_DATA),
                tipoExame.getDescricao(),
                medicoSolicitante.getNome(),
                paciente.getNome(),
                status);
    }
}