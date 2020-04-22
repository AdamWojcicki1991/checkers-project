package com.checkers.engine.board;

import com.checkers.engine.figures.FigureType;
import com.checkers.engine.figures.Pawn;
import com.checkers.engine.figures.Queen;
import com.checkers.engine.playres.PlayerType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.checkers.engine.board.BoardField.createBoardField;
import static com.checkers.engine.figures.FigureType.*;
import static com.checkers.engine.playres.PlayerType.BLACK;
import static com.checkers.engine.playres.PlayerType.WHITE;
import static com.checkers.engine.utils.EngineUtils.COLUMN_COUNT;
import static com.checkers.engine.utils.EngineUtils.ROW_COUNT;

public class Board {
    private BoardField[][] board;

    public Board() {
        this.board = initBoard();
    }

    public BoardField[][] initBoard() {
        int boardFieldNumber = 1;
        board = new BoardField[ROW_COUNT][COLUMN_COUNT];
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COLUMN_COUNT; col++) {
                if (row < 4 && (row + col) % 2 != 0) {
                    board[row][col] = createBoardField(row % 2 == col % 2 ? 0 : boardFieldNumber++, new Pawn(BLACK_PAWN, row, col));
                } else if (row > 5 && (row + col) % 2 != 0) {
                    board[row][col] = createBoardField(row % 2 == col % 2 ? 0 : boardFieldNumber++, new Pawn(WHITE_PAWN, row, col));
                } else {
                    board[row][col] = createBoardField(row % 2 == col % 2 ? 0 : boardFieldNumber++, null);
                }
            }
        }
        return board;
    }

    public void executeMove(Move move) {
        if (!board[move.destinationRow][move.destinationColumn].isBoardFieldOccupied()) {
            moveFigure(move);
        }
    }

    public void executeJump(Move move) {
        if (!board[move.destinationRow][move.destinationColumn].isBoardFieldOccupied() && !board[move.getEnemyDestinationRow()][move.getEnemyDestinationColumn()].isJumped()) {
            moveFigure(move);
            if (move.isPawnAttackMove() || move.isQueenAttackMove()) {
                board[move.getEnemyDestinationRow()][move.getEnemyDestinationColumn()].setJumped(true);
            }
        }
    }

    public void killFigure(Move move) {
        if (board[move.getEnemyDestinationRow()][move.getEnemyDestinationColumn()].isBoardFieldOccupied()) {
            if (move.isPawnAttackMove() || move.isQueenAttackMove()) {
                board[move.getEnemyDestinationRow()][move.getEnemyDestinationColumn()] =
                        createBoardField(board[move.getEnemyDestinationRow()][move.getEnemyDestinationColumn()].getBoardFieldNumber(), null);
            }
        }
    }

    public void pawnPromotion(Move move, List<Move> calculateJump) {
        if (move.isPawnAttackMove() && calculateJump.isEmpty()) {
            executePromotion(move);
        } else if (move.isPawnMove()) {
            executePromotion(move);
        }
    }

    public List<Move> calculateMovesOnBoard(PlayerType player) {
        final List<Move> legalMovesOnBoard = new ArrayList<>();
        final List<Move> legalAttacksOnBoard = new ArrayList<>();
        if (player != WHITE && player != BLACK) return legalMovesOnBoard;
        FigureType playerPawn = setPlayerPawn(player);
        FigureType playerQueen = setPlayerQueen(player);
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COLUMN_COUNT; col++) {
                if (board[row][col].isBoardFieldOccupied()) {
                    calculateMoves(player, legalMovesOnBoard, legalAttacksOnBoard, playerPawn, playerQueen, row, col);
                }
            }
        }
        if (!legalAttacksOnBoard.isEmpty()) {
            return Collections.unmodifiableList(legalAttacksOnBoard);
        }
        return Collections.unmodifiableList(legalMovesOnBoard);
    }

    public List<Move> calculateNextJumpOnBoard(PlayerType player, int destinationRow, int destinationColumn) {
        List<Move> nextJumpsOnBoard = new ArrayList<>();
        if (player != WHITE && player != BLACK) return nextJumpsOnBoard;
        FigureType playerPawn = setPlayerPawn(player);
        FigureType playerQueen = setPlayerQueen(player);
        calculateNextJump(player, destinationRow, destinationColumn, nextJumpsOnBoard, playerPawn, playerQueen);
        return nextJumpsOnBoard;
    }

    public BoardField getBoardField(int row, int column) {
        return board[row][column];
    }

    public BoardField[][] getBoard() {
        return board;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COLUMN_COUNT; col++) {
                final String boardFieldText = board[row][col].toString();
                stringBuilder.append(String.format("%3s", boardFieldText));
                if ((col + 1) % ROW_COUNT == 0) {
                    stringBuilder.append("\n");
                }
            }
        }
        return stringBuilder.toString();
    }

    private void moveFigure(Move move) {
        FigureType movedFigure = board[move.initialRow][move.initialColumn].getFigure().getFigureType();
        if (movedFigure == WHITE_PAWN || movedFigure == BLACK_PAWN) {
            board[move.destinationRow][move.destinationColumn] =
                    createBoardField(board[move.destinationRow][move.destinationColumn].getBoardFieldNumber(),
                            new Pawn(movedFigure, move.destinationRow, move.destinationColumn));
        } else {
            board[move.destinationRow][move.destinationColumn] =
                    createBoardField(board[move.destinationRow][move.destinationColumn].getBoardFieldNumber(),
                            new Queen(movedFigure, move.destinationRow, move.destinationColumn));
        }
        board[move.initialRow][move.initialColumn] = createBoardField(board[move.initialRow][move.initialColumn].getBoardFieldNumber(), null);
    }

    private void executePromotion(Move move) {
        if (move.destinationRow == 0 && board[move.destinationRow][move.destinationColumn].getFigure().getFigureType() == WHITE_PAWN)
            board[move.destinationRow][move.destinationColumn] =
                    createBoardField(board[move.destinationRow][move.destinationColumn].getBoardFieldNumber(),
                            new Queen(WHITE_QUEEN, move.destinationRow, move.destinationColumn));
        if (move.destinationRow == 9 && board[move.destinationRow][move.destinationColumn].getFigure().getFigureType() == BLACK_PAWN)
            board[move.destinationRow][move.destinationColumn] =
                    createBoardField(board[move.destinationRow][move.destinationColumn].getBoardFieldNumber(),
                            new Queen(BLACK_QUEEN, move.destinationRow, move.destinationColumn));
    }

    private void calculateMoves(PlayerType player, List<Move> legalMovesOnBoard, List<Move> legalAttacksOnBoard, FigureType playerPawn, FigureType playerQueen, int row, int col) {
        if (board[row][col].getFigure().getFigureType() == playerPawn) {
            List<Move> legalMoves = board[row][col].getFigure().calculateLegalMove(board, playerPawn, player);
            if (legalMoves.isEmpty()) return;
            if (legalMoves.get(0).isPawnMajorMove()) {
                legalMovesOnBoard.addAll(legalMoves);
            } else {
                legalAttacksOnBoard.addAll(legalMoves);
            }
        } else if (board[row][col].getFigure().getFigureType() == playerQueen) {
            List<Move> legalMoves = board[row][col].getFigure().calculateLegalMove(board, playerQueen, player);
            if (legalMoves.get(0).isQueenMajorMove()) {
                legalMovesOnBoard.addAll(legalMoves);
            } else {
                legalAttacksOnBoard.addAll(legalMoves);
            }
        }
    }

    private void calculateNextJump(PlayerType player, int initialRow, int initialColumn, List<Move> nextJumpsOnBoard, FigureType playerPawn, FigureType playerQueen) {
        if (board[initialRow][initialColumn].isBoardFieldOccupied()) {
            if (board[initialRow][initialColumn].getFigure().getFigureType() == playerPawn) {
                List<Move> nextJumps = board[initialRow][initialColumn].getFigure().calculateNextJump(
                        board, playerPawn, player, initialRow, initialColumn);
                nextJumpsOnBoard.addAll(nextJumps);
            } else if (board[initialRow][initialColumn].getFigure().getFigureType() == playerQueen) {
                List<Move> nextJumps = board[initialRow][initialColumn].getFigure().calculateNextJump(
                        board, playerQueen, player, initialRow, initialColumn);
                nextJumpsOnBoard.addAll(nextJumps);
            }
        }
    }

    private FigureType setPlayerPawn(PlayerType player) {
        return (player == WHITE) ? WHITE_PAWN : BLACK_PAWN;
    }

    private FigureType setPlayerQueen(PlayerType player) {
        return (player == WHITE) ? WHITE_QUEEN : BLACK_QUEEN;
    }
}