package com.checkers.engine.board;

import com.checkers.engine.figures.Figure;

import java.util.Objects;

public abstract class BoardField implements Cloneable {

    protected final int boardFieldNumber;
    protected final Figure figure;
    private boolean isJumped;

    public BoardField(final int boardFieldNumber, final Figure figure) {
        this.boardFieldNumber = boardFieldNumber;
        this.figure = figure;
        this.isJumped = false;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardField boardField = (BoardField) o;

        return Objects.equals(boardFieldNumber, boardField.boardFieldNumber) &&
                Objects.equals(figure, boardField.figure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardFieldNumber, figure);
    }

    @Override
    public BoardField clone() {
        return createBoardField(this.boardFieldNumber, this.figure);
    }

    public static BoardField createBoardField(final int boardFieldNumber, final Figure figure) {
        return figure != null ? new OccupiedBoardField(boardFieldNumber, figure) :
                new EmptyBoardField(boardFieldNumber, null);
    }

    public void setJumped(boolean jumped) {
        isJumped = true;
    }

    public boolean isJumped() {
        return isJumped;
    }

    public abstract boolean isBoardFieldOccupied();

    public abstract Figure getFigure();

    public abstract int getBoardFieldNumber();


    public final static class EmptyBoardField extends BoardField {

        public EmptyBoardField(int boardFieldNumber, Figure figure) {
            super(boardFieldNumber, figure);
        }

        @Override
        public boolean isBoardFieldOccupied() {
            return false;
        }

        @Override
        public Figure getFigure() {
            return null;
        }

        @Override
        public int getBoardFieldNumber() {
            return boardFieldNumber;
        }

        @Override
        public String toString() {
            return "-";
        }
    }

    public final static class OccupiedBoardField extends BoardField {

        public OccupiedBoardField(int boardFieldNumber, Figure figure) {
            super(boardFieldNumber, figure);
        }

        @Override
        public boolean isBoardFieldOccupied() {
            return true;
        }

        @Override
        public Figure getFigure() {
            return figure;
        }

        @Override
        public int getBoardFieldNumber() {
            return boardFieldNumber;
        }

        @Override
        public String toString() {
            return getFigure().getFigureType().toString();
        }
    }
}
