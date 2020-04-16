package com.checkers.engine.board;

import com.checkers.engine.playres.PlayerType;

import java.util.ArrayList;
import java.util.List;

import static com.checkers.engine.board.FigureType.*;
import static com.checkers.engine.playres.PlayerType.BLACK;
import static com.checkers.engine.playres.PlayerType.WHITE;
import static com.checkers.engine.utils.Constants.COLUMN_COUNT;
import static com.checkers.engine.utils.Constants.ROW_COUNT;

public class BoardProcessor {
    private FigureType[][] board;

    public BoardProcessor() {
        this.board = initFigures();
    }

    public FigureType[][] initFigures() {
        board = new FigureType[ROW_COUNT][COLUMN_COUNT];
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COLUMN_COUNT; col++) {
                if (row < 4 && (row + col) % 2 != 0) {
                    board[row][col] = BLACK_PAWN;
                } else if (row > 5 && (row + col) % 2 != 0) {
                    board[row][col] = WHITE_PAWN;
                } else {
                    board[row][col] = EMPTY;
                }
            }
        }
        return board;
    }

    public FigureType getFigureFromBoard(int row, int col) {
        return board[row][col];
    }

    public void makeMove(Move move) {
        board[move.destinationRow][move.destinationColumn] = board[move.initialRow][move.initialColumn];
        board[move.initialRow][move.initialColumn] = EMPTY;
        if (move.initialRow - move.destinationRow == 2 || move.initialRow - move.destinationRow == -2) {
            int jumpRow = (move.initialRow + move.destinationRow) / 2;  // Row of the jumped piece.
            int jumpCol = (move.initialColumn + move.destinationColumn) / 2;  // Column of the jumped piece.
            board[jumpRow][jumpCol] = EMPTY;
        }
        if (move.destinationRow == 0 && board[move.destinationRow][move.destinationColumn] == WHITE_PAWN)
            board[move.destinationRow][move.destinationColumn] = WHITE_QUEEN;
        if (move.destinationRow == 9 && board[move.destinationRow][move.destinationColumn] == BLACK_PAWN)
            board[move.destinationRow][move.destinationColumn] = BLACK_QUEEN;
    }

    public List<Move> getLegalMoves(PlayerType player) {
        if (player != WHITE && player != BLACK) return null;
        FigureType playerPawn;
        if (player == WHITE) {
            playerPawn = WHITE_PAWN;
        } else {
            playerPawn = BLACK_PAWN;
        }
        List<Move> legalMoves = new ArrayList<>();
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COLUMN_COUNT; col++) {
                if (board[row][col] == playerPawn) {
                    if (canJump(playerPawn, row + 1, col + 1, row + 2, col + 2))
                        legalMoves.add(new Move(row, col, row + 2, col + 2));
                    if (canJump(playerPawn, row + 1, col - 1, row + 2, col - 2))
                        legalMoves.add(new Move(row, col, row + 2, col - 2));
                    if (canJump(playerPawn, row - 1, col + 1, row - 2, col + 2))
                        legalMoves.add(new Move(row, col, row - 2, col + 2));
                    if (canJump(playerPawn, row - 1, col - 1, row - 2, col - 2))
                        legalMoves.add(new Move(row, col, row - 2, col - 2));
                }
            }
        }
        if (legalMoves.size() == 0) {
            for (int row = 0; row < ROW_COUNT; row++) {
                for (int col = 0; col < COLUMN_COUNT; col++) {
                    if (board[row][col] == playerPawn) {
                        if (canMove(playerPawn, row, col, row + 1, col + 1))
                            legalMoves.add(new Move(row, col, row + 1, col + 1));
                        if (canMove(playerPawn, row, col, row + 1, col - 1))
                            legalMoves.add(new Move(row, col, row + 1, col - 1));
                        if (canMove(playerPawn, row, col, row - 1, col + 1))
                            legalMoves.add(new Move(row, col, row - 1, col + 1));
                        if (canMove(playerPawn, row, col, row - 1, col - 1))
                            legalMoves.add(new Move(row, col, row - 1, col - 1));
                    }
                }
            }
        }
        return (legalMoves.isEmpty()) ? null : legalMoves;
    }

    public List<Move> getLegalJumps(PlayerType player, int row, int col) {
        if (player != WHITE && player != BLACK) return null;
        FigureType playerPawn;
        if (player == WHITE) {
            playerPawn = WHITE_PAWN;
        } else {
            playerPawn = BLACK_PAWN;
        }
        List<Move> legalJumps = new ArrayList<>();
        if (board[row][col] == playerPawn) {
            if (canJump(playerPawn, row + 1, col + 1, row + 2, col + 2))
                legalJumps.add(new Move(row, col, row + 2, col + 2));
            if (canJump(playerPawn, row + 1, col - 1, row + 2, col - 2))
                legalJumps.add(new Move(row, col, row + 2, col - 2));
            if (canJump(playerPawn, row - 1, col + 1, row - 2, col + 2))
                legalJumps.add(new Move(row, col, row - 2, col + 2));
            if (canJump(playerPawn, row - 1, col - 1, row - 2, col - 2))
                legalJumps.add(new Move(row, col, row - 2, col - 2));
        }
        return (legalJumps.isEmpty()) ? null : legalJumps;
    }

    private boolean canJump(FigureType playerFigure, int row2, int col2, int row3, int col3) {
        if (row3 < 0 || row3 >= ROW_COUNT || col3 < 0 || col3 >= COLUMN_COUNT) return false;
        if (board[row3][col3] != EMPTY) return false;
        if (playerFigure == WHITE_PAWN) {
            return board[row2][col2] == BLACK_PAWN;
        } else {
            return board[row2][col2] == WHITE_PAWN;
        }
    }

    private boolean canMove(FigureType playerFigure, int row1, int col1, int row2, int col2) {
        if (row2 < 0 || row2 >= ROW_COUNT || col2 < 0 || col2 >= COLUMN_COUNT) return false;
        if (board[row2][col2] != EMPTY) return false;
        if (playerFigure == WHITE_PAWN) {
            return (board[row1][col1] == WHITE_PAWN) && (row2 < row1);
        } else {
            return (board[row1][col1] == BLACK_PAWN) && (row2 > row1);
        }
    }
}
