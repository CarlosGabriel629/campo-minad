package com.campominado.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tabuleiro {
    private final int linhas;
    private final int colunas;
    private final int quantidadeMinas;
    private final ElementoTabuleiro[][] grid;
    private StatusJogo status;

    public Tabuleiro(int linhas, int colunas, int quantidadeMinas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.quantidadeMinas = quantidadeMinas;
        this.grid = new ElementoTabuleiro[linhas][colunas];
        this.status = StatusJogo.EM_ANDAMENTO;

        inicializarGrid();
        distribuirMinas();
        distribuirBonus();
        calcularVizinhos();
    }

    private void inicializarGrid() {
        for (int r = 0; r < linhas; r++) {
            for (int c = 0; c < colunas; c++) {
                grid[r][c] = new CelulaSegura(r, c);
            }
        }
    }

    private void distribuirMinas() {
        Random random = new Random();
        int minasColocadas = 0;
        while (minasColocadas < quantidadeMinas) {
            int r = random.nextInt(linhas);
            int c = random.nextInt(colunas);
            if (grid[r][c] instanceof CelulaSegura) {
                grid[r][c] = new CelulaMina(r, c);
                minasColocadas++;
            }
        }
    }

    private void distribuirBonus() {
        Random random = new Random();
        int bonusColocados = 0;
        while (bonusColocados < 2) { // 2 bônus por partida
            int r = random.nextInt(linhas);
            int c = random.nextInt(colunas);
            if (grid[r][c] instanceof CelulaSegura) {
                grid[r][c] = new CelulaBonus(r, c);
                bonusColocados++;
            }
        }
    }

    private void calcularVizinhos() {
        for (int r = 0; r < linhas; r++) {
            for (int c = 0; c < colunas; c++) {
                if (grid[r][c] instanceof CelulaSegura segura) {
                    long count = getVizinhos(r, c).stream()
                            .filter(e -> e instanceof CelulaMina)
                            .count();
                    segura.setMinasVizinhas((int) count);
                }
            }
        }
    }

    public List<ElementoTabuleiro> getVizinhos(int linha, int coluna) {
        List<ElementoTabuleiro> vizinhos = new ArrayList<>();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = linha + dr;
                int nc = coluna + dc;
                if (nr >= 0 && nr < linhas && nc >= 0 && nc < colunas) {
                    vizinhos.add(grid[nr][nc]);
                }
            }
        }
        return vizinhos;
    }

    public void revelarPosicao(int linha, int coluna) {
        if (status != StatusJogo.EM_ANDAMENTO) return;

        ElementoTabuleiro elemento = grid[linha][coluna];
        if (elemento.isRevelada() || elemento.isMarcada()) return;

        elemento.interagir();

        if (elemento instanceof CelulaMina) {
            this.status = StatusJogo.DERROTA;
            revelarTodasMinas();
            return;
        }

        if (elemento instanceof CelulaSegura segura && segura.getMinasVizinhas() == 0) {
            for (ElementoTabuleiro vizinho : getVizinhos(linha, coluna)) {
                if (!vizinho.isRevelada() && !(vizinho instanceof CelulaMina)) {
                    revelarPosicao(vizinho.getLinha(), vizinho.getColuna());
                }
            }
        }

        verificarVitoria();
    }

    public void alternarBandeira(int linha, int coluna) {
        if (status == StatusJogo.EM_ANDAMENTO) {
            grid[linha][coluna].alternarBandeira();
        }
    }

    private void revelarTodasMinas() {
        for (int r = 0; r < linhas; r++) {
            for (int c = 0; c < colunas; c++) {
                if (grid[r][c] instanceof CelulaMina) {
                    grid[r][c].revelar();
                }
            }
        }
    }

    private void verificarVitoria() {
        for (int r = 0; r < linhas; r++) {
            for (int c = 0; c < colunas; c++) {
                ElementoTabuleiro e = grid[r][c];
                if ((e instanceof CelulaSegura || e instanceof CelulaBonus) && !e.isRevelada()) {
                    return;
                }
            }
        }
        this.status = StatusJogo.VITORIA;
    }

    public int getPontuacaoTotal() {
        int pontos = 0;
        for (int r = 0; r < linhas; r++) {
            for (int c = 0; c < colunas; c++) {
                if (grid[r][c].isRevelada()) {
                    pontos += grid[r][c].calcularPontos();
                }
            }
        }
        return pontos;
    }

    public void exibeTabuleiro() {
        System.out.print("   ");
        for (int c = 0; c < colunas; c++) System.out.printf(" %d  ", c);
        System.out.println("\n  +" + "---+".repeat(colunas));

        for (int r = 0; r < linhas; r++) {
            System.out.print(r + " |");
            for (int c = 0; c < colunas; c++) {
                System.out.print(grid[r][c] + "|");
            }
            System.out.println("\n  +" + "---+".repeat(colunas));
        }
    }

    public int getLinhas() { return linhas; }
    public int getColunas() { return colunas; }
    public StatusJogo getStatus() { return status; }
}