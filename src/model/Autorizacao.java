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
    private LocalDate dataCancelamento; // null se não foi cancelada
    private String motivoCancelamento; // null se não foi cancelada

    public Autorizacao(LocalDate dataCadastro, Medico medicoSolicitante,
            Paciente paciente, TipoExame tipoExame) {
        this.codigo = contadorCodigo++;
        this.dataCadastro = dataCadastro;
        this.medicoSolicitante = medicoSolicitante;
        this.paciente = paciente;
        this.tipoExame = tipoExame;
        this.dataRealizacao = null;
        this.dataCancelamento = null;
        this.motivoCancelamento = null;
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

    public boolean isCancelada() {
        return dataCancelamento != null;
    }

    public LocalDate getDataCancelamento() {
        return dataCancelamento;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    // --- Lógica de cancelamento ---

    /**
     * Cancela a autorização.
     * Só é possível cancelar uma autorização que ainda esteja pendente (não realizada e não cancelada).
     *
     * @param motivo o motivo do cancelamento (pode ser null)
     * @return true se cancelou com sucesso, false se não pôde cancelar
     */
    public boolean cancelar(String motivo) {
        // Validações: não pode estar realizada ou já cancelada
        if (isRealizado() || isCancelada()) {
            return false;
        }
        this.dataCancelamento = LocalDate.now();
        this.motivoCancelamento = motivo;
        return true;
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
        String status;
        if (isCancelada()) {
            status = "Cancelada em " + dataCancelamento.format(FORMATO_DATA);
        } else if (isRealizado()) {
            status = "Realizado em " + dataRealizacao.format(FORMATO_DATA);
        } else {
            status = "Pendente";
        }
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
