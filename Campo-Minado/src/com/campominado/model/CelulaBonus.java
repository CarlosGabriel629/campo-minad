package com.campominado.model;

public class CelulaBonus extends ElementoTabuleiro {

    public CelulaBonus(int linha, int coluna) {
        super(linha, coluna);
    }

    @Override
    public void interagir() {
        this.revelar();
    }

    @Override
    public int calcularPontos() {
        return 50; // Bônus extra de pontos
    }

    @Override
    public String toString() {
        if (marcada) return "[🚩]";
        if (!revelada) return "[ ? ]";
        return "[ ⭐ ]";
    }
}