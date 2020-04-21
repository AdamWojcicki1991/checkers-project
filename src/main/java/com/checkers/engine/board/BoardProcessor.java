package com.checkers.engine.board;

import com.checkers.engine.playres.PlayerType;

import java.util.ArrayList;
import java.util.List;

import static com.checkers.engine.board.FigureType.*;
import static com.checkers.engine.board.Move.*;
import static com.checkers.engine.playres.PlayerType.BLACK;
import static com.checkers.engine.playres.PlayerType.WHITE;
import static com.checkers.engine.utils.Constants.COLUMN_COUNT;
import static com.checkers.engine.utils.Constants.ROW_COUNT;

public class BoardProcessor {
    private FigureType[][] board;

    public BoardProcessor() {
        this.board = initFigures();
    }

    public FigureType[][] initFigures() {
        board = new FigureType[ROW_COUNT][COLUMN_COUNT];
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COLUMN_COUNT; col++) {
                if (row < 4 && (row + col) % 2 != 0) {
                    board[row][col] = BLACK_PAWN;
                } else if (row > 5 && (row + col) % 2 != 0) {
                    board[row][col] = WHITE_PAWN;
                } else {
                    board[row][col] = EMPTY;
                }
            }
        }
        return board;
    }

    public FigureType getFigure(int row, int col) {
        return board[row][col];
    }

    public void executeMove(Move move) {
        board[move.destinationRow][move.destinationColumn] = board[move.initialRow][move.initialColumn];
        board[move.initialRow][move.initialColumn] = EMPTY;

        if (move.isPawnAttackMove()) {
            int jumpedRow = (move.initialRow + move.destinationRow) / 2;
            int jumpedCol = (move.initialColumn + move.destinationColumn) / 2;
            board[jumpedRow][jumpedCol] = EMPTY;
        } else if (move.isQueenAttackMove()) {
            board[move.getEnemyDestinationRow()][move.getEnemyDestinationColumn()] = EMPTY;
        }
    }

    public void pawnPromotion(Move move, List<Move> calculatedJumps) {
        if (move.isPawnAttackMove() && calculatedJumps.isEmpty()) {
            executePromotion(move);
        } else if (move.isPawnMove()) {
            executePromotion(move);
        }
    }

    public List<Move> calculateLegalMoves(PlayerType player) {
        List<Move> legalMoves = new ArrayList<>();
        if (player != WHITE && player != BLACK) return legalMoves;
        FigureType playerPawn, playerQueen;
        if (player == WHITE) {
            playerPawn = WHITE_PAWN;
            playerQueen = WHITE_QUEEN;
        } else {
            playerPawn = BLACK_PAWN;
            playerQueen = BLACK_QUEEN;
        }
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COLUMN_COUNT; col++) {
                if (board[row][col] == playerPawn) {
                    if (canJump(playerPawn, row + 1, col + 1, row + 2, col + 2))
                        legalMoves.add(new PawnAttackMove(row, col, row + 2, col + 2, row + 1, col + 1));
                    if (canJump(playerPawn, row + 1, col - 1, row + 2, col - 2))
                        legalMoves.add(new PawnAttackMove(row, col, row + 2, col - 2, row + 1, col - 1));
                    if (canJump(playerPawn, row - 1, col + 1, row - 2, col + 2))
                        legalMoves.add(new PawnAttackMove(row, col, row - 2, col + 2, row - 1, col + 1));
                    if (canJump(playerPawn, row - 1, col - 1, row - 2, col - 2))
                        legalMoves.add(new PawnAttackMove(row, col, row - 2, col - 2, row - 1, col - 1));
                } else if (board[row][col] == playerQueen) {
                    int incrementRow = row + 1, incrementCol = col + 1, decrementRow = row - 1, decrementCol = col - 1, enemyDestinationRow = -1, enemyDestinationCol = -1;
                    while (!isInvalidDestination(incrementRow, incrementCol)) {
                        if (hasAllianceFigure(player, incrementRow, incrementCol)) break;
                        if (canJump(playerQueen, incrementRow, incrementCol, incrementRow + 1, incrementCol + 1)) {
                            enemyDestinationRow = incrementRow;
                            enemyDestinationCol = incrementCol;
                            legalMoves.add(new QueenAttackMove(row, col,
                                    incrementRow + 1, incrementCol + 1,
                                    enemyDestinationRow, enemyDestinationCol));
                        }
                        if (hasEnemyFigure(player, incrementRow, incrementCol)) {
                            while (canMoveBehindFigure(incrementRow + 1, incrementCol + 1)) {
                                legalMoves.add(new QueenAttackMove(row, col,
                                        incrementRow + 1, incrementCol + 1,
                                        enemyDestinationRow, enemyDestinationCol));
                                incrementRow++;
                                incrementCol++;
                            }
                            break;
                        }
                        incrementRow++;
                        incrementCol++;
                    }
                    while (!isInvalidDestination(decrementRow, decrementCol)) {
                        if (hasAllianceFigure(player, decrementRow, decrementCol)) break;
                        if (canJump(playerQueen, decrementRow, decrementCol, decrementRow - 1, decrementCol - 1)) {
                            enemyDestinationRow = decrementRow;
                            enemyDestinationCol = decrementCol;
                            legalMoves.add(new QueenAttackMove(row, col,
                                    decrementRow - 1, decrementCol - 1,
                                    enemyDestinationRow, enemyDestinationCol));
                        }
                        if (hasEnemyFigure(player, decrementRow, decrementCol)) {
                            while (canMoveBehindFigure(decrementRow - 1, decrementCol - 1)) {
                                legalMoves.add(new QueenAttackMove(row, col,
                                        decrementRow - 1, decrementCol - 1,
                                        enemyDestinationRow, enemyDestinationCol));
                                decrementRow--;
                                decrementCol--;
                            }
                            break;
                        }
                        decrementRow--;
                        decrementCol--;
                    }
                    incrementRow = row + 1;
                    incrementCol = col + 1;
                    decrementRow = row - 1;
                    decrementCol = col - 1;
                    while (!isInvalidDestination(decrementRow, incrementCol)) {
                        if (hasAllianceFigure(player, decrementRow, incrementCol)) break;
                        if (canJump(playerQueen, decrementRow, incrementCol, decrementRow - 1, incrementCol + 1)) {
                            enemyDestinationRow = decrementRow;
                            enemyDestinationCol = incrementCol;
                            legalMoves.add(new QueenAttackMove(row, col,
                                    decrementRow - 1, incrementCol + 1,
                                    enemyDestinationRow, enemyDestinationCol));
                        }
                        if (hasEnemyFigure(player, decrementRow, incrementCol)) {
                            while (canMoveBehindFigure(decrementRow - 1, incrementCol + 1)) {
                                legalMoves.add(new QueenAttackMove(row, col,
                                        decrementRow - 1, incrementCol + 1,
                                        enemyDestinationRow, enemyDestinationCol));
                                decrementRow--;
                                incrementCol++;
                            }
                            break;
                        }
                        decrementRow--;
                        incrementCol++;
                    }
                    while (!isInvalidDestination(incrementRow, decrementCol)) {
                        if (hasAllianceFigure(player, incrementRow, decrementCol)) break;
                        if (canJump(playerQueen, incrementRow, decrementCol, incrementRow + 1, decrementCol - 1)) {
                            enemyDestinationRow = incrementRow;
                            enemyDestinationCol = decrementCol;
                            legalMoves.add(new QueenAttackMove(row, col,
                                    incrementRow + 1, decrementCol - 1,
                                    enemyDestinationRow, enemyDestinationCol));
                        }
                        if (hasEnemyFigure(player, incrementRow, decrementCol)) {
                            while (canMoveBehindFigure(incrementRow + 1, decrementCol - 1)) {
                                legalMoves.add(new QueenAttackMove(row, col,
                                        incrementRow + 1, decrementCol - 1,
                                        enemyDestinationRow, enemyDestinationCol));
                                incrementRow++;
                                decrementCol--;
                            }
                            break;
                        }
                        incrementRow++;
                        decrementCol--;
                    }
                }
            }
        }
        if (legalMoves.size() == 0) {
            for (int row = 0; row < ROW_COUNT; row++) {
                for (int col = 0; col < COLUMN_COUNT; col++) {
                    if (board[row][col] == playerPawn) {
                        if (canMove(playerPawn, row, col, row + 1, col + 1))
                            legalMoves.add(new MajorMove(row, col, row + 1, col + 1));
                        if (canMove(playerPawn, row, col, row + 1, col - 1))
                            legalMoves.add(new MajorMove(row, col, row + 1, col - 1));
                        if (canMove(playerPawn, row, col, row - 1, col + 1))
                            legalMoves.add(new MajorMove(row, col, row - 1, col + 1));
                        if (canMove(playerPawn, row, col, row - 1, col - 1))
                            legalMoves.add(new MajorMove(row, col, row - 1, col - 1));
                    } else if (board[row][col] == playerQueen) {
                        int incrementRow = row + 1, incrementCol = col + 1, decrementRow = row - 1, decrementCol = col - 1;
                        while (canMove(playerQueen, row, col, incrementRow, incrementCol)) {
                            legalMoves.add(new MajorMove(row, col, incrementRow, incrementCol));
                            incrementRow++;
                            incrementCol++;
                        }
                        while (canMove(playerQueen, row, col, decrementRow, decrementCol)) {
                            legalMoves.add(new MajorMove(row, col, decrementRow, decrementCol));
                            decrementRow--;
                            decrementCol--;
                        }
                        incrementRow = row + 1;
                        incrementCol = col + 1;
                        decrementRow = row - 1;
                        decrementCol = col - 1;
                        while (canMove(playerQueen, row, col, decrementRow, incrementCol)) {
                            legalMoves.add(new MajorMove(row, col, decrementRow, incrementCol));
                            decrementRow--;
                            incrementCol++;
                        }
                        while (canMove(playerQueen, row, col, incrementRow, decrementCol)) {
                            legalMoves.add(new MajorMove(row, col, incrementRow, decrementCol));
                            incrementRow++;
                            decrementCol--;
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    public List<Move> calculateNextJump(PlayerType player, int destinationRow, int destinationColumn) {
        List<Move> nextJumps = new ArrayList<>();
        if (player != WHITE && player != BLACK) return nextJumps;
        FigureType playerPawn, playerQueen;
        if (player == WHITE) {
            playerPawn = WHITE_PAWN;
            playerQueen = WHITE_QUEEN;
        } else {
            playerPawn = BLACK_PAWN;
            playerQueen = BLACK_QUEEN;
        }
        if (board[destinationRow][destinationColumn] == playerPawn) {
            if (canJump(playerPawn, destinationRow + 1, destinationColumn + 1, destinationRow + 2, destinationColumn + 2))
                nextJumps.add(new PawnAttackMove(destinationRow, destinationColumn,
                        destinationRow + 2, destinationColumn + 2,
                        destinationRow + 1, destinationColumn + 1));
            if (canJump(playerPawn, destinationRow + 1, destinationColumn - 1, destinationRow + 2, destinationColumn - 2))
                nextJumps.add(new PawnAttackMove(destinationRow, destinationColumn,
                        destinationRow + 2, destinationColumn - 2,
                        destinationRow + 1, destinationColumn - 1));
            if (canJump(playerPawn, destinationRow - 1, destinationColumn + 1, destinationRow - 2, destinationColumn + 2))
                nextJumps.add(new PawnAttackMove(destinationRow, destinationColumn,
                        destinationRow - 2, destinationColumn + 2,
                        destinationRow - 1, destinationColumn + 1));
            if (canJump(playerPawn, destinationRow - 1, destinationColumn - 1, destinationRow - 2, destinationColumn - 2))
                nextJumps.add(new PawnAttackMove(destinationRow, destinationColumn,
                        destinationRow - 2, destinationColumn - 2,
                        destinationRow - 1, destinationColumn - 1));
        } else if (board[destinationRow][destinationColumn] == playerQueen) {
            int incrementRow = destinationRow + 1, incrementCol = destinationColumn + 1, decrementRow = destinationRow - 1,
                    decrementCol = destinationColumn - 1, enemyDestinationRow = -1, enemyDestinationCol = -1;
            while (!isInvalidDestination(incrementRow, incrementCol)) {
                if (hasAllianceFigure(player, incrementRow, incrementCol)) break;
                if (canJump(playerQueen, incrementRow, incrementCol, incrementRow + 1, incrementCol + 1)) {
                    enemyDestinationRow = incrementRow;
                    enemyDestinationCol = incrementCol;
                    nextJumps.add(new QueenAttackMove(destinationRow, destinationColumn,
                            incrementRow + 1, incrementCol + 1,
                            enemyDestinationRow, enemyDestinationCol));
                }
                if (hasEnemyFigure(player, incrementRow, incrementCol)) {
                    while (canMoveBehindFigure(incrementRow + 1, incrementCol + 1)) {
                        nextJumps.add(new QueenAttackMove(destinationRow, destinationColumn,
                                incrementRow + 1, incrementCol + 1,
                                enemyDestinationRow, enemyDestinationCol));
                        incrementRow++;
                        incrementCol++;
                    }
                    break;
                }
                incrementRow++;
                incrementCol++;
            }
            while (!isInvalidDestination(decrementRow, decrementCol)) {
                if (hasAllianceFigure(player, decrementRow, decrementCol)) break;
                if (canJump(playerQueen, decrementRow, decrementCol, decrementRow - 1, decrementCol - 1)) {
                    enemyDestinationRow = decrementRow;
                    enemyDestinationCol = decrementCol;
                    nextJumps.add(new QueenAttackMove(destinationRow, destinationColumn,
                            decrementRow - 1, decrementCol - 1,
                            enemyDestinationRow, enemyDestinationCol));
                }
                if (hasEnemyFigure(player, decrementRow, decrementCol)) {
                    while (canMoveBehindFigure(decrementRow - 1, decrementCol - 1)) {
                        nextJumps.add(new QueenAttackMove(destinationRow, destinationColumn,
                                decrementRow - 1, decrementCol - 1,
                                enemyDestinationRow, enemyDestinationCol));
                        decrementRow--;
                        decrementCol--;
                    }
                    break;
                }
                decrementRow--;
                decrementCol--;
            }
            incrementRow = destinationRow + 1;
            incrementCol = destinationColumn + 1;
            decrementRow = destinationRow - 1;
            decrementCol = destinationColumn - 1;
            while (!isInvalidDestination(decrementRow, incrementCol)) {
                if (hasAllianceFigure(player, decrementRow, incrementCol)) break;
                if (canJump(playerQueen, decrementRow, incrementCol, decrementRow - 1, incrementCol + 1)) {
                    enemyDestinationRow = decrementRow;
                    enemyDestinationCol = incrementCol;
                    nextJumps.add(new QueenAttackMove(destinationRow, destinationColumn,
                            decrementRow - 1, incrementCol + 1,
                            enemyDestinationRow, enemyDestinationCol));
                }
                if (hasEnemyFigure(player, decrementRow, incrementCol)) {
                    while (canMoveBehindFigure(decrementRow - 1, incrementCol + 1)) {
                        nextJumps.add(new QueenAttackMove(destinationRow, destinationColumn,
                                decrementRow - 1, incrementCol + 1,
                                enemyDestinationRow, enemyDestinationCol));
                        decrementRow--;
                        incrementCol++;
                    }
                    break;
                }
                decrementRow--;
                incrementCol++;
            }
            while (!isInvalidDestination(incrementRow, decrementCol)) {
                if (hasAllianceFigure(player, incrementRow, decrementCol)) break;
                if (canJump(playerQueen, incrementRow, decrementCol, incrementRow + 1, decrementCol - 1)) {
                    enemyDestinationRow = incrementRow;
                    enemyDestinationCol = decrementCol;
                    nextJumps.add(new QueenAttackMove(destinationRow, destinationColumn,
                            incrementRow + 1, decrementCol - 1,
                            enemyDestinationRow, enemyDestinationCol));
                }
                if (hasEnemyFigure(player, incrementRow, decrementCol)) {
                    while (canMoveBehindFigure(incrementRow + 1, decrementCol - 1)) {
                        nextJumps.add(new QueenAttackMove(destinationRow, destinationColumn,
                                incrementRow + 1, decrementCol - 1,
                                enemyDestinationRow, enemyDestinationCol));
                        incrementRow++;
                        decrementCol--;
                    }
                    break;
                }
                incrementRow++;
                decrementCol--;
            }
        }
        return nextJumps;
    }

    private void executePromotion(Move move) {
        if (move.destinationRow == 0 && board[move.destinationRow][move.destinationColumn] == WHITE_PAWN)
            board[move.destinationRow][move.destinationColumn] = WHITE_QUEEN;
        if (move.destinationRow == 9 && board[move.destinationRow][move.destinationColumn] == BLACK_PAWN)
            board[move.destinationRow][move.destinationColumn] = BLACK_QUEEN;
    }

    private boolean canJump(FigureType playerFigure, int jumpedRow, int jumpedColumn, int destinationRow, int destinationColumn) {
        if (isInvalidDestination(destinationRow, destinationColumn)) return false;
        if (board[destinationRow][destinationColumn] != EMPTY) return false;
        if (playerFigure == WHITE_PAWN || playerFigure == WHITE_QUEEN) {
            return board[jumpedRow][jumpedColumn] == BLACK_PAWN || board[jumpedRow][jumpedColumn] == BLACK_QUEEN;
        } else {
            return board[jumpedRow][jumpedColumn] == WHITE_PAWN || board[jumpedRow][jumpedColumn] == WHITE_QUEEN;
        }
    }

    private boolean canMove(FigureType playerFigure, int initialRow, int initialColumn, int destinationRow, int destinationColumn) {
        if (isInvalidDestination(destinationRow, destinationColumn)) return false;
        if (board[destinationRow][destinationColumn] != EMPTY) return false;
        if (playerFigure == WHITE_PAWN) {
            return (board[initialRow][initialColumn] == WHITE_PAWN) && (destinationRow < initialRow);
        } else if (playerFigure == BLACK_PAWN) {
            return (board[initialRow][initialColumn] == BLACK_PAWN) && (destinationRow > initialRow);
        } else if (playerFigure == WHITE_QUEEN) {
            return (board[initialRow][initialColumn] == WHITE_QUEEN);
        } else {
            return (board[initialRow][initialColumn] == BLACK_QUEEN);
        }
    }

    private boolean canMoveBehindFigure(int destinationRow, int destinationColumn) {
        if (isInvalidDestination(destinationRow, destinationColumn)) return false;
        return board[destinationRow][destinationColumn] == EMPTY;
    }

    private boolean hasEnemyFigure(PlayerType player, int row, int col) {
        if (player == WHITE && (getFigure(row, col) == BLACK_PAWN || getFigure(row, col) == BLACK_QUEEN)) {
            return true;
        } else return player == BLACK && (getFigure(row, col) == WHITE_PAWN || getFigure(row, col) == WHITE_QUEEN);
    }

    private boolean hasAllianceFigure(PlayerType player, int row, int col) {
        if (player == WHITE && (getFigure(row, col) == WHITE_PAWN || getFigure(row, col) == WHITE_PAWN)) {
            return true;
        } else return player == BLACK && (getFigure(row, col) == BLACK_PAWN || getFigure(row, col) == BLACK_QUEEN);
    }

    private boolean isInvalidDestination(int destinationRow, int destinationColumn) {
        return destinationRow < 0 || destinationRow >= ROW_COUNT || destinationColumn < 0 || destinationColumn >= COLUMN_COUNT;
    }
}
