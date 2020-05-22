package com.checkers.engine.players;

import com.checkers.engine.board.Board;

import static com.checkers.engine.players.Player.PlayerType.BLACK;

public class BlackPlayer extends Player {
    private boolean computerPlayer = false;

    public BlackPlayer(final Board board) {
        super(board);
    }

    @Override
    public boolean isComputerPlayer() {
        return computerPlayer;
    }

    @Override
    public void setComputerPlayer(boolean computerPlayer) {
        this.computerPlayer = computerPlayer;
    }

    @Override
    public Board setBoard(Board board) {
        return this.board = board;
    }

    @Override
    public PlayerType getPlayerType() {
        return BLACK;
    }
}
