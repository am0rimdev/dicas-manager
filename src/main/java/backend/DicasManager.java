/**
 * @author:Emily Júnia, Micael Pereira, Nífane Borges, Vinícius Alves Amorim
 * @description: Gerenciador de dicas que implementa operações CRUD utilizando SQLite.
 * Este gerenciador permite adicionar, editar, remover e listar dicas, além de buscar uma dica aleatória.
 */

package backend;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DicasManager {
    private final String dbUrl;
    
    public DicasManager(String dbUrl) {
        this.dbUrl = dbUrl;
        criarTabelaSeNaoExistir();
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS dicas (id INTEGER PRIMARY KEY AUTOINCREMENT, frase TEXT NOT NULL)";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    public String getDicaAleatoria() {
        String sql = "SELECT frase FROM dicas ORDER BY RANDOM() LIMIT 1";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("frase");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar dica aleatória: " + e.getMessage());
        }
        return "Nenhuma dica encontrada no banco de dados.";
    }

    public void adicionarFrase(String frase) {
        if (frase == null || frase.trim().isEmpty()) return;
        String sql = "INSERT INTO dicas(frase) VALUES(?)";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, frase);
            pstmt.executeUpdate();
            System.out.println("Frase adicionada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar a frase: " + e.getMessage());
        }
    }

    public int buscarIndiceDica(String texto) {
        if (texto == null || texto.trim().isEmpty()) return -1;
        ArrayList<String> dicas = listarTodasDicas();
        String textoLower = texto.toLowerCase();
        for (int i = 0; i < dicas.size(); i++) {
            if (dicas.get(i).toLowerCase().contains(textoLower)) {
                return i;
            }
        }
        return -1;
    }

    public boolean editarFrase(int id, String novaFrase) {
        if (id < 0 || novaFrase == null || novaFrase.trim().isEmpty()) return false;
        String sql = "UPDATE dicas SET frase = ? WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, novaFrase);
            pstmt.setInt(2, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao editar frase: " + e.getMessage());
        }
        return false;
    }

    public boolean removerFrase(int id) {
        if (id < 0) return false;
        String sql = "DELETE FROM dicas WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao remover frase: " + e.getMessage());
        }
        return false;
    }

    public ArrayList<String> listarTodasDicas() {
        ArrayList<String> dicas = new ArrayList<>();
        String sql = "SELECT frase FROM dicas";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                dicas.add(rs.getString("frase"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar dicas: " + e.getMessage());
        }
        return dicas;
    }
}