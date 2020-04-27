package com.checkers.engine.strategy.ai;

import com.checkers.UIX.CheckersBoard;
import com.checkers.engine.board.Board;
import com.checkers.engine.figures.Figure;
import com.checkers.engine.playres.Player;

import static com.checkers.engine.utils.EngineUtils.COLUMN_COUNT;
import static com.checkers.engine.utils.EngineUtils.ROW_COUNT;

public final class StandardBoardEvaluator implements BoardEvaluator {

    private static final int ENEMY_TERRITORY_BONUS = 10;

    private static final int[][] initializeFieldValues() {
        return new int[][]{
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
    }

    @Override
    public int evaluate(CheckersBoard checkersBoard, Board board, int depth) {
        return scorePlayer(board, checkersBoard.getWhitePlayer(), depth) - scorePlayer(board, checkersBoard.getBlackPlayer(), depth);
    }

    private int scorePlayer(final Board board, final Player player, final int depth) {
        return figureValue(player) + mobility(player) + positionOnBoard(board) + enemyTerritory(player);
    }

    private static int figureValue(final Player player) {
        int figureValueScore = 0;
        for (final Figure figure : player.getActiveFigures()) {
            figureValueScore += figure.getFigureType().getFigureValue();
        }
        return figureValueScore;
    }

    private static int mobility(final Player player) {
        return player.getLegalMoves().size();
    }

    private static int positionOnBoard(Board board) {
        int[][] fieldValues = initializeFieldValues();
        int fieldValueScore = 0;
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int column = 0; column < COLUMN_COUNT; column++) {
                if (board.getBoardField(row, column).isBoardFieldOccupied()) {
                    fieldValueScore += fieldValues[row][column];
                }
            }
        }
        return fieldValueScore;
    }

    private static int enemyTerritory(Player player) {
        int figureOnEnemyTerritoryScore = 0;
        for (final Figure figure : player.getActiveFigures()) {
            if (player.getPlayerType().isWhite() && figure.getRow() <= 4) {
                figureOnEnemyTerritoryScore += ENEMY_TERRITORY_BONUS;
            } else if (player.getPlayerType().isBlack() && figure.getRow() >= 5) {
                figureOnEnemyTerritoryScore += ENEMY_TERRITORY_BONUS;
            }
        }
        return figureOnEnemyTerritoryScore;
    }
}