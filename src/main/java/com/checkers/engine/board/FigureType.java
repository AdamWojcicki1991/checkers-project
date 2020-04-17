package com.checkers.engine.board;

public enum FigureType {
    EMPTY("NONE"),
    WHITE_PAWN("WHITE"),
    WHITE_QUEEN("WHITE"),
    BLACK_PAWN("BLACK"),
    BLACK_QUEEN("BLACK");

    private final String color;

    FigureType(final String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
