package ads.pacote.abb;

class No {
    int valor;
    No esquerda, direita;
    int x, y; // coordenadas para desenhar

    public No(int valor) {
        this.valor = valor;
        esquerda = direita = null;
    }
}
