package ads.pacote.abb;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SimuladorABB extends JFrame {
    ArvoreBinariaBusca arvore = new ArvoreBinariaBusca();
    JTextField input = new JTextField(5);
    JButton inserir = new JButton("Inserir");
    JButton excluir = new JButton("Excluir");
    JButton buscar = new JButton("Buscar");
    JButton exportar = new JButton("Exportar Imagem");
    JButton emOrdem = new JButton("Em Ordem");
    JButton preOrdem = new JButton("Pré-Ordem");
    JButton posOrdem = new JButton("Pós-Ordem");
    JButton bfs = new JButton("BFS");
    JButton dfs = new JButton("DFS");

    boolean modoExcluir = false;
    Integer valorDestacado = null;
    PainelArvore painel = new PainelArvore();

    // Cores personalizadas
    private final Color corFundoJanela = new Color(240, 242, 245);
    private final Color corFundoTopo = new Color(230, 230, 250);
    private final Color corBotaoNormal = new Color(74, 144, 226);   // azul padrão para todos
    private final Color corBotaoHover = new Color(53, 122, 189);
    private final Color corBotaoExcluirAtivo = new Color(233, 75, 60); // vermelho para excluir ativo
    private final Color corTextoBotao = Color.WHITE;
    private final Color corNo = new Color(127, 179, 255);
    private final Color corNoDestacado = new Color(255, 165, 0);
    private final Color corLinha = new Color(85, 85, 85);

    public SimuladorABB() {
        super("Simulador de Árvore Binária de Busca");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(corFundoJanela);
        setLayout(new BorderLayout());

        // --- Painel Título fixo no topo ---
        JLabel titulo = new JLabel("Simulador de Árvore Binária de Busca", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(30, 30, 30));
        JPanel painelTitulo = new JPanel(new BorderLayout());
        painelTitulo.setBackground(Color.WHITE);
        painelTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(180, 180, 180)));
        painelTitulo.add(titulo, BorderLayout.CENTER);
        painelTitulo.setPreferredSize(new Dimension(getWidth(), 50));
        add(painelTitulo, BorderLayout.NORTH);

        // --- Painel topo com input e botões ---
        Dimension tamanhoBotao = new Dimension(120, 34);
        Dimension tamanhoInput = new Dimension(80, 34);

        input.setPreferredSize(tamanhoInput);
        inserir.setPreferredSize(tamanhoBotao);
        excluir.setPreferredSize(tamanhoBotao);
        buscar.setPreferredSize(tamanhoBotao);
        exportar.setPreferredSize(new Dimension(160, 34));
        emOrdem.setPreferredSize(tamanhoBotao);
        preOrdem.setPreferredSize(tamanhoBotao);
        posOrdem.setPreferredSize(tamanhoBotao);
        bfs.setPreferredSize(tamanhoBotao);
        dfs.setPreferredSize(tamanhoBotao);

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topo.setBackground(corFundoTopo);
        topo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 210)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        topo.add(new JLabel("VALOR:"));
        topo.add(input);
        topo.add(inserir);
        topo.add(excluir);
        topo.add(buscar);
        topo.add(exportar);
        topo.add(emOrdem);
        topo.add(preOrdem);
        topo.add(posOrdem);
        topo.add(bfs);
        topo.add(dfs);

        add(topo, BorderLayout.PAGE_START);

        // Estilizar botões
        estilizarBotao(inserir, corBotaoNormal, corBotaoHover);
        estilizarBotao(buscar, corBotaoNormal, corBotaoHover);
        estilizarBotao(exportar, corBotaoNormal, corBotaoHover);
        estilizarBotao(emOrdem, corBotaoNormal, corBotaoHover);
        estilizarBotao(preOrdem, corBotaoNormal, corBotaoHover);
        estilizarBotao(posOrdem, corBotaoNormal, corBotaoHover);
        estilizarBotao(bfs, corBotaoNormal, corBotaoHover);
        estilizarBotao(dfs, corBotaoNormal, corBotaoHover);
        estilizarBotao(excluir, corBotaoNormal, corBotaoHover); // excluir começa azul

        // Eventos dos botões
        inserir.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(input.getText());
                arvore.inserir(valor);
                valorDestacado = null;
                painel.atualizarTamanho();
                painel.repaint();
                input.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um número inteiro.");
            }
        });

        excluir.addActionListener(e -> {
            modoExcluir = !modoExcluir;
            // Quando ativo, fundo vermelho, senão azul normal
            excluir.setBackground(modoExcluir ? corBotaoExcluirAtivo : corBotaoNormal);
            excluir.setForeground(corTextoBotao);
        });

        buscar.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(input.getText());
                boolean encontrado = arvore.buscar(valor);
                valorDestacado = encontrado ? valor : null;
                JOptionPane.showMessageDialog(this,
                        encontrado ? "Elemento " + valor + " encontrado!" : "Elemento " + valor + " não encontrado.");
                painel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um número inteiro.");
            }
        });

        exportar.addActionListener(e -> {
            BufferedImage imagem = new BufferedImage(
                    painel.getWidth(), painel.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = imagem.createGraphics();
            painel.paint(g2);
            g2.dispose();
            try {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Salvar Imagem da Árvore");
                if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File arquivo = chooser.getSelectedFile();
                    ImageIO.write(imagem, "png", new File(arquivo.getAbsolutePath() + ".png"));
                    JOptionPane.showMessageDialog(this, "Imagem salva com sucesso!");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar imagem: " + ex.getMessage());
            }
        });

        emOrdem.addActionListener(e -> mostrarPercurso("emOrdem"));
        preOrdem.addActionListener(e -> mostrarPercurso("preOrdem"));
        posOrdem.addActionListener(e -> mostrarPercurso("posOrdem"));
        bfs.addActionListener(e -> mostrarPercurso("bfs"));
        dfs.addActionListener(e -> mostrarPercurso("dfs"));

        // Painel com JScrollPane para a árvore
        JScrollPane scroll = new JScrollPane(painel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }

    // Método para estilizar botões com cor, hover e borda arredondada
    private void estilizarBotao(JButton botao, Color corFundo, Color corHover) {
        botao.setBackground(corFundo);
        botao.setForeground(corTextoBotao);
        botao.setFocusPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setBorder(new RoundedBorder(8));
        botao.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(corHover);
            }

            public void mouseExited(MouseEvent e) {
                if (botao == excluir) {
                    // Se ativo, vermelho, senão azul normal
                    botao.setBackground(modoExcluir ? corBotaoExcluirAtivo : corBotaoNormal);
                } else {
                    botao.setBackground(corFundo);
                }
                botao.setForeground(corTextoBotao);
            }
        });
    }

    void mostrarPercurso(String tipo) {
        arvore.limparPercurso();
        switch (tipo) {
            case "emOrdem" -> arvore.emOrdem(arvore.raiz);
            case "preOrdem" -> arvore.preOrdem(arvore.raiz);
            case "posOrdem" -> arvore.posOrdem(arvore.raiz);
            case "bfs" -> arvore.bfs(arvore.raiz);
            case "dfs" -> arvore.dfs(arvore.raiz);
        }

        String titulo;
        switch (tipo) {
            case "emOrdem" -> titulo = "Em Ordem";
            case "preOrdem" -> titulo = "Pré-Ordem";
            case "posOrdem" -> titulo = "Pós-Ordem";
            case "bfs" -> titulo = "Busca em Largura (BFS)";
            case "dfs" -> titulo = "Busca em Profundidade (DFS)";
            default -> titulo = "Percurso";
        }

        JOptionPane.showMessageDialog(this, titulo + ": " + arvore.percurso);
    }

    // Borda arredondada para botões
    static class RoundedBorder extends AbstractBorder {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    class PainelArvore extends JPanel {
        private final int RAIO = 30;
        private final int DIST_Y = 90;
        private double escala = 1.0;
        private int largura = 2000;
        private int altura = 2000;

        public PainelArvore() {
            setBackground(Color.WHITE);
            addMouseWheelListener(e -> {
                if (e.getPreciseWheelRotation() < 0) {
                    escala = Math.min(escala + 0.1, 3.0);
                } else {
                    escala = Math.max(escala - 0.1, 0.3);
                }
                repaint();
            });

            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (modoExcluir) {
                        No clicado = encontrarClique(arvore.raiz, (int) (e.getX() / escala), (int) (e.getY() / escala));
                        if (clicado != null) {
                            arvore.remover(clicado.valor);
                            valorDestacado = null;
                            atualizarTamanho();
                            repaint();

                            // Se a árvore ficou vazia após a remoção, desative modoExcluir e atualize botão
                            if (arvore.raiz == null) {
                                modoExcluir = false;
                                excluir.setBackground(corBotaoNormal);
                                excluir.setForeground(corTextoBotao);
                            }
                        }
                    }
                }
            });

        }

        public void atualizarTamanho() {
            int profundidade = calcularProfundidade(arvore.raiz);
            largura = Math.max(1000, (int) Math.pow(2, profundidade) * 80);
            altura = profundidade * DIST_Y + 120;
            revalidate(); // atualiza o layout do JScrollPane
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(largura, altura);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.scale(escala, escala);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (arvore.raiz != null) {
                int profundidade = calcularProfundidade(arvore.raiz);
                int offsetInicial = Math.max(40, largura / (int) Math.pow(2, profundidade));

                desenhar(g2, arvore.raiz, largura / 2, 40, offsetInicial);
            }
        }

        private void desenhar(Graphics2D g2, No no, int x, int y, int offset) {
            no.x = x;
            no.y = y;

            // Sombra leve para os nós
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillOval(x - RAIO + 3, y - RAIO + 3, 2 * RAIO, 2 * RAIO);

            Color corFundo = (valorDestacado != null && no.valor == valorDestacado)
                    ? corNoDestacado
                    : corNo;

            g2.setColor(corFundo);
            g2.fillOval(x - RAIO, y - RAIO, 2 * RAIO, 2 * RAIO);

            g2.setColor(corLinha);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x - RAIO, y - RAIO, 2 * RAIO, 2 * RAIO);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            String texto = String.valueOf(no.valor);
            FontMetrics fm = g2.getFontMetrics();
            int larguraTexto = fm.stringWidth(texto);
            int alturaTexto = fm.getAscent();

            // Texto com contorno branco para melhor contraste
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.drawString(texto, x - larguraTexto / 2, y + alturaTexto / 4);

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1));
            g2.drawString(texto, x - larguraTexto / 2, y + alturaTexto / 4);

            g2.setStroke(new BasicStroke(1.5f));
            if (no.esquerda != null) {
                int filhoX = x - offset;
                int filhoY = y + DIST_Y;
                desenharLinhaEntreNodos(g2, x, y, filhoX, filhoY);
                desenhar(g2, no.esquerda, filhoX, filhoY, offset / 2);
            }
            if (no.direita != null) {
                int filhoX = x + offset;
                int filhoY = y + DIST_Y;
                desenharLinhaEntreNodos(g2, x, y, filhoX, filhoY);
                desenhar(g2, no.direita, filhoX, filhoY, offset / 2);
            }
        }

        private void desenharLinhaEntreNodos(Graphics2D g2, int x1, int y1, int x2, int y2) {
            double dx = x2 - x1;
            double dy = y2 - y1;
            double dist = Math.sqrt(dx * dx + dy * dy);
            double offsetX = RAIO * dx / dist;
            double offsetY = RAIO * dy / dist;
            int startX = (int) (x1 + offsetX);
            int startY = (int) (y1 + offsetY);
            int endX = (int) (x2 - offsetX);
            int endY = (int) (y2 - offsetY);
            g2.setColor(corLinha);
            g2.drawLine(startX, startY, endX, endY);
        }

        private No encontrarClique(No no, int x, int y) {
            if (no == null) return null;
            int dx = x - no.x;
            int dy = y - no.y;
            if (Math.sqrt(dx * dx + dy * dy) <= RAIO)
                return no;
            No esq = encontrarClique(no.esquerda, x, y);
            if (esq != null) return esq;
            return encontrarClique(no.direita, x, y);
        }

        private int calcularProfundidade(No no) {
            if (no == null) return 0;
            return 1 + Math.max(calcularProfundidade(no.esquerda), calcularProfundidade(no.direita));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimuladorABB::new);
    }
}




