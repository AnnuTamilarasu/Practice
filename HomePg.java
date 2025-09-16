<!DOCTYPE html>
<html>
<head>
    <title>Sudoku Game</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body style="margin:0; padding:0; background-color:#000;">
    <div id="home" style="position:relative; width:100%; height:100vh; background-image:url([[${bgImgPath}]]);
        background-size:cover;">
        <button id="playBtn" class="homeBtn">Play</button>
        <button id="tutorialBtn" class="homeBtn">Tutorial</button>
    </div>

    <div id="game" style="display:none;">
        <h1>Sudoku</h1>
        <div id="sudokuBoard"></div>
        <div id="numBar"></div>
        <div id="timer">00:00</div>
    </div>

    <script src="/js/game.js"></script>
</body>
</html>
