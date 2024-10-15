package battleship.game;

import battleship.game.entities.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BattleShipGame {
    public static class In {
        private static final Scanner scanner = new Scanner(System.in);

        public static Coordinates nextCoordinates() throws InputMismatchException {
            String coordinatesString = scanner.next();

            char vertical;
            int horizontal;

            try {
                vertical = coordinatesString.charAt(0);
            } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                throw new InputMismatchException("Error: Empty coordinate!");
            }

            try {
                horizontal = Integer.parseInt(coordinatesString.substring(1));
            } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                throw new InputMismatchException("Error: There is not second coordinate!");
            } catch (NumberFormatException numberFormatException) {
                throw new InputMismatchException("Error: The second coordinate is not number!");
            }

            return new Coordinates(vertical, horizontal);
        }

        public static void waitNextPlayer() {
            scanner.nextLine();
            scanner.nextLine();
        }
    }

    public static class Out {
        private static class Message {
            private final static String PLAYER_PLACE_SHIPS = "Player %d, place your ships on the game field\n";

            private final static String INPUT_SHIP_COORDINATES = "Enter the coordinates of the %s (%d cells): ";

            private final static String GAME_START = "The game starts!\n";

            private final static String TAKE_SHOT = "Player %d, it's your turn: ";
            private final static String YOU_HIT_SHIP = "You hit a ship!\n";
            private final static String YOU_SANK_SHIP = "You sank a ship!\n";
            private final static String YOU_MISSED = "You missed.\n";
            private final static String YOU_WIN = "You sank the last ship. You won. Congratulations!\n";

            private final static String WAIT = "Press Enter and pass the move to another player\n";
        }

        public static void askPlaceShips(int n) {
            System.out.printf(Message.PLAYER_PLACE_SHIPS, n);
        }

        public static void askShipCoordinates(BattleShip.Type type) {
            System.out.printf(Message.INPUT_SHIP_COORDINATES, type.getInGameName(), type.getLength());
        }

        public static void tellGameStart() {
            System.out.print(Message.GAME_START);
        }

        public static void askTakeShot(int n) {
            System.out.printf(Message.TAKE_SHOT, n);
        }

        public static void tellShotResult(ShotResult shotResult) {
            System.out.print(
                switch (shotResult) {
                    case MISS -> Message.YOU_MISSED;
                    case HIT -> Message.YOU_HIT_SHIP;
                    case SANK_SHIP -> Message.YOU_SANK_SHIP;
                    case DEFEAT -> Message.YOU_WIN;
                }
            );
        }

        public static void showGameBoard(Player player1, Player player2) {
            player2.showCensoredBoard();
            System.out.println("---------------------");
            player1.showUncensoredBoard();
        }

        public static void waitNextPlayerMessage() {
            System.out.print(Message.WAIT);
        }

        public static void flush() {
            System.out.flush();
        }
    }

    private final Rules rules;
    private final Player player1;
    private final Player player2;

    private Board createBoard() {
        return new Board(rules.length(), rules.width(), rules.firstLetter(), rules.firstNumber());
    }

    public BattleShipGame() {
        this.rules = new Rules();
        this.player1 = new Player(createBoard());
        this.player2 = new Player(createBoard());
    }

    private ShotResult makeShot(Player shouter, Player receiver, int n) {
        ShotResult shotResult;

        BattleShipGame.Out.showGameBoard(shouter, receiver);
        BattleShipGame.Out.askTakeShot(n);

        for (;;) {
            try {
                shotResult = shouter.makeShot(receiver);
                Out.tellShotResult(shotResult);
                break;
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }

        return shotResult;
    }

    private void placeBattleShips(Player player, int n) {
        BattleShipGame.Out.askPlaceShips(n);
        player.showCensoredBoard();

        for (int i = 0; i < rules.ships().length;) {
            BattleShipGame.Out.askShipCoordinates(rules.ships()[i]);

            for (;;) {
                try {
                    player.placeBattleShip(rules.ships()[i]);
                    player.showUncensoredBoard();
                    ++i;
                    break;
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            }
        }
    }

    private void waitNextPlayer() {
        BattleShipGame.Out.waitNextPlayerMessage();
        BattleShipGame.In.waitNextPlayer();
        BattleShipGame.Out.flush();
    }

    public void startGame() {
        placeBattleShips(player1, 1);
        waitNextPlayer();

        placeBattleShips(player2, 2);
        waitNextPlayer();

        BattleShipGame.Out.tellGameStart();

        for (;;) {
            if (makeShot(player1, player2, 1) == ShotResult.DEFEAT) {
                return;
            }
            waitNextPlayer();

            if (makeShot(player2, player1, 2) == ShotResult.DEFEAT) {
                return;
            }
            waitNextPlayer();
        }
    }
}
