package com.checkers.engine.strategy.ai;

import com.checkers.UIX.GameBoard;
import com.checkers.engine.board.Board;
import com.checkers.engine.move.Move;
import com.checkers.engine.move.MoveTransition;
import com.checkers.engine.players.Player.PlayerType;

import java.util.List;

import static com.checkers.engine.players.Player.PlayerType.BLACK;
import static com.checkers.engine.players.Player.PlayerType.WHITE;
import static com.checkers.engine.utils.EngineUtils.makeSimulatedMove;

public class MiniMax implements MoveStrategy {

    private final BoardEvaluator boardEvaluator;
    private final int depth;
    private long boardsEvaluated;

    public MiniMax(final int depth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.boardsEvaluated = 0;
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }

    @Override
    public Move execute(GameBoard gameBoard) {
        final long startTime = System.currentTimeMillis();

        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(gameBoard.getCurrentPlayer().getPlayerType() + " THINKING with depth = " + depth);
        int moveCounter = 1;
        int numMoves = gameBoard.getLegalMoves().size();

        for (final Move move : gameBoard.getLegalMoves()) {
            final MoveTransition moveTransition = makeSimulatedMove(gameBoard.getCurrentPlayer().getPlayerType(), gameBoard.getBoard(), move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentValue = gameBoard.getCurrentPlayer().getPlayerType() == WHITE ?
                        min(BLACK, moveTransition.getTransitionBoard(), depth - 1) :
                        max(WHITE, moveTransition.getTransitionBoard(), depth - 1);
                if (gameBoard.getCurrentPlayer().getPlayerType() == WHITE && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (gameBoard.getCurrentPlayer().getPlayerType() == BLACK && currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            } else {
                System.out.println("\t" + toString() + " can't execute move (" + moveCounter + "/" + numMoves + ") " + move);
            }
            moveCounter++;
        }
        long executionTime = System.currentTimeMillis() - startTime;
        System.out.printf("%s SELECTS %s [#boards = %d time taken = %d ms, rate = %.1f\n", gameBoard.getCurrentPlayer().getPlayerType(),
                bestMove, boardsEvaluated, executionTime, (1000 * ((double) boardsEvaluated / executionTime)));

        return bestMove;
    }

    public int min(final PlayerType playerType, final Board board, final int depth) {
        List<Move> legalMoves = board.calculateMovesOnBoard(playerType);
        if (depth == 0) {
            boardsEvaluated++;
            return boardEvaluator.evaluate(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : legalMoves) {
            final MoveTransition moveTransition = makeSimulatedMove(playerType, board, move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(WHITE, moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }

    public int max(final PlayerType playerType, final Board board, final int depth) {
        List<Move> legalMoves = board.calculateMovesOnBoard(playerType);
        if (depth == 0) {
            boardsEvaluated++;
            return boardEvaluator.evaluate(board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : legalMoves) {
            final MoveTransition moveTransition = makeSimulatedMove(playerType, board, move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(BLACK, moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }
}
