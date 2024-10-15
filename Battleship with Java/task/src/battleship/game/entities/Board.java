package battleship.game.entities;

public class Board {
    private enum ShowProperties {
        UNCENSORED,
        CENSORED
    }

    private final int length;
    private final int width;
    private final char firstLetter;
    private final int firstNumber;
    private final Cell[][] cells;

    public Board(int length, int width, char firstLetter, int firstNumber) {
        this.length = length;
        this.width = width;
        this.cells = new Cell[length][width];

        this.firstLetter = firstLetter;
        this.firstNumber = firstNumber;

        for (char v = 0; v < this.length; ++v) {
            for (int h = 0; h < this.width; ++h) {
                cells[v][h] = new Cell(
                    (char) (this.firstLetter + v),
                    this.firstNumber + h
                );
            }
        }
    }

    public int getLength() {
        return this.length;
    }

    public int getWidth() {
        return this.width;
    }

    public char getFirstLetter() {
        return this.firstLetter;
    }

    public char getLastLetter() {
        return (char) (this.firstLetter + this.length - 1);
    }

    public int getFirstNumber() {
        return this.firstNumber;
    }

    public int getLastNumber() {
        return this.firstNumber + this.width - 1;
    }

    public Cell getCell(Coordinates coordinates) throws IndexOutOfBoundsException {
        return this.cells[coordinates.vertical() - this.firstLetter][coordinates.horizontal() - this.firstNumber];
    }

    private void show(Board.ShowProperties showProperties) {
        System.out.print(' ');
        for (int h = 0; h < this.width; ++h) {
            System.out.print(' ');
            System.out.print(this.firstNumber + h);
        }
        System.out.println();

        for (int v = 0; v < this.length; ++v) {
            System.out.print((char) (this.firstLetter + v));
            for (int h = 0; h < this.width; ++h) {
                System.out.print(' ');

                if (showProperties == ShowProperties.CENSORED && cells[v][h].getStatus() == Cell.Status.BATTLE_SHIP) {
                    System.out.print(Cell.Status.AVAILABLE.getSymbol());
                } else {
                    System.out.print(cells[v][h].getStatus().getSymbol());
                }
            }
            System.out.println();
        }
    }

    public void showCensored() {
        this.show(ShowProperties.CENSORED);
    }

    public void showUncensored() {
        this.show(ShowProperties.UNCENSORED);
    }
}
