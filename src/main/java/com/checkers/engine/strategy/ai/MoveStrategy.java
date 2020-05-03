package com.checkers.engine.strategy.ai;

import com.checkers.UIX.GameBoard;
import com.checkers.engine.move.Move;

public interface MoveStrategy {
    Move execute(GameBoard gameBoard);
}
