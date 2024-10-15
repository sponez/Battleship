package battleship;

import battleship.game.BattleShipGame;

public class Main {
    public static void main(String[] args) {
        BattleShipGame gameController = new BattleShipGame();
        gameController.startGame();
    }
}
