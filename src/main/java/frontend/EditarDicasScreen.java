/**
 * @author: Emily Júnia, Micael Pereira, Nífane Borges, Vinícius Alves Amorim
 * @description: Interface gráfica para gerenciamento de dicas, permitindo adicionar,
 *               editar, remover e pesquisar dicas no banco de dados.
 */

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
    private int idSelecionado = -1;

    /**
     * Construtor da tela de edição de dicas
     * @param dicasManager gerenciador de dicas para operações no banco de dados
     * @param parent janela pai para controle de visibilidade
     * @param audioManager gerenciador de áudio para efeitos sonoros
     */
    public EditarDicasScreen(DicasManager dicasManager, JFrame parent, AudioManager audioManager) {
        this.dicasManager = dicasManager;
        this.parent = parent;
        this.audioManager = audioManager;
        initComponents();
        carregarListaDicas();
    }

    /**
     * Inicializa e configura todos os componentes da interface gráfica
     */
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
    
    /**
     * Configura todos os eventos dos componentes da interface
     */
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
        btnAdicionarDica.addActionListener(_ -> {
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
        btnPesquisarDica.addActionListener(_ -> {
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
        btnEditarDica.addActionListener(_ -> {
            audioManager.playButtonSound();
            editarDicaSelecionada();
        });
        
        // Evento para remover dica selecionada
        btnRemoverDica.addActionListener(_ -> {
            audioManager.playButtonSound();
            removerDicaSelecionada();
        });
        
        // Evento para voltar à tela Home
        btnVoltar.addActionListener(_ -> {
            audioManager.playButtonSound();
            this.dispose();
            parent.setVisible(true);
        });
        
        // Evento para seleção na lista
        lstDicas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String dicaSelecionada = lstDicas.getSelectedValue();
                if (dicaSelecionada != null) {
                    idSelecionado = dicasManager.buscarIndiceDica(dicaSelecionada);
                } else {
                    idSelecionado = -1;
                }
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
    
    /**
     * Carrega todas as dicas do banco de dados na lista da interface
     */
    private void carregarListaDicas() {
        mdlDicas.clear();
        ArrayList<String> dicas = dicasManager.listarTodasDicas();
        for (String dica : dicas) {
            mdlDicas.addElement(dica);
        }
    }
    
    /**
     * Realiza a pesquisa de uma dica no banco de dados
     */
    private void pesquisarDica() {
        String texto = txtPesquisarDica.getText().trim();
        if (!texto.isEmpty()) {
            // Busca a dica no banco de dados
            int id = dicasManager.buscarIndiceDica(texto);
            if (id >= 0) {
                // Busca a dica na lista pelo texto
                String textoLower = texto.toLowerCase();
                for (int i = 0; i < mdlDicas.getSize(); i++) {
                    String dica = mdlDicas.getElementAt(i);
                    if (dica.toLowerCase().contains(textoLower)) {
                        lstDicas.setSelectedIndex(i);
                        lstDicas.ensureIndexIsVisible(i);
                        return;
                    }
                }
            }
            
            JOptionPane.showMessageDialog(this, 
                "Nenhuma dica encontrada com o texto: " + texto, 
                "Resultado da Pesquisa", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Digite um texto para pesquisa.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Exibe uma mensagem de diálogo para o usuário
     * @param mensagem O texto da mensagem a ser exibida
     * @param titulo O título da janela de diálogo
     * @param tipo O tipo de mensagem (ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, etc)
     */
    private void mostrarMensagem(String mensagem, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipo);
    }

    /**
     * Valida se há uma dica selecionada na lista
     * @return true se houver uma dica selecionada, false caso contrário
     */
    private boolean eSelecaoValida() {
        if (idSelecionado >= 0) return true;
        
        mostrarMensagem("Selecione uma dica para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        return false;
    }
    
    /**
     * Solicita ao usuário uma nova versão da dica através de um diálogo de entrada
     * @param dicaAtual O texto atual da dica que será editada
     * @return A nova versão da dica ou null se o usuário cancelar
     */
    private String solicitarNovaDica(String dicaAtual) {
        return JOptionPane.showInputDialog(this, "Editar dica:", dicaAtual);
    }

    /**
     * Gerencia o processo de edição de uma dica selecionada
     * Valida a seleção, solicita a nova versão e atualiza no banco de dados
     */
    private void editarDicaSelecionada() {
        if (!eSelecaoValida()) return;
        
        String dicaAtual = lstDicas.getSelectedValue();
        String novaDica = solicitarNovaDica(dicaAtual);
        
        if (novaDica == null || novaDica.trim().isEmpty()) return;
        
        boolean sucesso = dicasManager.editarFrase(idSelecionado, novaDica);
        if (!sucesso) {
            mostrarMensagem("Erro ao atualizar dica.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        carregarListaDicas();
        mostrarMensagem("Dica atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Valida se há uma dica selecionada para remoção
     * @return true se houver uma dica selecionada, false caso contrário
     */
    private boolean eSelecaoValidaParaRemocao() {
        if (idSelecionado >= 0) return true;
        
        mostrarMensagem("Selecione uma dica para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
        return false;
    }
    
    /**
     * Solicita confirmação do usuário para remover a dica
     * @return true se o usuário confirmar a remoção, false caso contrário
     */
    private boolean confirmarRemocao() {
        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja remover esta dica?",
            "Confirmar Remoção",
            JOptionPane.YES_NO_OPTION);
        return confirmacao == JOptionPane.YES_OPTION;
    }
    
    /**
     * Gerencia o processo de remoção de uma dica selecionada
     * Valida a seleção, solicita confirmação e executa a remoção
     */
    private void removerDicaSelecionada() {
        if (!eSelecaoValidaParaRemocao()) return;
        if (!confirmarRemocao()) return;
        
        boolean sucesso = dicasManager.removerFrase(idSelecionado);
        if (!sucesso) {
            mostrarMensagem("Erro ao remover dica.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        carregarListaDicas();
        mostrarMensagem("Dica removida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
}
