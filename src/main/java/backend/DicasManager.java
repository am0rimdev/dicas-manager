/**
 * @author:Emily Júnia, Micael Pereira, Nífane Borges, Vinícius Alves Amorim
 * @description: Gerenciador de dicas que implementa operações CRUD utilizando SQLite.
 *               Este gerenciador permite adicionar, editar, remover e listar dicas, 
 *               além de buscar uma dica aleatória.
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
    
    /**
     * Construtor da classe DicasManager
     * @param dbUrl URL de conexão com o banco de dados SQLite
     */
    public DicasManager(String dbUrl) {
        this.dbUrl = dbUrl;
        criarTabelaSeNaoExistir();
    }

    /**
     * Estabelece conexão com o banco de dados SQLite
     * @return Connection objeto de conexão com o banco
     * @throws SQLException se houver erro na conexão
     */
    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(this.dbUrl);
    }

    /**
     * Cria a tabela 'dicas' se ela não existir no banco de dados
     * A tabela possui as colunas: id (INTEGER PRIMARY KEY) e frase (TEXT)
     */
    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS dicas (id INTEGER PRIMARY KEY AUTOINCREMENT, frase TEXT NOT NULL)";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    /**
     * Retorna uma dica aleatória do banco de dados
     * @return String contendo a dica aleatória ou mensagem de erro se não houver dicas
     */
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

    /**
     * Adiciona uma nova dica ao banco de dados
     * @param frase texto da dica a ser adicionada
     */
    public void adicionarFrase(String frase) {
        if (frase == null || frase.trim().isEmpty()) return;
        String sql = "INSERT INTO dicas(frase) VALUES(?)";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, frase);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar a frase: " + e.getMessage());
        }
    }

    /**
     * Busca o ID de uma dica no banco de dados
     * @param texto texto a ser pesquisado na dica
     * @return int ID da dica encontrada ou -1 se não encontrar
     */
    public int buscarIndiceDica(String texto) {
        if (texto == null || texto.trim().isEmpty()) return -1;
        
        String sql = "SELECT id FROM dicas WHERE LOWER(frase) LIKE LOWER(?)";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + texto + "%");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar índice da dica: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Atualiza o texto de uma dica existente no banco de dados
     * @param id ID da dica a ser editada
     * @param novaFrase novo texto da dica
     * @return boolean true se a atualização foi bem sucedida, false caso contrário
     */
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

    /**
     * Remove uma dica do banco de dados
     * @param id ID da dica a ser removida
     * @return boolean true se a remoção foi bem sucedida, false caso contrário
     */
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

    /**
     * Lista todas as dicas cadastradas no banco de dados
     * @return ArrayList<String> lista contendo todas as dicas
     */
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
