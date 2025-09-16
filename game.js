document.getElementById("playBtn").addEventListener("click", () => {
    document.getElementById("home").style.display = "none";
    document.getElementById("game").style.display = "block";
    initSudoku();
});

const SIZE = 9;
let selectedNumber = -1;

function initSudoku() {
    const boardDiv = document.getElementById("sudokuBoard");
    boardDiv.style.display = "grid";
    boardDiv.style.gridTemplateRows = `repeat(${SIZE}, 50px)`;
    boardDiv.style.gridTemplateColumns = `repeat(${SIZE}, 50px)`;
    boardDiv.style.gap = "2px";

    // Generate a simple empty board for now
    for (let r = 0; r < SIZE; r++) {
        for (let c = 0; c < SIZE; c++) {
            const cell = document.createElement("div");
            cell.className = "cell";
            cell.style.border = "1px solid black";
            cell.style.display = "flex";
            cell.style.justifyContent = "center";
            cell.style.alignItems = "center";
            cell.style.backgroundColor = "#f3e6d0";
            cell.dataset.row = r;
            cell.dataset.col = c;
            cell.innerText = "";
            cell.addEventListener("click", () => {
                if (selectedNumber != -1) {
                    cell.innerText = selectedNumber;
                }
            });
            boardDiv.appendChild(cell);
        }
    }

    // Number bar
    const numBar = document.getElementById("numBar");
    for (let i = 1; i <= 9; i++) {
        const btn = document.createElement("button");
        btn.innerText = i;
        btn.addEventListener("click", () => {
            selectedNumber = i;
        });
        numBar.appendChild(btn);
    }

    // Timer
    let seconds = 0;
    setInterval(() => {
        seconds++;
        const min = Math.floor(seconds / 60);
        const sec = seconds % 60;
        document.getElementById("timer").innerText = 
            `${min.toString().padStart(2,"0")}:${sec.toString().padStart(2,"0")}`;
    }, 1000);
}
