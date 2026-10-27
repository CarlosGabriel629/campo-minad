// CelulaMina.java
package com.campominado.model;

public class CelulaMina extends ElementoTabuleiro {

    public CelulaMina(int linha, int coluna) {
        super(linha, coluna);
    }

    @Override
    public void interagir() {
        this.revelar(); // Detona a bomba ao ser revelada
    }

    @Override
    public int calcularPontos() {
        return -50; // Revelar mina penaliza o jogador
    }

    @Override
    public String toString() {
        if (marcada) return "[🚩]";
        if (!revelada) return "[ ? ]";
        return "[💣]";
    }
}