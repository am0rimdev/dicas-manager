/**
 * @author:Emily Júnia, Micael Pereira, Nífane Borges, Vinícius Alves Amorim
 * @description: Cria o objeto de back-end (a partir do arquivo "dicas.db") 
 * e inicia a interface gráfica na Event Dispatch Thread
 */

package com.mycompany.dicas;

import backend.DicasManager;
import frontend.HomeScreen;

import javax.swing.SwingUtilities;

public class Dicas {
    public static void main(String[] args) {
        // Caminho do banco SQLite (arquivo local)
        String dbUrl = "jdbc:sqlite:src/main/java/data/dicas.db";
        DicasManager dicasManager = new DicasManager(dbUrl);

        SwingUtilities.invokeLater(() -> {
            new HomeScreen(dicasManager).setVisible(true);
        });
    }
}
