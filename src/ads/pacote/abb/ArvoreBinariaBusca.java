package ads.pacote.abb;

// ArvoreBinariaBusca.java
import java.util.*;

public class ArvoreBinariaBusca {
    public No raiz;
    public List<Integer> percurso = new ArrayList<>();

    public void inserir(int valor) {
        raiz = inserirRec(raiz, valor);
    }

    private No inserirRec(No raiz, int valor) {
        if (raiz == null) return new No(valor);
        if (valor < raiz.valor)
            raiz.esquerda = inserirRec(raiz.esquerda, valor);
        else if (valor > raiz.valor)
            raiz.direita = inserirRec(raiz.direita, valor);
        return raiz;
    }

    public void remover(int valor) {
        raiz = removerRec(raiz, valor);
    }

    private No removerRec(No raiz, int valor) {
        if (raiz == null) return null;
        if (valor < raiz.valor)
            raiz.esquerda = removerRec(raiz.esquerda, valor);
        else if (valor > raiz.valor)
            raiz.direita = removerRec(raiz.direita, valor);
        else {
            if (raiz.esquerda == null) return raiz.direita;
            if (raiz.direita == null) return raiz.esquerda;
            raiz.valor = menorValor(raiz.direita);
            raiz.direita = removerRec(raiz.direita, raiz.valor);
        }
        return raiz;
    }

    private int menorValor(No no) {
        while (no.esquerda != null) no = no.esquerda;
        return no.valor;
    }

    public boolean buscar(int valor) {
        return buscarRec(raiz, valor);
    }

    private boolean buscarRec(No no, int valor) {
        if (no == null) return false;
        if (valor == no.valor) return true;
        return valor < no.valor ? buscarRec(no.esquerda, valor) : buscarRec(no.direita, valor);
    }

    public void limparPercurso() {
        percurso.clear();
    }

    public void emOrdem(No no) {
        if (no != null) {
            emOrdem(no.esquerda);
            percurso.add(no.valor);
            emOrdem(no.direita);
        }
    }

    public void preOrdem(No no) {
        if (no != null) {
            percurso.add(no.valor);
            preOrdem(no.esquerda);
            preOrdem(no.direita);
        }
    }

    public void posOrdem(No no) {
        if (no != null) {
            posOrdem(no.esquerda);
            posOrdem(no.direita);
            percurso.add(no.valor);
        }
    }

    public void bfs(No raiz) {
        if (raiz == null) return;
        Queue<No> fila = new LinkedList<>();
        fila.add(raiz);
        while (!fila.isEmpty()) {
            No atual = fila.poll();
            percurso.add(atual.valor);
            if (atual.esquerda != null) fila.add(atual.esquerda);
            if (atual.direita != null) fila.add(atual.direita);
        }
    }

    public void dfs(No no) {
        if (no == null) return;
        percurso.add(no.valor);
        dfs(no.esquerda);
        dfs(no.direita);
    }
}
