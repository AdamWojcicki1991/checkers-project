package com.checkers.engine.strategy.ai;

import com.checkers.controler.GameBoard;
import com.checkers.engine.board.Board;
import com.checkers.engine.move.Move;
import com.checkers.engine.move.MoveTransition;
import com.checkers.engine.players.Player.PlayerType;

import java.util.List;

import static com.checkers.engine.players.Player.PlayerType.BLACK;
import static com.checkers.engine.players.Player.PlayerType.WHITE;

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

        int numMoves = gameBoard.getLegalMoves().size();
        System.out.println("\n" + gameBoard.getCurrentPlayer().getPlayerType() + ", Legal moves: " + numMoves + ", THINKING with depth = " + depth + "\n");

        int moveCounter = 1;
        for (final Move move : gameBoard.getLegalMoves()) {
            final MoveTransition moveTransition = makeSimulatedMove(gameBoard.getCurrentPlayer().getPlayerType(), gameBoard.getBoard(), move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentValue = gameBoard.getCurrentPlayer().getPlayerType() == WHITE ?
                        min(moveTransition.getTransitionBoard(), depth - 1) :
                        max(moveTransition.getTransitionBoard(), depth - 1);
                if (gameBoard.getCurrentPlayer().getPlayerType() == WHITE && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    System.out.println(" Score for " + moveTransition.getOpponentPlayer().toString() + " " + highestSeenValue + " for move " + move);
                    bestMove = move;
                } else if (gameBoard.getCurrentPlayer().getPlayerType() == BLACK && currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    System.out.println(" Score for " + moveTransition.getOpponentPlayer().toString() + " " + lowestSeenValue + " for move " + move);
                    bestMove = move;
                }
            } else {
                System.out.println("\t" + toString() + " can't execute move (" + moveCounter + "/" + numMoves + ") " + move);
            }
            moveCounter++;
        }
        long executionTime = System.currentTimeMillis() - startTime;
        System.out.printf("\n%s SELECTS %s [#boards = %d time taken = %d ms, rate = %.1f\n", gameBoard.getCurrentPlayer().getPlayerType(),
                          bestMove, boardsEvaluated, executionTime, (1000 * ((double) boardsEvaluated / executionTime)));

        return bestMove;
    }

    private MoveTransition makeSimulatedMove(final PlayerType playerType, final Board currentBoard, final Move move) {
        List<Move> legalMoves = currentBoard.calculateMovesOnBoard(playerType);
        if (legalMoves.isEmpty() || !legalMoves.contains(move)) {
            return new MoveTransition(playerType, currentBoard, move, Move.MoveStatus.ILLEGAL_MOVE);
        } else {
            return new MoveTransition(playerType, currentBoard, move, Move.MoveStatus.DONE);
        }
    }

    private int min(final Board board, final int depth) {
        List<Move> legalMoves = board.calculateMovesOnBoard(BLACK);
        if (depth == 0 || legalMoves.isEmpty()) {
            boardsEvaluated++;
            return boardEvaluator.evaluate(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : legalMoves) {
            final MoveTransition moveTransition = makeSimulatedMove(BLACK, board, move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }

    private int max(final Board board, final int depth) {
        List<Move> legalMoves = board.calculateMovesOnBoard(WHITE);
        if (depth == 0 || legalMoves.isEmpty()) {
            boardsEvaluated++;
            return boardEvaluator.evaluate(board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : legalMoves) {
            final MoveTransition moveTransition = makeSimulatedMove(WHITE, board, move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }
}
