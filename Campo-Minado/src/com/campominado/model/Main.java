package com.campominado.model;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Tabuleiro tabuleiro = new Tabuleiro(6, 6, 6);

        System.out.println("==========================================");
        System.out.println("  💣 CAMPO MINADO TATICAL - POO JAVA 💣  ");
        System.out.println("==========================================");

        while (tabuleiro.getStatus() == StatusJogo.EM_ANDAMENTO) {
            System.out.println();
            tabuleiro.exibeTabuleiro();

            System.out.println("\nPontuação atual: " + tabuleiro.getPontuacaoTotal() + " pts");
            System.out.println("Escolha uma ação: [1] Revelar Posição  |  [2] Colocar/Remover Bandeira (🚩)");
            System.out.print("Opção: ");

            int acao = scanner.nextInt();
            System.out.print("Informe a Linha: ");
            int l = scanner.nextInt();
            System.out.print("Informe a Coluna: ");
            int c = scanner.nextInt();

            if (l < 0 || l >= tabuleiro.getLinhas() || c < 0 || c >= tabuleiro.getColunas()) {
                System.out.println("\n⚠️ Coordenadas inválidas! Tente novamente.");
                continue;
            }

            if (acao == 1) {
                tabuleiro.revelarPosicao(l, c);
            } else if (acao == 2) {
                tabuleiro.alternarBandeira(l, c);
            } else {
                System.out.println("\n⚠️ Opção inválida!");
            }
        }

        System.out.println("\n==========================================");
        tabuleiro.exibeTabuleiro();
        System.out.println("==========================================");

        if (tabuleiro.getStatus() == StatusJogo.VITORIA) {
            System.out.println("\n🎉 PARABÉNS! Você revelou todo o campo com segurança!");
            System.out.println("🏆 Pontuação Final: " + tabuleiro.getPontuacaoTotal() + " pontos.");
        } else {
            System.out.println("\n💥 BOOM! Você atingiu uma mina terrestre.");
            System.out.println("☠️ Fim de Jogo!");
        }

        scanner.close();
    }
}