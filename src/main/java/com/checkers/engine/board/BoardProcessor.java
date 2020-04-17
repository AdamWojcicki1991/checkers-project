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

    public FigureType getFigure(int row, int col) {
        return board[row][col];
    }

    public void executeMove(Move move) {
        board[move.destinationRow][move.destinationColumn] = board[move.initialRow][move.initialColumn];
        board[move.initialRow][move.initialColumn] = EMPTY;
        if (move.initialRow - move.destinationRow == 2 || move.initialRow - move.destinationRow == -2) {
            int jumpedRow = (move.initialRow + move.destinationRow) / 2;
            int jumpedCol = (move.initialColumn + move.destinationColumn) / 2;
            board[jumpedRow][jumpedCol] = EMPTY;
        }
    }

    public void pawnPromotion(Move move, List<Move> calculatedJumps) {
        if (move.isJump() && calculatedJumps.isEmpty()) {
            executePromotion(move);
        } else if (move.isNormal()) {
            executePromotion(move);
        }
    }

    public List<Move> calculateLegalMoves(PlayerType player) {
        List<Move> legalMoves = new ArrayList<>();
        if (player != WHITE && player != BLACK) return legalMoves;
        FigureType playerPawn, playerQueen;
        if (player == WHITE) {
            playerPawn = WHITE_PAWN;
            playerQueen = WHITE_QUEEN;
        } else {
            playerPawn = BLACK_PAWN;
            playerQueen = BLACK_QUEEN;
        }
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COLUMN_COUNT; col++) {
                if (board[row][col] == playerPawn) {
                    if (canPawnJump(playerPawn, row + 1, col + 1, row + 2, col + 2))
                        legalMoves.add(new Move(row, col, row + 2, col + 2));
                    if (canPawnJump(playerPawn, row + 1, col - 1, row + 2, col - 2))
                        legalMoves.add(new Move(row, col, row + 2, col - 2));
                    if (canPawnJump(playerPawn, row - 1, col + 1, row - 2, col + 2))
                        legalMoves.add(new Move(row, col, row - 2, col + 2));
                    if (canPawnJump(playerPawn, row - 1, col - 1, row - 2, col - 2))
                        legalMoves.add(new Move(row, col, row - 2, col - 2));
                } else if (board[row][col] == playerQueen) {
                    int incrementRow = row + 1, incrementCol = col + 1, decrementRow = row - 1, decrementCol = col - 1;
                    while (canQueenJump(playerQueen, incrementRow, incrementCol, incrementRow + 1, incrementCol + 1)) {
                        legalMoves.add(new Move(incrementRow, incrementCol, incrementRow + 1, incrementCol + 1));
                        incrementRow++;
                        incrementCol++;
                    }
                    while (canQueenJump(playerQueen, decrementRow, decrementCol, decrementRow - 1, decrementCol - 1)) {
                        legalMoves.add(new Move(decrementRow, decrementCol, decrementRow - 1, decrementCol - 1));
                        decrementRow--;
                        decrementCol--;
                    }
                    incrementRow = row + 1;
                    incrementCol = col + 1;
                    decrementRow = row - 1;
                    decrementCol = col - 1;
                    while (canQueenJump(playerQueen, decrementRow, incrementCol, decrementRow - 1, incrementCol + 1)) {
                        legalMoves.add(new Move(decrementRow, incrementCol, decrementRow - 1, incrementCol + 1));
                        decrementRow--;
                        incrementCol++;
                    }
                    while (canQueenJump(playerQueen, incrementRow, decrementCol, incrementRow + 1, decrementCol - 1)) {
                        legalMoves.add(new Move(incrementRow, decrementCol, incrementRow + 1, decrementCol - 1));
                        incrementRow++;
                        decrementCol--;
                    }
                }
            }
        }
        if (legalMoves.size() == 0) {
            for (int row = 0; row < ROW_COUNT; row++) {
                for (int col = 0; col < COLUMN_COUNT; col++) {
                    if (board[row][col] == playerPawn) {
                        if (canPawnMove(playerPawn, row, col, row + 1, col + 1))
                            legalMoves.add(new Move(row, col, row + 1, col + 1));
                        if (canPawnMove(playerPawn, row, col, row + 1, col - 1))
                            legalMoves.add(new Move(row, col, row + 1, col - 1));
                        if (canPawnMove(playerPawn, row, col, row - 1, col + 1))
                            legalMoves.add(new Move(row, col, row - 1, col + 1));
                        if (canPawnMove(playerPawn, row, col, row - 1, col - 1))
                            legalMoves.add(new Move(row, col, row - 1, col - 1));
                    } else if (board[row][col] == playerQueen) {
                        int incrementRow = row + 1, incrementCol = col + 1, decrementRow = row - 1, decrementCol = col - 1;
                        while (canQueenMove(playerQueen, row, col, incrementRow, incrementCol)) {
                            legalMoves.add(new Move(row, col, incrementRow, incrementCol));
                            incrementRow++;
                            incrementCol++;
                        }
                        while (canQueenMove(playerQueen, row, col, decrementRow, decrementCol)) {
                            legalMoves.add(new Move(row, col, decrementRow, decrementCol));
                            decrementRow--;
                            decrementCol--;
                        }
                        incrementRow = row + 1;
                        incrementCol = col + 1;
                        decrementRow = row - 1;
                        decrementCol = col - 1;
                        while (canQueenMove(playerQueen, row, col, decrementRow, incrementCol)) {
                            legalMoves.add(new Move(row, col, decrementRow, incrementCol));
                            decrementRow--;
                            incrementCol++;
                        }
                        while (canQueenMove(playerQueen, row, col, incrementRow, decrementCol)) {
                            legalMoves.add(new Move(row, col, incrementRow, decrementCol));
                            incrementRow++;
                            decrementCol--;
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    public List<Move> calculateNextJump(PlayerType player, int destinationRow, int destinationColumn) {
        List<Move> nextJump = new ArrayList<>();
        if (player != WHITE && player != BLACK) return nextJump;
        FigureType playerPawn, playerQueen;
        if (player == WHITE) {
            playerPawn = WHITE_PAWN;
            playerQueen = WHITE_QUEEN;
        } else {
            playerPawn = BLACK_PAWN;
            playerQueen = BLACK_QUEEN;
        }
        if (board[destinationRow][destinationColumn] == playerPawn) {
            if (canPawnJump(playerPawn, destinationRow + 1, destinationColumn + 1, destinationRow + 2, destinationColumn + 2))
                nextJump.add(new Move(destinationRow, destinationColumn, destinationRow + 2, destinationColumn + 2));
            if (canPawnJump(playerPawn, destinationRow + 1, destinationColumn - 1, destinationRow + 2, destinationColumn - 2))
                nextJump.add(new Move(destinationRow, destinationColumn, destinationRow + 2, destinationColumn - 2));
            if (canPawnJump(playerPawn, destinationRow - 1, destinationColumn + 1, destinationRow - 2, destinationColumn + 2))
                nextJump.add(new Move(destinationRow, destinationColumn, destinationRow - 2, destinationColumn + 2));
            if (canPawnJump(playerPawn, destinationRow - 1, destinationColumn - 1, destinationRow - 2, destinationColumn - 2))
                nextJump.add(new Move(destinationRow, destinationColumn, destinationRow - 2, destinationColumn - 2));
        } else if (board[destinationRow][destinationColumn] == playerQueen) {

        }
        return nextJump;
    }

    private void executePromotion(Move move) {
        if (move.destinationRow == 0 && board[move.destinationRow][move.destinationColumn] == WHITE_PAWN)
            board[move.destinationRow][move.destinationColumn] = WHITE_QUEEN;
        if (move.destinationRow == 9 && board[move.destinationRow][move.destinationColumn] == BLACK_PAWN)
            board[move.destinationRow][move.destinationColumn] = BLACK_QUEEN;
    }

    private boolean canPawnJump(FigureType playerFigure, int jumpedRow, int jumpedColumn, int destinationRow, int destinationColumn) {
        if (isValidDestination(destinationRow, destinationColumn)) return false;
        if (board[destinationRow][destinationColumn] != EMPTY) return false;
        if (playerFigure == WHITE_PAWN) {
            return board[jumpedRow][jumpedColumn] == BLACK_PAWN || board[jumpedRow][jumpedColumn] == BLACK_QUEEN;
        } else {
            return board[jumpedRow][jumpedColumn] == WHITE_PAWN || board[jumpedRow][jumpedColumn] == WHITE_QUEEN;
        }
    }

    private boolean canPawnMove(FigureType playerFigure, int initialRow, int initialColumn, int destinationRow, int destinationColumn) {
        if (isValidDestination(destinationRow, destinationColumn)) return false;
        if (board[destinationRow][destinationColumn] != EMPTY) return false;
        if (playerFigure == WHITE_PAWN) {
            return (board[initialRow][initialColumn] == WHITE_PAWN) && (destinationRow < initialRow);
        } else {
            return (board[initialRow][initialColumn] == BLACK_PAWN) && (destinationRow > initialRow);
        }
    }

    private boolean canQueenJump(FigureType playerFigure, int jumpedRow, int jumpedColumn, int destinationRow, int destinationColumn) {
        if (isValidDestination(destinationRow, destinationColumn)) return false;
        if (board[destinationRow][destinationColumn] != EMPTY) return false;
        if (board[jumpedRow][jumpedColumn].getColor().equals(playerFigure.getColor())) {
            return false;
        } else {
            if (playerFigure == WHITE_QUEEN) {
                return board[jumpedRow][jumpedColumn] == BLACK_PAWN || board[jumpedRow][jumpedColumn] == BLACK_QUEEN;
            } else {
                return board[jumpedRow][jumpedColumn] == WHITE_PAWN || board[jumpedRow][jumpedColumn] == WHITE_QUEEN;
            }
        }
    }

    private boolean canQueenMove(FigureType playerFigure, int initialRow, int initialColumn, int destinationRow, int destinationColumn) {
        if (isValidDestination(destinationRow, destinationColumn)) return false;
        if (board[destinationRow][destinationColumn] != EMPTY) return false;
        if (playerFigure == WHITE_QUEEN) {
            return (board[initialRow][initialColumn] == WHITE_QUEEN);
        } else {
            return (board[initialRow][initialColumn] == BLACK_QUEEN);
        }
    }

    private boolean isValidDestination(int destinationRow, int destinationColumn) {
        return destinationRow < 0 || destinationRow >= ROW_COUNT || destinationColumn < 0 || destinationColumn >= COLUMN_COUNT;
    }
}
