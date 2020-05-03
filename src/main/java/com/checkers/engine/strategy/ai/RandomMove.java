package com.checkers.engine.strategy.ai;

import com.checkers.UIX.GameBoard;
import com.checkers.engine.move.Move;

import java.util.List;
import java.util.Random;

public class RandomMove implements MoveStrategy {

    private final Random RANDOM;

    public RandomMove() {
        this.RANDOM = new Random();
    }

    @Override
    public String toString() {
        return "Random Move";
    }

    @Override
    public Move execute(GameBoard board) {
        List<Move> legalMoves = board.getLegalMoves();
        return legalMoves.get(RANDOM.nextInt(legalMoves.size()));
    }
}
