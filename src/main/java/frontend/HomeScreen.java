package frontend;

import backend.DicasManager;
import utils.AudioManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomeScreen extends JFrame {
    private final DicasManager dicasManager;
    private JButton btnExibirDica;
    private JButton btnEditarDicas;
    private JLabel lblTitulo;

    public HomeScreen(DicasManager dicasManager) {
        this.dicasManager = dicasManager;
        initComponents();
    }

    private void initComponents() {
        setTitle("Home - Projeto Dicas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);
        
        // Painel principal com layout em grade
        JPanel pnlPrincipal = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Carrega o GIF como fundo
                ImageIcon gifIcon = new ImageIcon(getClass().getResource("/resources/images/img1.gif"));
                g.drawImage(gifIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        pnlPrincipal.setBorder(BorderFactory.createEmptyBorder(150, 150, 150, 150));
        
        AudioManager audioManager = new AudioManager();
        audioManager.playBackgroundMusic(); // Toca a música de fundo ao iniciar

        // Título
        lblTitulo = new JLabel("Bem-vindo ao Projeto Dicas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("serif", Font.BOLD, 20));
        lblTitulo.setForeground(Color.black); // Texto branco para contrastar com o fundo
        
        // Painel para botões
        JPanel pnlBotoes = new JPanel(new GridLayout(2, 1, 10, 10));
        pnlBotoes.setOpaque(false); // Painel transparente
        
        // Botão para exibir a dica do dia 
        btnExibirDica = new JButton("Exibir Dica do Dia");
        btnExibirDica.setFont(new Font("Arial", Font.PLAIN, 14));
        btnExibirDica.addActionListener(_ -> {
            audioManager.playButtonSound();
            String dica = dicasManager.getDicaAleatoria();
            JOptionPane.showMessageDialog(HomeScreen.this, 
                dica, 
                "Dica do Dia", 
                JOptionPane.INFORMATION_MESSAGE);
        });

        // Botão para abrir a tela de edição de dicas
        btnEditarDicas = new JButton("Editar Dicas");
        btnEditarDicas.setFont(new Font("Arial", Font.PLAIN, 14));
        btnEditarDicas.addActionListener(_ -> {
            EditarDicasScreen editarScreen = new EditarDicasScreen(dicasManager, HomeScreen.this);
            editarScreen.setVisible(true);
            audioManager.playButtonSound();
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