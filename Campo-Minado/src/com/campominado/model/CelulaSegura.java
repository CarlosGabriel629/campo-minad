// CelulaSegura.java
package com.campominado.model;

public class CelulaSegura extends ElementoTabuleiro {
    private int minasVizinhas;

    public CelulaSegura(int linha, int coluna) {
        super(linha, coluna);
        this.minasVizinhas = 0;
    }

    @Override
    public void interagir() {
        this.revelar();
    }

    @Override
    public int calcularPontos() {
        return 10; // Célula segura concede 10 pontos básicos
    }

    public int getMinasVizinhas() { return minasVizinhas; }
    public void setMinasVizinhas(int minasVizinhas) { this.minasVizinhas = minasVizinhas; }

    @Override
    public String toString() {
        if (marcada) return "[🚩]";
        if (!revelada) return "[ ? ]";
        if (minasVizinhas == 0) return "[   ]";
        return "[ " + minasVizinhas + " ]";
    }
}