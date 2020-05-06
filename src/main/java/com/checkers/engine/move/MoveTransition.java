package com.checkers.engine.move;

import com.checkers.engine.board.Board;
import com.checkers.engine.move.Move.MoveStatus;
import com.checkers.engine.players.Player.PlayerType;

import java.util.List;

import static com.checkers.engine.players.Player.PlayerType.BLACK;
import static com.checkers.engine.players.Player.PlayerType.WHITE;

public class MoveTransition {
    private final PlayerType currentPlayer;
    private PlayerType opponentPlayer;
    private final Board fromBoard;
    private final Move transitionMove;
    private final MoveStatus moveStatus;
    private final Board transitionBoard;

    public MoveTransition(final PlayerType currentPlayer, final Board fromBoard, final Move transitionMove, MoveStatus moveStatus) {
        this.currentPlayer = currentPlayer;
        this.opponentPlayer = calculateOpponentPlayer();
        this.fromBoard = fromBoard;
        this.transitionMove = transitionMove;
        this.moveStatus = moveStatus;
        this.transitionBoard = calculateTransitionBoard();
    }

    private Board calculateTransitionBoard() {
        Board transitionBoard = new Board(fromBoard);
        if (transitionMove.isPawnMajorMove() || transitionMove.isQueenMajorMove()) {
            transitionBoard.executeMove(transitionMove);
            transitionBoard.pawnPromotion(transitionMove, null);
        } else if (transitionMove.isPawnAttackMove() || transitionMove.isQueenAttackMove()) {
            transitionBoard.executeJump(transitionMove);
            List<Move> legalJumps = transitionBoard.calculateNextJumpOnBoard(currentPlayer, transitionMove.destinationRow, transitionMove.destinationColumn);
            transitionBoard.pawnPromotion(transitionMove, legalJumps);
            transitionBoard.killFigure(transitionMove);
        }
        return transitionBoard;
    }

    private PlayerType calculateOpponentPlayer() {
        if (currentPlayer == WHITE) {
            return opponentPlayer = BLACK;
        } else {
            return opponentPlayer = WHITE;
        }
    }

    public PlayerType getOpponentPlayer() {
        return opponentPlayer;
    }

    public MoveStatus getMoveStatus() {
        return moveStatus;
    }

    public Board getTransitionBoard() {
        return transitionBoard;
    }

    @Override
    public String toString() {
        return "MoveTransition{" +
                "currentPlayer=" + currentPlayer +
                ", opponentPlayer=" + opponentPlayer +
                ", fromBoard=" + fromBoard +
                ", transitionMove=" + transitionMove +
                ", moveStatus=" + moveStatus +
                ", transitionBoard=" + transitionBoard +
                '}';
    }
}
