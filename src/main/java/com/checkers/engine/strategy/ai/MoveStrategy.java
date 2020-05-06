package com.checkers.engine.strategy.ai;

import com.checkers.controler.GameBoard;
import com.checkers.engine.move.Move;

public interface MoveStrategy {
    Move execute(GameBoard gameBoard);
}
