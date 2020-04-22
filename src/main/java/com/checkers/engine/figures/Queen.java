package com.checkers.engine.figures;

import com.checkers.engine.board.BoardField;
import com.checkers.engine.board.Move;
import com.checkers.engine.playres.PlayerType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.checkers.engine.board.Move.QueenAttackMove;
import static com.checkers.engine.board.Move.QueenMajorMove;
import static com.checkers.engine.figures.FigureType.*;
import static com.checkers.engine.utils.EngineUtils.*;

public class Queen extends Figure {

    public Queen(FigureType figureType, int row, int column) {
        super(figureType, row, column);
    }

    @Override
    public List<Move> calculateLegalMove(final BoardField[][] board, final FigureType playerFigure, final PlayerType playerType) {
        final List<Move> legalMoves = new ArrayList<>();
        calculateAttackMove(playerType, playerFigure, board, 1, 1, legalMoves);
        calculateAttackMove(playerType, playerFigure, board, -1, -1, legalMoves);
        calculateAttackMove(playerType, playerFigure, board, -1, 1, legalMoves);
        calculateAttackMove(playerType, playerFigure, board, 1, -1, legalMoves);
        if (legalMoves.isEmpty()) {
            calculateMajorMove(board, playerFigure, 1, 1, legalMoves);
            calculateMajorMove(board, playerFigure, -1, -1, legalMoves);
            calculateMajorMove(board, playerFigure, -1, 1, legalMoves);
            calculateMajorMove(board, playerFigure, 1, -1, legalMoves);
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public List<Move> calculateNextJump(final BoardField[][] board, final FigureType playerFigure, final PlayerType playerType, final int destinationRow, final int destinationColumn) {
        final List<Move> nextJumps = new ArrayList<>();
        calculateNextAttackMove(playerType, playerFigure, board, 1, 1, destinationRow, destinationColumn, nextJumps);
        calculateNextAttackMove(playerType, playerFigure, board, -1, -1, destinationRow, destinationColumn, nextJumps);
        calculateNextAttackMove(playerType, playerFigure, board, -1, 1, destinationRow, destinationColumn, nextJumps);
        calculateNextAttackMove(playerType, playerFigure, board, 1, -1, destinationRow, destinationColumn, nextJumps);
        return Collections.unmodifiableList(nextJumps);
    }

    @Override
    public String toString() {
        return figureType.toString();
    }

    private void calculateAttackMove(PlayerType playerType, FigureType playerFigure, BoardField[][] board, int moveRow, int moveColumn, List<Move> jumpMoves) {
        calculateAttack(playerType, playerFigure, board, moveRow, moveColumn, row, column, jumpMoves);
    }

    private void calculateNextAttackMove(PlayerType playerType, FigureType playerFigure, BoardField[][] board, int moveRow, int moveColumn, int destinationRow, int destinationColumn, List<Move> nextJumpMoves) {
        calculateAttack(playerType, playerFigure, board, moveRow, moveColumn, destinationRow, destinationColumn, nextJumpMoves);
    }

    private void calculateAttack(PlayerType playerType, FigureType playerFigure, BoardField[][] board, int moveRow, int moveColumn, int initialRow, int initialColumn, List<Move> nextJumpMoves) {
        int calculateRow = initialRow + moveRow, calculateColumn = initialColumn + moveColumn, enemyDestinationRow = -1, enemyDestinationCol = -1;
        while (!isDestinationInvalid(calculateRow, calculateColumn)) {
            if (hasAllianceFigure(playerType, board, calculateRow, calculateColumn)) break;
            if (isJumpValid(playerFigure, board, calculateRow, calculateColumn, calculateRow + moveRow, calculateColumn + moveColumn)) {
                enemyDestinationRow = calculateRow;
                enemyDestinationCol = calculateColumn;
                nextJumpMoves.add(new QueenAttackMove(initialRow, initialColumn,
                        calculateRow + moveRow, calculateColumn + moveColumn,
                        enemyDestinationRow, enemyDestinationCol));
            }
            if (hasEnemyFigure(playerType, board, calculateRow, calculateColumn)) {
                while (canMoveBehindFigure(board, calculateRow + moveRow, calculateColumn + moveColumn)) {
                    nextJumpMoves.add(new QueenAttackMove(initialRow, initialColumn,
                            calculateRow + moveRow, calculateColumn + moveColumn,
                            enemyDestinationRow, enemyDestinationCol));
                    calculateRow = calculateRow + moveRow;
                    calculateColumn = calculateColumn + moveColumn;
                }
                break;
            }
            calculateRow = calculateRow + moveRow;
            calculateColumn = calculateColumn + moveColumn;
        }
    }

    private void calculateMajorMove(BoardField[][] board, FigureType playerFigure, int moveRow, int moveColumn, List<Move> legalMoves) {
        int calculateRow = row + moveRow, calculateCol = column + moveColumn;
        while (isMoveValid(playerFigure, board, row, column, calculateRow, calculateCol)) {
            legalMoves.add(new QueenMajorMove(row, column, calculateRow, calculateCol));
            calculateRow = calculateRow + moveRow;
            calculateCol = calculateCol + moveColumn;
        }
    }

    private boolean canMoveBehindFigure(BoardField[][] board, int destinationRow, int destinationColumn) {
        if (isDestinationInvalid(destinationRow, destinationColumn)) return false;
        return !board[destinationRow][destinationColumn].isBoardFieldOccupied();
    }

    private boolean hasEnemyFigure(PlayerType player, BoardField[][] board, int row, int col) {
        return isFigureAllianceValid(player, board, row, col, BLACK_PAWN, BLACK_QUEEN, WHITE_PAWN, WHITE_QUEEN);
    }

    private boolean hasAllianceFigure(PlayerType player, BoardField[][] board, int row, int col) {
        return isFigureAllianceValid(player, board, row, col, WHITE_PAWN, WHITE_QUEEN, BLACK_PAWN, BLACK_QUEEN);
    }
}
