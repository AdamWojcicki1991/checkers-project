package com.checkers.engine.utils;

import com.checkers.engine.board.BoardField;
import com.checkers.engine.figures.FigureType;
import com.checkers.engine.playres.PlayerType;

import static com.checkers.engine.figures.FigureType.*;
import static com.checkers.engine.playres.PlayerType.BLACK;
import static com.checkers.engine.playres.PlayerType.WHITE;

public interface EngineUtils {
    int BOARD_FIELD_SIZE = 70;
    int ROW_COUNT = 10;
    int COLUMN_COUNT = 10;

    static boolean isDestinationInvalid(int destinationRow, int destinationColumn) {
        return destinationRow < 0 || destinationRow >= ROW_COUNT || destinationColumn < 0 || destinationColumn >= COLUMN_COUNT;
    }

    static boolean isFigureAllianceValid(PlayerType player, BoardField[][] board, int row, int col, FigureType blackPawn, FigureType blackQueen, FigureType whitePawn, FigureType whiteQueen) {
        if (!board[row][col].isBoardFieldOccupied()) return false;
        if (board[row][col].isJumped()) return false;
        if (player == WHITE && (board[row][col].getFigure().getFigureType() == blackPawn ||
                board[row][col].getFigure().getFigureType() == blackQueen)) {
            return true;
        } else return player == BLACK && (board[row][col].getFigure().getFigureType() == whitePawn ||
                board[row][col].getFigure().getFigureType() == whiteQueen);
    }

    static boolean isJumpValid(FigureType playerFigure, BoardField[][] board, int jumpedRow, int jumpedColumn, int destinationRow, int destinationColumn) {
        if (isDestinationInvalid(destinationRow, destinationColumn)) return false;
        if (board[jumpedRow][jumpedColumn].isJumped()) return false;
        if (board[destinationRow][destinationColumn].isJumped()) return false;
        if (board[destinationRow][destinationColumn].isBoardFieldOccupied()) return false;
        if (!board[jumpedRow][jumpedColumn].isBoardFieldOccupied()) return false;
        if (playerFigure == WHITE_PAWN || playerFigure == WHITE_QUEEN) {
            return board[jumpedRow][jumpedColumn].getFigure().getFigureType() == BLACK_PAWN ||
                    board[jumpedRow][jumpedColumn].getFigure().getFigureType() == BLACK_QUEEN;
        } else {
            return board[jumpedRow][jumpedColumn].getFigure().getFigureType() == WHITE_PAWN ||
                    board[jumpedRow][jumpedColumn].getFigure().getFigureType() == WHITE_QUEEN;
        }
    }

    static boolean isMoveValid(FigureType playerFigure, BoardField[][] board, int initialRow, int initialColumn, int destinationRow, int destinationColumn) {
        if (isDestinationInvalid(destinationRow, destinationColumn)) return false;
        if (board[destinationRow][destinationColumn].isBoardFieldOccupied()) return false;
        if (!board[initialRow][initialColumn].isBoardFieldOccupied()) return false;
        if (playerFigure == WHITE_PAWN) {
            return (board[initialRow][initialColumn].getFigure().getFigureType() == WHITE_PAWN) && (destinationRow < initialRow);
        } else if (playerFigure == BLACK_PAWN) {
            return (board[initialRow][initialColumn].getFigure().getFigureType() == BLACK_PAWN) && (destinationRow > initialRow);
        } else if (playerFigure == WHITE_QUEEN) {
            return board[initialRow][initialColumn].getFigure().getFigureType() == WHITE_QUEEN;
        } else {
            return board[initialRow][initialColumn].getFigure().getFigureType() == BLACK_QUEEN;
        }
    }
}
