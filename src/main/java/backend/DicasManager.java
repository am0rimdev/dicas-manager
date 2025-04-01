/**
 * @author:Emily Júnia, Micael Pereira, Nífane Borges, Vinícius Alves Amorim
 * @description: Gerenciador de dicas que implementa operações CRUD
 */

package backend;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class DicasManager {
    private final String filename;
    private final ArrayList<String> frases;

    public DicasManager(String filename) {
        this.filename = filename;
        this.frases = new ArrayList<>();

        // Verifica se o arquivo existe; se não, cria um novo.
        File file = new File(this.filename);
        if (!file.exists()) {
            try {
                boolean fileCreated = file.createNewFile();
                if (!fileCreated)
                    System.out.println("O arquivo não pôde ser criado ou já existe.");
            } catch(IOException e) {
                System.out.println("Erro ao criar o arquivo: " + e.getMessage());
            }
        }

        carregarFrases(); // Carrega as frases do arquivo ao iniciar
    }

    private void carregarFrases() {
        frases.clear(); // Limpa a lista antes de carregar
        try (BufferedReader leitor = new BufferedReader(new FileReader(this.filename))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                this.frases.add(linha);
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
    
    private void salvarTodasFrases() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.filename, false))) {
            boolean primeiraLinha = true;
            for (String frase : frases) {
                if (!primeiraLinha) {
                    writer.newLine();
                }
                writer.write(frase);
                primeiraLinha = false;
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar as frases: " + e.getMessage());
        }
    }

    public String getDicaAleatoria() {
        if (this.frases.isEmpty()) {
            return "Nenhuma dica encontrada no arquivo.";
        }
        Random random = new Random();
        int indice = random.nextInt(frases.size());
        return frases.get(indice);
    }

    public void adicionarFrase(String frase) {
        if (frase == null || frase.trim().isEmpty()) {
            return;
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.filename, true))) {
            if (!frases.isEmpty()) {
                writer.newLine(); // Adiciona nova linha
            }
            writer.write(frase);
            this.frases.add(frase);
            System.out.println("Frase adicionada com sucesso!");
        } catch(IOException e) {
            System.out.println("Erro ao adicionar a frase: " + e.getMessage());
        }
    }

    // Método para buscar uma dica que contenha um texto informado
    public String buscarDica(String texto) {
        for (String dica : frases) {
            if (dica.toLowerCase().contains(texto.toLowerCase())) {
                return dica;
            }
        }
        return "Nenhuma dica encontrada com o texto: " + texto;
    }
    
    // Método para buscar o índice de uma dica
    public int buscarIndiceDica(String texto) {
        for (int i = 0; i < frases.size(); i++) {
            if (frases.get(i).toLowerCase().contains(texto.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }
    
    // Método para editar uma dica existente
    public boolean editarFrase(int indice, String novaFrase) {
        if (indice < 0 || indice >= frases.size() || novaFrase == null || novaFrase.trim().isEmpty()) {
            return false;
        }
        
        frases.set(indice, novaFrase);
        salvarTodasFrases();
        return true;
    }
    
    // Método para remover uma dica
    public boolean removerFrase(int indice) {
        if (indice < 0 || indice >= frases.size()) {
            return false;
        }
        
        frases.remove(indice);
        salvarTodasFrases();
        return true;
    }
    
    // Método para listar todas as dicas
    public ArrayList<String> listarTodasDicas() {
        return new ArrayList<>(frases);
    }
}