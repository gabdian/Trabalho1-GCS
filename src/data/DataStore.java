package data;

import model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataStore {

    private static List<Medico> medicos = new ArrayList<>();
    private static List<Paciente> pacientes = new ArrayList<>();
    private static List<Administrador> administradores = new ArrayList<>();
    private static List<Autorizacao> autorizacoes = new ArrayList<>();

    private static Usuario usuarioAtual = null;

    private static int proximoId = 100;

    // Bloco estático: executa uma vez quando a classe é carregada
    static {
        carregarDadosIniciais();
    }

    private static void carregarDadosIniciais() {
        // === MÉDICOS ===
        Medico m1 = new Medico(1, "Dr. Carlos Silva", "CS");
        Medico m2 = new Medico(2, "Dra. Ana Oliveira", "AO");
        Medico m3 = new Medico(3, "Dr. Roberto Lima", "RL");
        medicos.add(m1);
        medicos.add(m2);
        medicos.add(m3);

        // === PACIENTES ===
        Paciente p1 = new Paciente(10, "Maria Santos", "MS");
        Paciente p2 = new Paciente(11, "João Pereira", "JP");
        Paciente p3 = new Paciente(12, "Fernanda Costa", "FC");
        Paciente p4 = new Paciente(13, "Pedro Almeida", "PA");
        Paciente p5 = new Paciente(14, "Luciana Ramos", "LR");
        pacientes.add(p1);
        pacientes.add(p2);
        pacientes.add(p3);
        pacientes.add(p4);
        pacientes.add(p5);

        // === ADMINISTRADORES ===
        Administrador a1 = new Administrador(50, "Admin Geral", "AG");
        administradores.add(a1);

        // === AUTORIZAÇÕES DE EXAME ===
        LocalDate hoje = LocalDate.now();

        autorizacoes.add(new Autorizacao(hoje.minusDays(20), m1, p1,
                TipoExame.RAIO_X));
        autorizacoes.add(new Autorizacao(hoje.minusDays(15), m1, p2,
                TipoExame.HEMOGRAMA));
        autorizacoes.add(new Autorizacao(hoje.minusDays(10), m2, p1,
                TipoExame.TOMOGRAFIA));
        autorizacoes.add(new Autorizacao(hoje.minusDays(8), m2, p3,
                TipoExame.ULTRASSONOGRAFIA));
        autorizacoes.add(new Autorizacao(hoje.minusDays(5), m3, p4,
                TipoExame.ELETROCARDIOGRAMA));
        autorizacoes.add(new Autorizacao(hoje.minusDays(3), m1, p5,
                TipoExame.ENDOSCOPIA));
        autorizacoes.add(new Autorizacao(hoje.minusDays(2), m3, p2,
                TipoExame.MAMOGRAFIA));
        autorizacoes.add(new Autorizacao(hoje.minusDays(1), m2, p4,
                TipoExame.COLONOSCOPIA));

        // Marcar algumas como já realizadas (para testar estatísticas)
        autorizacoes.get(0).marcarComoRealizado(hoje.minusDays(18));
        autorizacoes.get(1).marcarComoRealizado(hoje.minusDays(13));
        autorizacoes.get(3).marcarComoRealizado(hoje.minusDays(6));

        usuarioAtual = a1;
    }

    public static List<Medico> getMedicos() {
        return medicos;
    }

    public static List<Paciente> getPacientes() {
        return pacientes;
    }

    public static List<Administrador> getAdministradores() {
        return administradores;
    }

    public static List<Autorizacao> getAutorizacoes() {
        return autorizacoes;
    }

    public static Usuario getUsuarioAtual() {
        return usuarioAtual;
    }

    public static void setUsuarioAtual(Usuario usuario) {
        usuarioAtual = usuario;
    }

    public static List<Usuario> getTodosUsuarios() {
        List<Usuario> todos = new ArrayList<>();
        todos.addAll(medicos);
        todos.addAll(pacientes);
        todos.addAll(administradores);
        return todos;
    }

    /**
     * Gera o próximo ID disponível para um novo usuário.
     */
    public static int gerarProximoId() {
        return proximoId++;
    }

    /**
     * Adiciona uma nova autorização ao sistema.
     */
    public static void adicionarAutorizacao(Autorizacao autorizacao) {
        autorizacoes.add(autorizacao);
    }

    public static void adicionarMedico(Medico medico) {
        medicos.add(medico);
    }

    public static void adicionarPaciente(Paciente paciente) {
        pacientes.add(paciente);
    }

    public static void adicionarAdministrador(Administrador administrador) {
        administradores.add(administrador);
    }
}