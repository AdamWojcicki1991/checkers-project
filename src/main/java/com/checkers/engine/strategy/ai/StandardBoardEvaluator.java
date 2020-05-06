package com.checkers.engine.strategy.ai;

import com.checkers.engine.board.Board;
import com.checkers.engine.figures.Figure;
import com.checkers.engine.move.Move;
import com.checkers.engine.players.Player.PlayerType;

import java.util.List;

import static com.checkers.engine.figures.Figure.FigureType.*;
import static com.checkers.engine.players.Player.PlayerType.BLACK;
import static com.checkers.engine.players.Player.PlayerType.WHITE;
import static com.checkers.engine.utils.EngineUtils.COLUMN_COUNT;
import static com.checkers.engine.utils.EngineUtils.ROW_COUNT;

public final class StandardBoardEvaluator implements BoardEvaluator {

    private static final int QUEEN_THREAT_BONUS = 400;
    private static final int PAWN_THREAT_BONUS = 100;
    private static final int PROMOTION_ROW_BONUS = 100;
    private static final int ENEMY_TERRITORY_BONUS = 20;
    private final static int MOBILITY_MULTIPLIER = 4;

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
        return figureValue(player, board) + attackThreats(player, board, depth) + mobility(player, board) +
                positionOnBoard(player, board) + enemyTerritory(player, board);
    }

    private static int figureValue(final PlayerType player, final Board board) {
        int figureValueScore = 0;
        for (final Figure figure : board.calculateFiguresOnBoard(player)) {
            figureValueScore += figure.getFigureType().getFigureValue();
        }
        return figureValueScore;
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
                        if (row == 0 && board.getBoardField(row, column).getFigure().getFigureType() == WHITE_PAWN) {
                            fieldValueScore += BOARD_FIELD_VALUE[row][column] + PROMOTION_ROW_BONUS;
                        } else {
                            fieldValueScore += BOARD_FIELD_VALUE[row][column];
                        }
                    } else {
                        if (row == 9 && board.getBoardField(row, column).getFigure().getFigureType() == BLACK_PAWN) {
                            fieldValueScore += BOARD_FIELD_VALUE[row][column] + PROMOTION_ROW_BONUS;
                        } else {
                            fieldValueScore += BOARD_FIELD_VALUE[row][column];
                        }
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

    private static int attackThreats(final PlayerType player, final Board board, final int depth) {
        int attackPawnThreatScore = 0;
        int attackQueenThreatScore = 0;
        List<Move> legalMoves = board.calculateMovesOnBoard(player);
        for (Move attackMove : legalMoves) {
            if (attackMove.isPawnAttackMove()) {
                attackPawnThreatScore++;
            } else if (attackMove.isQueenAttackMove()) {
                attackQueenThreatScore++;
            }
        }
        return (attackPawnThreatScore * PAWN_THREAT_BONUS + attackQueenThreatScore * QUEEN_THREAT_BONUS) * depthBonus(depth);
    }

    private static int depthBonus(final int depth) {
        return depth == 0 ? 1 : 100 * depth;
    }
}
