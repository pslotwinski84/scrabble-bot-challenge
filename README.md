Welcome to Scrabble Bot game. Have you ever wondered how is it possible to have 2000 literaki ranking on Kurnik, and be able to put several "scrabble", 7 letters combinations during one 3 minute game? It's easy if you are a computer bot:) Check the source code for details.

1. HOW TO BUILD THE APP

run build.bat under windows or type "mvn clean install" in a project directory.

2. HOW TO RUN THE APP

run run.bat under windows.

You will find the game under following URL: http://localhost:8080/scrab

Please login with following credencials: login "admin", password "admin@123". The menu is self explanatory, what is worth to note is that when you press "NOWA GRA" for the first time after the server start up, you will have to wait 2 minutes, as computer loads all the necessary dictionary sets into RAM. The game actually starts when your letters appear, and when the "player" or "komputer" section starts to blink. If it's your turn - "player" section blinks. Every finished game's replay is written into the data base. The game is considered finished when one of the players puts all the letters to the board, or when you press "nowa gra" again. In the menu section you can find all the recent games replays - but only those which began after last server start up. It's due to the fact that data base is destroyed when you close the server. It's in memory data base, H2. The game is written for Polish character set. Enjoy;)