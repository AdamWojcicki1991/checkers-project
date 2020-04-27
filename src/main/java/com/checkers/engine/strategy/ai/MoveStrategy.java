package com.checkers.engine.strategy.ai;

import com.checkers.UIX.CheckersBoard;
import com.checkers.engine.move.Move;

public interface MoveStrategy {
    Move execute(CheckersBoard board);
}
