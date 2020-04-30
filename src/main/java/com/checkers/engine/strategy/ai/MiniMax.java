package com.checkers.engine.strategy.ai;

import com.checkers.UIX.CheckersBoard;
import com.checkers.engine.board.Board;
import com.checkers.engine.move.Move;
import com.checkers.engine.move.MoveTransition;

public class MiniMax implements MoveStrategy {

    private final BoardEvaluator boardEvaluator;
    private final int depth;
    private final long boardsEvaluated;

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
    public Move execute(CheckersBoard board) {

        final long startTime = System.currentTimeMillis();

        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.getCurrentPlayer().getPlayerType() + " THINKING with depth = " + depth);
        int moveCounter = 1;
        int numMoves = board.getCurrentPlayer().getLegalMoves().size();

        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentValue = board.getCurrentPlayer().getPlayerType().isWhite() ?
                        min(board, moveTransition.getTransitionBoard(), depth - 1) :
                        max(board, moveTransition.getTransitionBoard(), depth - 1);

                if (board.getCurrentPlayer().getPlayerType().isWhite() && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.getCurrentPlayer().getPlayerType().isBlack() && currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            } else {
                System.out.println("\t" + toString() + " can't execute move (" + moveCounter + "/" + numMoves + ") " + move);
            }
            moveCounter++;
        }
        long executionTime = System.currentTimeMillis() - startTime;
        System.out.printf("%s SELECTS %s [#boards = %d time taken = %d ms, rate = %.1f\n", board.getCurrentPlayer().getPlayerType(),
                bestMove, boardsEvaluated, executionTime, (1000 * ((double) boardsEvaluated / executionTime)));

        return bestMove;
    }

    public int min(final CheckersBoard checkersBoard, final Board board, final int depth) {
        if (depth == 0) {
            return boardEvaluator.evaluate(checkersBoard, board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : checkersBoard.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = checkersBoard.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(checkersBoard, moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }

    public int max(final CheckersBoard checkersBoard, final Board board, final int depth) {
        if (depth == 0) {
            return boardEvaluator.evaluate(checkersBoard, board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : checkersBoard.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = checkersBoard.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(checkersBoard, moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }
}
