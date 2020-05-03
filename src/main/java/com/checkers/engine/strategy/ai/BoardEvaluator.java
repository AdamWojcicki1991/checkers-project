package com.checkers.engine.strategy.ai;

import com.checkers.engine.board.Board;

public interface BoardEvaluator {
    int evaluate(Board board, int depth);
}
