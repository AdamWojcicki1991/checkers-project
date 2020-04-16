package com.checkers.engine.board;

public class Move {
    public int initialRow, initialColumn;
    public int destinationRow, destinationColumn;

    public Move(int initialRow, int initialColumn, int destinationRow, int destinationColumn) {
        this.initialRow = initialRow;
        this.initialColumn = initialColumn;
        this.destinationRow = destinationRow;
        this.destinationColumn = destinationColumn;
    }

    public boolean isJump() {
        return (initialRow - destinationRow == 2 || initialRow - destinationRow == -2);
    }

    public boolean isNormal() {
        return (initialRow - destinationRow == 1 || initialRow - destinationRow == -1);
    }
}
