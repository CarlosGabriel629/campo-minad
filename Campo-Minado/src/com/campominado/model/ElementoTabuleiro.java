package com.campominado.model;

public abstract class ElementoTabuleiro implements AcaoJogavel, Pontuavel {
    protected final int linha;
    protected final int coluna;
    protected boolean revelada;
    protected boolean marcada;

    public ElementoTabuleiro(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
        this.revelada = false;
        this.marcada = false;
    }

    @Override
    public boolean revelar() {
        if (!marcada && !revelada) {
            this.revelada = true;
            return true;
        }
        return false;
    }

    @Override
    public void alternarBandeira() {
        if (!revelada) {
            this.marcada = !this.marcada;
        }
    }

    // Método abstrato obriga as subclasses a implementarem seus comportamentos polimórficos
    public abstract void interagir();

    // Getters encapsulados
    public int getLinha() { return linha; }
    public int getColuna() { return coluna; }
    public boolean isRevelada() { return revelada; }
    public boolean isMarcada() { return marcada; }
}