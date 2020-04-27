package com.checkers.engine.figures;

import com.checkers.engine.board.BoardField;
import com.checkers.engine.move.Move;
import com.checkers.engine.playres.Player.PlayerType;

import java.util.List;
import java.util.Objects;

public abstract class Figure {

    protected final FigureType figureType;
    protected final int row;
    protected final int column;

    public Figure(final FigureType figureType, final int row, final int column) {
        this.figureType = figureType;
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Figure figure = (Figure) o;

        return Objects.equals(figureType, figure.figureType) &&
                Objects.equals(row, figure.row) &&
                Objects.equals(column, figure.column);
    }

    @Override
    public int hashCode() {
        return Objects.hash(figureType, row, column);
    }

    public FigureType getFigureType() {
        return figureType;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public enum FigureType {

        WHITE_PAWN("WP", 10),
        WHITE_QUEEN("WQ", 900),
        BLACK_PAWN("BP", 10),
        BLACK_QUEEN("BQ", 900);

        private final String figureName;
        private final int figureValue;

        FigureType(final String figureName, final int figureValue) {
            this.figureName = figureName;
            this.figureValue = figureValue;
        }

        public String getFigureName() {
            return figureName;
        }

        public int getFigureValue() {
            return figureValue;
        }

        @Override
        public String toString() {
            return figureName;
        }
    }

    public abstract List<Move> calculateLegalMoves(final BoardField[][] board, final FigureType playerFigure, final PlayerType playerType);

    public abstract List<Move> calculateNextJump(final BoardField[][] board, final FigureType playerFigure, final PlayerType playerType, final int destinationRow, final int destinationColumn);
}
