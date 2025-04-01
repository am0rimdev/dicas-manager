package frontend;

import backend.DicasManager;
import utils.AudioManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EditarDicasScreen extends JFrame {
    private final DicasManager dicasManager;
    private final JFrame parent;
    private final AudioManager audioManager;
    private JTextField txtNovaDica;
    private JButton btnAdicionarDica;
    private JTextField txtPesquisarDica;
    private JButton btnPesquisarDica;
    private JList<String> lstDicas;
    private DefaultListModel<String> mdlDicas;
    private JButton btnEditarDica;
    private JButton btnRemoverDica;
    private JButton btnVoltar;
    private int indiceSelecionado = -1;

    public EditarDicasScreen(DicasManager dicasManager, JFrame parent) {
        this.dicasManager = dicasManager;
        this.parent = parent;
        this.audioManager = new AudioManager();
        initComponents();
        carregarListaDicas();
    }

    private void initComponents() {
        setTitle("Editar Dicas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);

        JPanel pnlPrincipal = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon gifIcon = new ImageIcon(getClass().getResource("/resources/images/img2.gif"));
                g.drawImage(gifIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        pnlPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel norte para adicionar nova dica
        JPanel pnlNorte = new JPanel(new BorderLayout(5, 0));
        pnlNorte.setOpaque(false);
        
        JPanel pnlAdicionarDica = new JPanel(new BorderLayout(5, 0));
        pnlAdicionarDica.setOpaque(false);
        
        JLabel lblNovaDica = new JLabel("Nova Dica:");
        lblNovaDica.setForeground(Color.pink);
        txtNovaDica = new JTextField();
        btnAdicionarDica = new JButton("Adicionar");
        
        // Estilização dos botões
        btnAdicionarDica.setContentAreaFilled(false);
        btnAdicionarDica.setOpaque(true);
        btnAdicionarDica.setBackground(new Color(140, 29, 24));
        btnAdicionarDica.setForeground(Color.pink);
        btnAdicionarDica.setBorder(BorderFactory.createLineBorder(new Color(140, 29, 24)));
        
        pnlAdicionarDica.add(lblNovaDica, BorderLayout.WEST);
        pnlAdicionarDica.add(txtNovaDica, BorderLayout.CENTER);
        pnlAdicionarDica.add(btnAdicionarDica, BorderLayout.EAST);
        pnlNorte.add(pnlAdicionarDica, BorderLayout.NORTH);
        
        // Painel de pesquisa
        JPanel pnlPesquisar = new JPanel(new BorderLayout(5, 0));
        pnlPesquisar.setOpaque(false);
        
        JLabel lblPesquisar = new JLabel("Pesquisar:");
        lblPesquisar.setForeground(Color.pink);
        txtPesquisarDica = new JTextField();
        btnPesquisarDica = new JButton("Pesquisar");
        
        // Estilização do botão pesquisar
        btnPesquisarDica.setContentAreaFilled(false);
        btnPesquisarDica.setOpaque(true);
        btnPesquisarDica.setBackground(new Color(140, 29, 24));
        btnPesquisarDica.setForeground(Color.pink);
        btnPesquisarDica.setBorder(BorderFactory.createLineBorder(new Color(140, 29, 24)));
        
        pnlPesquisar.add(lblPesquisar, BorderLayout.WEST);
        pnlPesquisar.add(txtPesquisarDica, BorderLayout.CENTER);
        pnlPesquisar.add(btnPesquisarDica, BorderLayout.EAST);
        
        JPanel pnlBotoes = new JPanel(new GridLayout(1, 2, 5, 0));
        pnlBotoes.setOpaque(false);
        
        pnlNorte.add(pnlPesquisar, BorderLayout.CENTER);
        pnlNorte.add(pnlBotoes, BorderLayout.SOUTH);
        
        // Painel central com lista de dicas
        JPanel pnlCentral = new JPanel(new BorderLayout());
        pnlCentral.setOpaque(false);
        
        mdlDicas = new DefaultListModel<>();
        lstDicas = new JList<>(mdlDicas) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                Dimension size = super.getPreferredScrollableViewportSize();
                size.height = getModel().getSize() * 45; 
                return size;
            }
        };
        
        lstDicas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstDicas.setOpaque(false);
        lstDicas.setForeground(Color.pink);
        lstDicas.setFixedCellHeight(45);
        lstDicas.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                boolean isSelected, boolean cellHasFocus) {
                
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                setOpaque(true);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 10, 5, 10),
                    BorderFactory.createLineBorder(new Color(140, 29, 24, 50), 1, true)
                ));
                
                if (isSelected) {
                    setBackground(new Color(241, 173, 189, 180));
                    setForeground(Color.white);
                } else {
                    setBackground(new Color(241, 173, 189, 100)); 
                    setForeground(Color.white);
                }
                return this;
            }
        });

        JScrollPane scrDicas = new JScrollPane(lstDicas);
        scrDicas.setOpaque(false);
        scrDicas.getViewport().setOpaque(false);
        scrDicas.setBorder(BorderFactory.createTitledBorder("Lista de Dicas"));
        ((javax.swing.border.TitledBorder)scrDicas.getBorder()).setTitleColor(Color.pink);
        
        pnlCentral.add(scrDicas, BorderLayout.CENTER);
        
        // Painel sul para botões de ação
        JPanel pnlSul = new JPanel(new GridLayout(1, 3, 5, 0));
        pnlSul.setOpaque(false);
        
        btnEditarDica = new JButton("Editar Selecionada");
        btnRemoverDica = new JButton("Remover Selecionada");
        btnVoltar = new JButton("Voltar");
        
        // Estilização dos botões
        for (JButton btn : new JButton[]{btnEditarDica, btnRemoverDica, btnVoltar}) {
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            btn.setBackground(new Color(140, 29, 24));
            btn.setForeground(Color.pink);
            btn.setBorder(BorderFactory.createLineBorder(new Color(140, 29, 24)));
        }
        
        pnlSul.add(btnEditarDica);
        pnlSul.add(btnRemoverDica);
        pnlSul.add(btnVoltar);
        
        // Adicionar os painéis ao painel principal
        pnlPrincipal.add(pnlNorte, BorderLayout.NORTH);
        pnlPrincipal.add(pnlCentral, BorderLayout.CENTER);
        pnlPrincipal.add(pnlSul, BorderLayout.SOUTH);
        
        add(pnlPrincipal);
        
        // Configurar eventos
        configurarEventos();
    }
    
    private void configurarEventos() {
        // Evento ao fechar a janela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parent.setVisible(true);
                dispose();
            }
        });
        
        // Evento para adicionar dica
        btnAdicionarDica.addActionListener((ActionEvent e) -> {
            audioManager.playButtonSound();
            String novaDica = txtNovaDica.getText().trim();
            if (!novaDica.isEmpty()) {
                dicasManager.adicionarFrase(novaDica);
                carregarListaDicas();
                txtNovaDica.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, digite uma dica.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Evento para pesquisar dica
        btnPesquisarDica.addActionListener((ActionEvent e) -> {
            audioManager.playButtonSound();
            pesquisarDica();
        });
        
        // Evento para a tecla Enter no campo de pesquisa
        txtPesquisarDica.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    pesquisarDica();
                }
            }
        });
        
        // Evento para editar dica selecionada
        btnEditarDica.addActionListener((ActionEvent e) -> {
            audioManager.playButtonSound();
            editarDicaSelecionada();
        });
        
        // Evento para remover dica selecionada
        btnRemoverDica.addActionListener((ActionEvent e) -> {
            audioManager.playButtonSound();
            removerDicaSelecionada();
        });
        
        // Evento para voltar à tela Home
        btnVoltar.addActionListener((ActionEvent e) -> {
            audioManager.playButtonSound();
            this.dispose();
            parent.setVisible(true);
        });
        
        // Evento para seleção na lista
        lstDicas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                indiceSelecionado = lstDicas.getSelectedIndex();
            }
        });
        
        // Evento de duplo clique para editar
        lstDicas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarDicaSelecionada();
                }
            }
        });
    }
    
    private void carregarListaDicas() {
        mdlDicas.clear();
        ArrayList<String> dicas = dicasManager.listarTodasDicas();
        for (String dica : dicas) {
            mdlDicas.addElement(dica);
        }
    }
    
    private void pesquisarDica() {
        String texto = txtPesquisarDica.getText().trim();
        if (!texto.isEmpty()) {
            int indice = dicasManager.buscarIndiceDica(texto);
            if (indice >= 0) {
                lstDicas.setSelectedIndex(indice);
                lstDicas.ensureIndexIsVisible(indice);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Nenhuma dica encontrada com o texto: " + texto, 
                    "Resultado da Pesquisa", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Digite um texto para pesquisa.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void editarDicaSelecionada() {
        if (indiceSelecionado < 0) {
            JOptionPane.showMessageDialog(this, 
                "Selecione uma dica para editar.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String dicaAtual = mdlDicas.getElementAt(indiceSelecionado);
        String novaDica = JOptionPane.showInputDialog(this, 
            "Editar dica:", 
            dicaAtual);
            
        if (novaDica != null && !novaDica.trim().isEmpty()) {
            boolean sucesso = dicasManager.editarFrase(indiceSelecionado, novaDica);
            if (sucesso) {
                carregarListaDicas();
                JOptionPane.showMessageDialog(this, 
                    "Dica atualizada com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao atualizar dica.", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void removerDicaSelecionada() {
        if (indiceSelecionado < 0) {
            JOptionPane.showMessageDialog(this, 
                "Selecione uma dica para remover.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja remover esta dica?", 
            "Confirmar Remoção", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirmacao == JOptionPane.YES_OPTION) {
            boolean sucesso = dicasManager.removerFrase(indiceSelecionado);
            if (sucesso) {
                carregarListaDicas();
                JOptionPane.showMessageDialog(this, 
                    "Dica removida com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao remover dica.", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}