// Configurações das Dificuldades
const LEVELS = {
    easy: { rows: 9, cols: 9, mines: 10 },
    medium: { rows: 12, cols: 12, mines: 20 },
    hard: { rows: 16, cols: 16, mines: 40 }
};

// Estado do Jogo
let currentLevel = 'easy';
let rows, cols, minesCount;
let grid = [];
let gameOver = false;
let gameStarted = false;
let timer = 0;
let timerInterval = null;
let flagsPlaced = 0;

// Elementos da DOM
const boardEl = document.getElementById('board');
const minesValEl = document.getElementById('mines-val');
const timerValEl = document.getElementById('timer-val');
const faceEmojiEl = document.getElementById('face-emoji');
const resetBtn = document.getElementById('reset-btn');
const diffButtons = document.querySelectorAll('.btn-diff');

const modal = document.getElementById('modal');
const modalIcon = document.getElementById('modal-icon');
const modalTitle = document.getElementById('modal-title');
const modalText = document.getElementById('modal-text');
const modalBtn = document.getElementById('modal-btn');

// Inicialização
initGame();

// Event ListenersGlobais
resetBtn.addEventListener('click', () => initGame());
modalBtn.addEventListener('click', () => {
    hideModal();
    initGame();
});

diffButtons.forEach(btn => {
    btn.addEventListener('click', (e) => {
        diffButtons.forEach(b => b.classList.remove('active'));
        e.target.classList.add('active');
        currentLevel = e.target.dataset.level;
        initGame();
    });
});

// Função de Inicialização
function initGame() {
    clearInterval(timerInterval);
    timer = 0;
    gameStarted = false;
    gameOver = false;
    flagsPlaced = 0;

    const config = LEVELS[currentLevel];
    rows = config.rows;
    cols = config.cols;
    minesCount = config.mines;

    updateDisplay();
    faceEmojiEl.textContent = '🙂';

    // Configurar Grid CSS
    boardEl.style.gridTemplateColumns = `repeat(${cols}, 36px)`;
    boardEl.innerHTML = '';

    // Criar Estrutura de Dados
    grid = [];
    for (let r = 0; r < rows; r++) {
        grid[r] = [];
        for (let c = 0; c < cols; c++) {
            grid[r][c] = {
                row: r,
                col: c,
                isMine: false,
                isRevealed: false,
                isFlagged: false,
                neighborMines: 0,
                element: null
            };

            // Criar Elemento HTML da Célula
            const cellEl = document.createElement('div');
            cellEl.classList.add('cell');

            // Eventos de Clique
            cellEl.addEventListener('click', () => handleCellClick(r, c));
            cellEl.addEventListener('contextmenu', (e) => {
                e.preventDefault();
                handleCellFlag(r, c);
            });

            cellEl.addEventListener('mousedown', () => {
                if (!gameOver && !grid[r][c].isRevealed) faceEmojiEl.textContent = '😮';
            });
            cellEl.addEventListener('mouseup', () => {
                if (!gameOver) faceEmojiEl.textContent = '🙂';
            });

            grid[r][c].element = cellEl;
            boardEl.appendChild(cellEl);
        }
    }
}

// Primeiro Clique e Gerador de Minas
function startGame(firstRow, firstCol) {
    gameStarted = true;
    startTimer();

    // Distribuir minas garantindo que o 1º clique e vizinhos sejam seguros
    let placedMines = 0;
    while (placedMines < minesCount) {
        const r = Math.floor(Math.random() * rows);
        const c = Math.floor(Math.random() * cols);

        const isFirstClickArea = Math.abs(r - firstRow) <= 1 && Math.abs(c - firstCol) <= 1;

        if (!grid[r][c].isMine && !isFirstClickArea) {
            grid[r][c].isMine = true;
            placedMines++;
        }
    }

    // Calcular vizinhos
    for (let r = 0; r < rows; r++) {
        for (let c = 0; c < cols; c++) {
            if (!grid[r][c].isMine) {
                let count = 0;
                getNeighbors(r, c).forEach(n => {
                    if (n.isMine) count++;
                });
                grid[r][c].neighborMines = count;
            }
        }
    }
}

// Lógica ao Clicar na Célula
function handleCellClick(r, c) {
    if (gameOver) return;
    const cell = grid[r][c];

    if (cell.isFlagged || cell.isRevealed) return;

    if (!gameStarted) {
        startGame(r, c);
    }

    revealCell(r, c);
}

// Revelar Célula
function revealCell(r, c) {
    const cell = grid[r][c];
    if (cell.isRevealed || cell.isFlagged) return;

    cell.isRevealed = true;
    cell.element.classList.add('revealed');

    if (cell.isMine) {
        cell.element.classList.add('mine');
        cell.element.textContent = '💣';
        handleGameOver(false);
        return;
    }

    if (cell.neighborMines > 0) {
        cell.element.textContent = cell.neighborMines;
        cell.element.dataset.num = cell.neighborMines;
    } else {
        // Expansão em Cascata (Flood Fill)
        getNeighbors(r, c).forEach(n => {
            if (!n.isRevealed) revealCell(n.row, n.col);
        });
    }

    checkWin();
}

// Alternar Bandeira
function handleCellFlag(r, c) {
    if (gameOver || !gameStarted) return;
    const cell = grid[r][c];

    if (cell.isRevealed) return;

    cell.isFlagged = !cell.isFlagged;
    if (cell.isFlagged) {
        cell.element.textContent = '🚩';
        flagsPlaced++;
    } else {
        cell.element.textContent = '';
        flagsPlaced--;
    }

    updateDisplay();
}

// Funções Auxiliares
function getNeighbors(r, c) {
    const neighbors = [];
    for (let dr = -1; dr <= 1; dr++) {
        for (let dc = -1; dc <= 1; dc++) {
            if (dr === 0 && dc === 0) continue;
            const nr = r + dr;
            const nc = c + dc;
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                neighbors.push(grid[nr][nc]);
            }
        }
    }
    return neighbors;
}

function startTimer() {
    timerInterval = setInterval(() => {
        timer++;
        if (timer > 999) timer = 999;
        updateDisplay();
    }, 1000);
}

function updateDisplay() {
    const remainingMines = minesCount - flagsPlaced;
    minesValEl.textContent = String(Math.max(0, remainingMines)).padStart(3, '0');
    timerValEl.textContent = String(timer).padStart(3, '0');
}

// Checar Vitória
function checkWin() {
    let unrevealedSafeCells = 0;
    for (let r = 0; r < rows; r++) {
        for (let c = 0; c < cols; c++) {
            if (!grid[r][c].isMine && !grid[r][c].isRevealed) {
                unrevealedSafeCells++;
            }
        }
    }

    if (unrevealedSafeCells === 0) {
        handleGameOver(true);
    }
}

// Fim de Jogo
function handleGameOver(isWin) {
    gameOver = true;
    clearInterval(timerInterval);

    if (isWin) {
        faceEmojiEl.textContent = '😎';
        showModal('🏆', 'Você Venceu!', `Tempo total: ${timer} segundos.`);
    } else {
        faceEmojiEl.textContent = '😵';
        // Revelar todas as minas
        for (let r = 0; r < rows; r++) {
            for (let c = 0; c < cols; c++) {
                if (grid[r][c].isMine) {
                    grid[r][c].element.classList.add('revealed', 'mine');
                    grid[r][c].element.textContent = '💣';
                }
            }
        }
        showModal('💥', 'Fim de Jogo!', 'Você atingiu uma mina terrestre.');
    }
}

// Controle do Modal
function showModal(icon, title, text) {
    modalIcon.textContent = icon;
    modalTitle.textContent = title;
    modalText.textContent = text;
    modal.classList.add('active');
}

function hideModal() {
    modal.classList.remove('active');
}