/**
 * @author:
 * @description: Tela principal do aplicativo de dicas
 */

package frontend;

import backend.DicasManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomeScreen extends JFrame {
    private final DicasManager dicasManager;
    private JButton btnExibirDica; // Prefixo btn para botões
    private JButton btnEditarDicas;
    private JLabel lblTitulo;

    public HomeScreen(DicasManager dicasManager) {
        this.dicasManager = dicasManager;
        initComponents();
    }

    private void initComponents() {
        setTitle("Home - Projeto Dicas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        
        // Painel principal com layout em grade
        JPanel pnlPrincipal = new JPanel(new BorderLayout(10, 10));
        pnlPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        lblTitulo = new JLabel("Bem-vindo ao Projeto Dicas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Painel para botões
        JPanel pnlBotoes = new JPanel(new GridLayout(2, 1, 10, 10));
        
        // Botão para exibir a dica do dia
        btnExibirDica = new JButton("Exibir Dica do Dia");
        btnExibirDica.setFont(new Font("Arial", Font.PLAIN, 14));
        btnExibirDica.addActionListener((ActionEvent e) -> {
            String dica = dicasManager.getDicaAleatoria();
            JOptionPane.showMessageDialog(HomeScreen.this, 
                dica, 
                "Dica do Dia", 
                JOptionPane.INFORMATION_MESSAGE);
        });

        // Botão para abrir a tela de edição de dicas
        btnEditarDicas = new JButton("Editar Dicas");
        btnEditarDicas.setFont(new Font("Arial", Font.PLAIN, 14));
        btnEditarDicas.addActionListener((ActionEvent e) -> {
            // Abre a tela de edição e esconde a tela principal
            EditarDicasScreen editarScreen = new EditarDicasScreen(dicasManager, HomeScreen.this);
            editarScreen.setVisible(true);
            HomeScreen.this.setVisible(false);
        });

        pnlBotoes.add(btnExibirDica);
        pnlBotoes.add(btnEditarDicas);
        
        // Adicionar componentes ao painel principal
        pnlPrincipal.add(lblTitulo, BorderLayout.NORTH);
        pnlPrincipal.add(pnlBotoes, BorderLayout.CENTER);

        add(pnlPrincipal);
    }
}