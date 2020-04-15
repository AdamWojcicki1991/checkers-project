package com.checkers.engine.board;

public class Move {
    public int fromRow, fromCol;  // Position of piece to be moved.
    public int toRow, toCol;      // Square it is to move to.

    public Move(int r1, int c1, int r2, int c2) {
        fromRow = r1;
        fromCol = c1;
        toRow = r2;
        toCol = c2;
    }

    public boolean isJump() {
        return (fromRow - toRow == 2 || fromRow - toRow == -2);
    }
}
