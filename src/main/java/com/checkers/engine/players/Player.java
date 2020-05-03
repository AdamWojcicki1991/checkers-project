package com.checkers.engine.players;

import com.checkers.engine.board.Board;
import com.checkers.engine.figures.Figure;
import com.checkers.engine.move.Move;

import java.util.List;

public abstract class Player {

    protected Board board;

    Player(final Board board) {
        this.board = board;
    }

    public enum PlayerType {
        WHITE {
            @Override
            public String toString() {
                return "White Player";
            }
        },
        BLACK {
            @Override
            public String toString() {
                return "Black Player";
            }
        };
    }

    public abstract PlayerType getPlayerType();

    public abstract Board setBoard(Board board);

    public abstract boolean isComputerPlayer();

    public abstract void setComputerPlayer(boolean computerPlayer);
}
