package com.checkers.engine.playres;

import com.checkers.engine.board.Board;
import com.checkers.engine.figures.Figure;
import com.checkers.engine.move.Move;
import com.checkers.engine.move.Move.MoveStatus;
import com.checkers.engine.move.MoveTransition;

import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final List<Move> legalMoves;

    Player(final Board board) {
        this.board = board;
        this.legalMoves = board.calculateMovesOnBoard(getPlayerType());
    }

    public MoveTransition makeMove(final Move move) {
        if (!legalMoves.contains(move)) {
            return new MoveTransition(board, move, MoveStatus.ILLEGAL_MOVE);
        } else {
            return new MoveTransition(board, move, MoveStatus.DONE);
        }
    }

    public MoveTransition unMakeMove(final Move move) {
        return new MoveTransition(board, move.undo(), MoveStatus.DONE);
    }

    public List<Move> getLegalMoves() {
        return this.legalMoves;
    }

    public enum PlayerType {
        WHITE {
            @Override
            public boolean isWhite() {
                return true;
            }

            @Override
            public boolean isBlack() {
                return false;
            }

            @Override
            public String toString() {
                return "White Player";
            }
        },
        BLACK {
            @Override
            public boolean isWhite() {
                return false;
            }

            @Override
            public boolean isBlack() {
                return true;
            }

            @Override
            public String toString() {
                return "Black Player";
            }
        };

        public abstract boolean isWhite();

        public abstract boolean isBlack();
    }

    public abstract List<Figure> getActiveFigures();

    public abstract PlayerType getPlayerType();
}
