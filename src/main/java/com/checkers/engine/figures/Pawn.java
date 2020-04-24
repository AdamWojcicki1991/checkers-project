package com.checkers.engine.figures;

import com.checkers.engine.board.BoardField;
import com.checkers.engine.move.Move;
import com.checkers.engine.playres.PlayerType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.checkers.engine.move.Move.PawnAttackMove;
import static com.checkers.engine.move.Move.PawnMajorMove;
import static com.checkers.engine.utils.EngineUtils.isJumpValid;
import static com.checkers.engine.utils.EngineUtils.isMoveValid;

public class Pawn extends Figure {

    public Pawn(final FigureType figureType, final int row, final int column) {
        super(figureType, row, column);
    }

    @Override
    public List<Move> calculateLegalMoves(final BoardField[][] board, final FigureType playerFigure, final PlayerType playerType) {
        final List<Move> legalMoves = new ArrayList<>();
        calculateAttackMove(board, playerFigure, legalMoves, row, column);
        if (legalMoves.isEmpty()) {
            calculateMajorMove(board, playerFigure, legalMoves);
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public List<Move> calculateNextJump(final BoardField[][] board, final FigureType playerFigure, final PlayerType playerType, final int destinationRow, final int destinationColumn) {
        List<Move> nextJumps = new ArrayList<>();
        calculateAttackMove(board, playerFigure, nextJumps, destinationRow, destinationColumn);
        return Collections.unmodifiableList(nextJumps);
    }

    private void calculateAttackMove(BoardField[][] board, FigureType playerFigure, List<Move> legalMoves, int row, int column) {
        if (isJumpValid(playerFigure, board, row + 1, column + 1, row + 2, column + 2))
            legalMoves.add(new PawnAttackMove(row, column, row + 2, column + 2, row + 1, column + 1));
        if (isJumpValid(playerFigure, board, row + 1, column - 1, row + 2, column - 2))
            legalMoves.add(new PawnAttackMove(row, column, row + 2, column - 2, row + 1, column - 1));
        if (isJumpValid(playerFigure, board, row - 1, column + 1, row - 2, column + 2))
            legalMoves.add(new PawnAttackMove(row, column, row - 2, column + 2, row - 1, column + 1));
        if (isJumpValid(playerFigure, board, row - 1, column - 1, row - 2, column - 2))
            legalMoves.add(new PawnAttackMove(row, column, row - 2, column - 2, row - 1, column - 1));
    }

    private void calculateMajorMove(BoardField[][] board, FigureType playerFigure, List<Move> legalMoves) {
        if (isMoveValid(playerFigure, board, row, column, row + 1, column + 1))
            legalMoves.add(new PawnMajorMove(row, column, row + 1, column + 1));
        if (isMoveValid(playerFigure, board, row, column, row + 1, column - 1))
            legalMoves.add(new PawnMajorMove(row, column, row + 1, column - 1));
        if (isMoveValid(playerFigure, board, row, column, row - 1, column + 1))
            legalMoves.add(new PawnMajorMove(row, column, row - 1, column + 1));
        if (isMoveValid(playerFigure, board, row, column, row - 1, column - 1))
            legalMoves.add(new PawnMajorMove(row, column, row - 1, column - 1));
    }

    @Override
    public String toString() {
        return figureType.toString();
    }
}
