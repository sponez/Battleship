package battleship.game.entities;

import java.util.Objects;

public record Rules(int length, int width, char firstLetter, int firstNumber, BattleShip.Type[] ships) {
    public Rules {
        if (length <= 0) {
            throw new IllegalArgumentException("The game board length must be more than 0");
        }

        if (width <= 0) {
            throw new IllegalArgumentException("The game board width must be more than 0");
        }

        if (ships.length == 0) {
            throw new IllegalArgumentException("Ships amount must be more than 0");
        }

        int remainingArea = length * width;
        for (BattleShip.Type battleShip: ships) {
            if (length < battleShip.getLength() && width < battleShip.getLength()) {
                throw new IllegalArgumentException(
                    String.format(
                        "Your board too small for %s",
                        battleShip.getInGameName()
                    )
                );
            }

            remainingArea -= 2 * (battleShip.getLength() + 1);
        }

        if (remainingArea < 0) {
            throw new IllegalArgumentException("Your board too small for all ships");
        }
    }

    public Rules() {
        this(
            10,
            10,
            'A',
            1,
            new BattleShip.Type[] {
                BattleShip.Type.AIRCRAFT_CARRIER,
                BattleShip.Type.BATTLESHIP,
                BattleShip.Type.SUBMARINE,
                BattleShip.Type.CRUISER,
                BattleShip.Type.DESTROYER
            }
        );
    }
}