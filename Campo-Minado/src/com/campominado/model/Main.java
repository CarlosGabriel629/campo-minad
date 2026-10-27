package com.campominado.model;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Aponta para o arquivo HTML baseado na estrutura do seu projeto
            File htmlFile = new File("src/view/index.html");

            if (htmlFile.exists()) {
                // Abre a tela no navegador padrão do sistema (Chrome, Edge, etc.)
                Desktop.getDesktop().browse(htmlFile.toURI());
            } else {
                System.out.println("Arquivo index.html não encontrado verifique o caminho.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao tentar abrir o navegador: " + e.getMessage());
        }
    }
}