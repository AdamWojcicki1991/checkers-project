package com.checkers.engine.playres;

import com.checkers.engine.board.Board;
import com.checkers.engine.figures.Figure;
import com.checkers.engine.move.Move;

import java.util.ArrayList;
import java.util.List;

import static com.checkers.engine.figures.Figure.FigureType.WHITE_PAWN;
import static com.checkers.engine.figures.Figure.FigureType.WHITE_QUEEN;
import static com.checkers.engine.playres.Player.PlayerType.WHITE;
import static com.checkers.engine.utils.EngineUtils.COLUMN_COUNT;
import static com.checkers.engine.utils.EngineUtils.ROW_COUNT;

public class WhitePlayer extends Player {

    private List<Move> legalMoves;

    public WhitePlayer(Board board) {
        super(board);
        this.legalMoves = board.calculateMovesOnBoard(getPlayerType());
    }

    @Override
    public List<Figure> getActiveFigures() {
        List<Figure> activeFigures = new ArrayList<>();
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COLUMN_COUNT; col++) {
                if (board.getBoardArray()[row][col].isBoardFieldOccupied() &&
                        (board.getBoardArray()[row][col].getFigure().getFigureType() == WHITE_PAWN ||
                                board.getBoardArray()[row][col].getFigure().getFigureType() == WHITE_QUEEN)) {
                    activeFigures.add(board.getBoardArray()[row][col].getFigure());
                }
            }
        }
        return activeFigures;
    }

    @Override
    public List<Move> getLegalMoves() {
        return legalMoves;
    }

    @Override
    public PlayerType getPlayerType() {
        return WHITE;
    }
}
