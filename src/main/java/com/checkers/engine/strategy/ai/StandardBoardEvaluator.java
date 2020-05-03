package com.checkers.engine.strategy.ai;

import com.checkers.engine.board.Board;
import com.checkers.engine.figures.Figure;
import com.checkers.engine.move.Move;
import com.checkers.engine.players.Player.PlayerType;

import static com.checkers.engine.figures.Figure.FigureType.WHITE_PAWN;
import static com.checkers.engine.figures.Figure.FigureType.WHITE_QUEEN;
import static com.checkers.engine.players.Player.PlayerType.BLACK;
import static com.checkers.engine.players.Player.PlayerType.WHITE;
import static com.checkers.engine.utils.EngineUtils.COLUMN_COUNT;
import static com.checkers.engine.utils.EngineUtils.ROW_COUNT;

public final class StandardBoardEvaluator implements BoardEvaluator {

    private static final int ENEMY_TERRITORY_BONUS = 20;
    private final static int MOBILITY_MULTIPLIER = 2;
    private final static int ATTACK_MULTIPLIER = 2;

    private final static int[][] BOARD_FIELD_VALUE = {
            {0, 5, 0, 5, 0, 5, 0, 5, 0, 5},
            {5, 0, 4, 0, 4, 0, 4, 0, 4, 0},
            {0, 4, 0, 3, 0, 3, 0, 3, 0, 5},
            {5, 0, 3, 0, 2, 0, 2, 0, 3, 0},
            {0, 4, 0, 2, 0, 1, 0, 3, 0, 5},
            {5, 0, 3, 0, 1, 0, 2, 0, 4, 0},
            {0, 4, 0, 2, 0, 2, 0, 3, 0, 5},
            {5, 0, 3, 0, 3, 0, 3, 0, 4, 0},
            {0, 4, 0, 3, 0, 4, 0, 4, 0, 5},
            {5, 0, 5, 0, 5, 0, 5, 0, 5, 0}
    };

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(WHITE, board, depth) - scorePlayer(BLACK, board, depth);
    }

    private int scorePlayer(final PlayerType player, final Board board, final int depth) {
        return figureValue(player, board) + attacks(player, board) + mobility(player, board) +
                positionOnBoard(player, board) + enemyTerritory(player, board);
    }

    private static int figureValue(final PlayerType player, final Board board) {
        int figureValueScore = 0;
        for (final Figure figure : board.calculateFiguresOnBoard(player)) {
            figureValueScore += figure.getFigureType().getFigureValue();
        }
        return figureValueScore;
    }

    private static int attacks(final PlayerType player, final Board board) {
        int attackScore = 0;
        for (final Move move : board.calculateMovesOnBoard(player)) {
            if (move.isPawnAttackMove() || move.isQueenAttackMove()) {
                attackScore++;
            }
        }
        return attackScore * ATTACK_MULTIPLIER;
    }

    private static int mobility(final PlayerType player, final Board board) {
        return board.calculateMovesOnBoard(player).size() * MOBILITY_MULTIPLIER;
    }

    private static int positionOnBoard(final PlayerType player, final Board board) {
        int fieldValueScore = 0;
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int column = 0; column < COLUMN_COUNT; column++) {
                if (board.getBoardField(row, column).isBoardFieldOccupied()) {
                    if (player == WHITE && board.getBoardField(row, column).getFigure().getFigureType() == WHITE_PAWN ||
                            board.getBoardField(row, column).getFigure().getFigureType() == WHITE_QUEEN) {
                        fieldValueScore += BOARD_FIELD_VALUE[row][column];
                    } else {
                        fieldValueScore += BOARD_FIELD_VALUE[row][column];
                    }
                }
            }
        }
        return fieldValueScore;
    }

    private static int enemyTerritory(final PlayerType player, final Board board) {
        int figureOnEnemyTerritoryScore = 0;
        for (final Figure figure : board.calculateFiguresOnBoard(player)) {
            if (player == WHITE && figure.getRow() <= 4) {
                figureOnEnemyTerritoryScore += ENEMY_TERRITORY_BONUS;
            } else if (player == BLACK && figure.getRow() >= 5) {
                figureOnEnemyTerritoryScore += ENEMY_TERRITORY_BONUS;
            }
        }
        return figureOnEnemyTerritoryScore;
    }
}
