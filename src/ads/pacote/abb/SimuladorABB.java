package ads.pacote.abb;
// SimuladorABB.java
import javax.swing.*;
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

    public SimuladorABB() {
        super("Simulador de Árvore Binária de Busca");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.setBackground(new Color(230, 230, 250));
        topo.add(new JLabel("Valor:"));
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
            excluir.setBackground(modoExcluir ? Color.RED : null);
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

        add(topo, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(painel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        setVisible(true);
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
        JOptionPane.showMessageDialog(this, tipo.toUpperCase() + ": " + arvore.percurso);
    }

    class PainelArvore extends JPanel {
        private final int RAIO = 30;
        private final int DIST_Y = 90;
        private double escala = 1.0;
        private int largura = 2000;
        private int altura = 2000;

        public PainelArvore() {
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
                        No clicado = encontrarClique(arvore.raiz, e.getX(), e.getY());
                        if (clicado != null) {
                            arvore.remover(clicado.valor);
                            valorDestacado = null;
                            atualizarTamanho();
                            repaint();
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
            setBackground(Color.WHITE);
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

            Color corFundo = (valorDestacado != null && no.valor == valorDestacado)
                    ? new Color(255, 165, 0)
                    : new Color(120, 170, 255);

            g2.setColor(corFundo);
            g2.fillOval(x - RAIO, y - RAIO, 2 * RAIO, 2 * RAIO);

            g2.setColor(new Color(70, 70, 70));
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x - RAIO, y - RAIO, 2 * RAIO, 2 * RAIO);

            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            String texto = String.valueOf(no.valor);
            FontMetrics fm = g2.getFontMetrics();
            int larguraTexto = fm.stringWidth(texto);
            int alturaTexto = fm.getAscent();

            g2.setColor(new Color(0, 0, 0, 80));
            g2.drawString(texto, x - larguraTexto / 2 + 1, y + alturaTexto / 4 + 1);

            g2.setColor(Color.BLACK);
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


