package com.checkers.engine.players;

import com.checkers.engine.board.Board;
import com.checkers.engine.figures.Figure;
import com.checkers.engine.move.Move;

import java.util.ArrayList;
import java.util.List;

import static com.checkers.engine.figures.Figure.FigureType.BLACK_PAWN;
import static com.checkers.engine.figures.Figure.FigureType.BLACK_QUEEN;
import static com.checkers.engine.players.Player.PlayerType.BLACK;
import static com.checkers.engine.utils.EngineUtils.COLUMN_COUNT;
import static com.checkers.engine.utils.EngineUtils.ROW_COUNT;

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
