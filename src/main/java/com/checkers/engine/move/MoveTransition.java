package com.checkers.engine.move;

import com.checkers.engine.board.Board;
import com.checkers.engine.move.Move.MoveStatus;

public class MoveTransition {
    private final Board fromBoard;
    private final Move transitionMove;
    private final MoveStatus moveStatus;
    private final Board transitionBoard;

    public MoveTransition(final Board fromBoard, final Move transitionMove, MoveStatus moveStatus) {
        this.fromBoard = fromBoard;
        this.transitionMove = transitionMove;
        this.moveStatus = moveStatus;
        this.transitionBoard = calculateTransitionBoard();
    }

    private Board calculateTransitionBoard() {
        Board transitionBoard = new Board(fromBoard);
        if (transitionMove.isPawnMajorMove() || transitionMove.isQueenMajorMove()) {
            transitionBoard.executeMove(transitionMove);
        } else if (transitionMove.isPawnAttackMove() || transitionMove.isQueenAttackMove()) {
            transitionBoard.executeJump(transitionMove);
            transitionBoard.killFigure(transitionMove);
        }
        return transitionBoard;
    }

    public Board getFromBoard() {
        return this.fromBoard;
    }

    public Board getTransitionBoard() {
        return this.transitionBoard;
    }

    public Move getTransitionMove() {
        return this.transitionMove;
    }

    public MoveStatus getMoveStatus() {
        return moveStatus;
    }

    @Override
    public String toString() {
        return "MoveTransition{" +
                "fromBoard=" + fromBoard +
                ", transitionMove=" + transitionMove +
                ", moveStatus=" + moveStatus +
                ", transitionBoard=" + transitionBoard +
                '}';
    }
}
