package com.checkers.engine.board;

import com.checkers.engine.figures.Figure;
import com.checkers.engine.figures.Pawn;
import com.checkers.engine.figures.Queen;
import com.checkers.engine.move.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.checkers.engine.board.BoardField.createBoardField;
import static com.checkers.engine.figures.Figure.FigureType;
import static com.checkers.engine.figures.Figure.FigureType.*;
import static com.checkers.engine.players.Player.PlayerType;
import static com.checkers.engine.players.Player.PlayerType.BLACK;
import static com.checkers.engine.players.Player.PlayerType.WHITE;
import static com.checkers.engine.utils.EngineUtils.*;

public class Board {
    private BoardField[][] board;

    public Board() {
        this.board = initBoard();
    }

    public Board(Board copyBoard) {
        this.board = copyBoardArray(copyBoard.getBoardArray());
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

    public List<Figure> calculateFiguresOnBoard(PlayerType player) {
        List<Figure> figuresOnBoard = new ArrayList<>();
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COLUMN_COUNT; col++) {
                if (player == WHITE) {
                    if (board[row][col].isBoardFieldOccupied() && (board[row][col].getFigure().getFigureType() == WHITE_PAWN || board[row][col].getFigure().getFigureType() == WHITE_QUEEN)) {
                        figuresOnBoard.add(board[row][col].getFigure());
                    }
                } else {
                    if (board[row][col].isBoardFieldOccupied() && (board[row][col].getFigure().getFigureType() == BLACK_PAWN || board[row][col].getFigure().getFigureType() == BLACK_QUEEN)) {
                        figuresOnBoard.add(board[row][col].getFigure());
                    }
                }
            }
        }
        return figuresOnBoard;
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
        if (move.isPawnMove()) {
            executePromotion(move);
        } else if (move.isPawnAttackMove() && calculateJump.isEmpty()) {
            executePromotion(move);
        }
    }

    public List<Move> calculateMovesOnBoard(PlayerType playerType) {
        final List<Move> legalMovesOnBoard = new ArrayList<>();
        final List<Move> legalAttacksOnBoard = new ArrayList<>();
        if (playerType != WHITE && playerType != BLACK) return legalMovesOnBoard;
        FigureType playerPawn = setPlayerPawn(playerType);
        FigureType playerQueen = setPlayerQueen(playerType);
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COLUMN_COUNT; col++) {
                if (board[row][col].isBoardFieldOccupied()) {
                    calculateMoves(legalMovesOnBoard, legalAttacksOnBoard,
                                   playerPawn, playerQueen, playerType, row, col);
                }
            }
        }
        if (!legalAttacksOnBoard.isEmpty()) {
            return Collections.unmodifiableList(legalAttacksOnBoard);
        }
        return Collections.unmodifiableList(legalMovesOnBoard);
    }

    public List<Move> calculateNextJumpOnBoard(PlayerType playerType, int destinationRow, int destinationColumn) {
        List<Move> nextJumpsOnBoard = new ArrayList<>();
        if (playerType != WHITE && playerType != BLACK) return nextJumpsOnBoard;
        FigureType playerPawn = setPlayerPawn(playerType);
        FigureType playerQueen = setPlayerQueen(playerType);
        calculateNextJump(destinationRow, destinationColumn, nextJumpsOnBoard, playerPawn, playerQueen, playerType);
        return nextJumpsOnBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board boardInstance = (Board) o;

        return Arrays.deepEquals(board, boardInstance.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
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

    public BoardField getBoardField(int row, int column) {
        return board[row][column];
    }

    public BoardField[][] getBoardArray() {
        return board;
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

    private void calculateMoves(List<Move> legalMovesOnBoard, List<Move> legalAttacksOnBoard, FigureType playerPawn, FigureType playerQueen, PlayerType playerType, int row, int col) {
        if (board[row][col].getFigure().getFigureType() == playerPawn) {
            List<Move> legalMoves = board[row][col].getFigure().calculateLegalMoves(board, playerPawn, playerType);
            if (legalMoves.isEmpty()) return;
            if (legalMoves.get(0).isPawnMajorMove()) {
                legalMovesOnBoard.addAll(legalMoves);
            } else {
                legalAttacksOnBoard.addAll(legalMoves);
            }
        } else if (board[row][col].getFigure().getFigureType() == playerQueen) {
            List<Move> legalMoves = board[row][col].getFigure().calculateLegalMoves(board, playerQueen, playerType);
            if (legalMoves.isEmpty()) return;
            if (legalMoves.get(0).isQueenMajorMove()) {
                legalMovesOnBoard.addAll(legalMoves);
            } else {
                legalAttacksOnBoard.addAll(legalMoves);
            }
        }
    }

    private void calculateNextJump(int initialRow, int initialColumn, List<Move> nextJumpsOnBoard, FigureType playerPawn, FigureType playerQueen, PlayerType playerType) {
        if (board[initialRow][initialColumn].isBoardFieldOccupied()) {
            if (board[initialRow][initialColumn].getFigure().getFigureType() == playerPawn) {
                List<Move> nextJumps = board[initialRow][initialColumn].getFigure().calculateNextJump(
                        board, playerPawn, playerType, initialRow, initialColumn);
                nextJumpsOnBoard.addAll(nextJumps);
            } else if (board[initialRow][initialColumn].getFigure().getFigureType() == playerQueen) {
                List<Move> nextJumps = board[initialRow][initialColumn].getFigure().calculateNextJump(
                        board, playerQueen, playerType, initialRow, initialColumn);
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
