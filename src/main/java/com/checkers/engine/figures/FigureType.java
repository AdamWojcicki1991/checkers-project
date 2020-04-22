package com.checkers.engine.figures;

public enum FigureType {

    WHITE_PAWN("WP"),
    WHITE_QUEEN("WQ"),
    BLACK_PAWN("BP"),
    BLACK_QUEEN("BQ");

    private final String figureName;

    FigureType(final String figureName) {
        this.figureName = figureName;
    }

    @Override
    public String toString() {
        return figureName;
    }
}
