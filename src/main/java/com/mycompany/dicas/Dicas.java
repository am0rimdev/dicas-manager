/**
 * @author:
 * @description:
 */

package com.mycompany.dicas;

import backend.DicasManager;
import frontend.HomeScreen;
import javax.swing.SwingUtilities;

public class Dicas {
    public static void main(String[] args) {
        // Cria o objeto de back-end (a partir do arquivo "dicas.txt")
        DicasManager dicasManager = new DicasManager("dicas.txt");

        // Inicia a interface gráfica na Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new HomeScreen(dicasManager).setVisible(true);
        });
    }
}
