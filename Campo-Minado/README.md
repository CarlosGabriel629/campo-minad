Campo Minado Tatical - Projeto POO

Projeto desenvolvido para demonstrar a aplicação prática e avançada dos conceitos de Programação Orientada a Objetos (POO) em Java. O jogo é uma versão tática do clássico Campo Minado, rodando via Console, com sistema de pontuação e células especiais.

📑 Sumário

Visão Geral e Objetivos

Regras do Jogo

Arquitetura e Padrões de POO

Estrutura de Classes

Como Executar

📸 Visão Geral e Objetivos

O objetivo deste projeto não é apenas criar um jogo funcional, mas utilizá-lo como um laboratório para estruturar código utilizando os pilares da Orientação a Objetos. O jogo apresenta um tabuleiro reativo onde o jogador deve deduzir a posição das minas, revelando áreas seguras e coletando bônus.

📜 Regras do Jogo

O tabuleiro é gerado com uma quantidade específica de Minas e Bônus ocultos.

O jogador insere coordenadas para Revelar (1) ou Marcar com Bandeira (2).

Se revelar uma Célula Segura, ela mostra o número de minas ao redor. Se for 0, ocorre a expansão automática (Flood Fill).

Se revelar um Bônus (⭐), ganha pontuação extra.

Se revelar uma Mina (💣), o jogo termina em Derrota.

Vitória: Ocorre quando todas as células que não são minas forem reveladas.

🧠 Arquitetura e Padrões de POO (Justificativas)

Este projeto cumpre rigorosamente os requisitos acadêmicos aplicando os seguintes conceitos:

1. Abstração e Herança

Implementação: Classe abstrata ElementoTabuleiro e subclasses CelulaSegura, CelulaMina, CelulaBonus.

Justificativa: Todos os itens do tabuleiro compartilham o conceito de ter uma coordenada (x, y) e estados (revelada, marcada). Isolamos essas características comuns na superclasse, evitando duplicação de código. As regras específicas de cada elemento ficam nas subclasses.

2. Polimorfismo

Implementação: Método abstrato interagir() sobrescrito em cada subclasse.

Justificativa: O Tabuleiro não precisa verificar o tipo da célula antes de interagir. Ao chamar grid[l][c].interagir(), a JVM decide em tempo de execução qual comportamento usar (ex: explodir a mina, adicionar pontos do bônus ou revelar vizinhos).

3. Interfaces

Implementação: Interfaces AcaoJogavel e Pontuavel.

Justificativa: Cria um contrato padronizado. A interface Pontuavel permite que qualquer objeto futuro (como um multiplicador de tempo) possa ser somado ao placar sem alterar a lógica principal do jogo, reduzindo o acoplamento.

4. Encapsulamento

Implementação: Modificadores private e protected, com uso estrito de Getters e Setters.

Justificativa: Impede que o jogador (ou a classe Main) altere manualmente o estado de uma célula ou a quantidade de minas do tabuleiro, garantindo a integridade das regras do jogo.

5. Composição

Implementação: A classe Tabuleiro possui uma matriz bidimensional de ElementoTabuleiro[][].

Justificativa: Estabelece uma relação de "Todo-Parte". O tabuleiro é composto por elementos. Se o tabuleiro for encerrado, os elementos deixam de existir no contexto da aplicação.

📂 Estrutura de Classes (Pacote com.campominado)

Main: Ponto de entrada, gerencia o loop do menu no console (I/O).

model/StatusJogo (Enum): Controla o estado atual (EM_ANDAMENTO, VITORIA, DERROTA).

model/AcaoJogavel (Interface): Contrato para interações (revelar(), alternarBandeira()).

model/Pontuavel (Interface): Contrato para cálculo de score (calcularPontos()).

model/ElementoTabuleiro (Abstract): Molde base de todas as peças do jogo.

model/CelulaSegura (Classe): Comporta a lógica de contagem de bombas vizinhas.

model/CelulaMina (Classe): Comporta a lógica de game over.

model/CelulaBonus (Classe): Célula especial para acréscimo de pontuação.

model/Tabuleiro (Classe): O "cérebro" do jogo. Gerencia a matriz, instanciamento aleatório e o algoritmo recursivo de Flood Fill.

🚀 Como Executar

Importe o projeto na sua IDE (IntelliJ IDEA, Eclipse ou VSCode).

Verifique se o JDK 17 ou superior está configurado.

Navegue até o arquivo src/com/campominado/Main.java.

Execute o método main. O jogo rodará diretamente no terminal/console da sua IDE.