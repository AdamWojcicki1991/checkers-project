package com.checkers.engine.strategy.ai;

import com.checkers.UIX.CheckersBoard;
import com.checkers.engine.board.Board;

public interface BoardEvaluator {
    int evaluate(CheckersBoard checkersBoard, Board board, int depth);
}
