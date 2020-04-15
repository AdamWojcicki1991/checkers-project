package com.checkers.engine.board;

import com.checkers.engine.playres.PlayerType;

import java.util.ArrayList;

import static com.checkers.engine.board.FigureType.*;
import static com.checkers.engine.playres.PlayerType.BLACK;
import static com.checkers.engine.playres.PlayerType.WHITE;

public class Board {
    FigureType[][] board;

    public Board() {
        board = new FigureType[10][10];
        setUpGame();
    }

    public void setUpGame() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (row < 4 && (row + col) % 2 != 0) {
                    board[row][col] = BLACK_PAWN;
                } else if (row > 5 && (row + col) % 2 != 0) {
                    board[row][col] = WHITE_PAWN;
                } else {
                    board[row][col] = EMPTY;
                }
            }
        }
    }

    public FigureType figureAt(int row, int col) {
        return board[row][col];
    }

    public void makeMove(Move move) {
        makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
    }

    void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = EMPTY;
        if (fromRow - toRow == 2 || fromRow - toRow == -2) {
            int jumpRow = (fromRow + toRow) / 2;  // Row of the jumped piece.
            int jumpCol = (fromCol + toCol) / 2;  // Column of the jumped piece.
            board[jumpRow][jumpCol] = EMPTY;
        }
        if (toRow == 0 && board[toRow][toCol] == WHITE_PAWN)
            board[toRow][toCol] = WHITE_QUEEN;
        if (toRow == 9 && board[toRow][toCol] == BLACK_PAWN)
            board[toRow][toCol] = BLACK_QUEEN;
    }

    public Move[] getLegalMoves(PlayerType player) {
        if (player != WHITE && player != BLACK)
            return null;
        FigureType playerQueen;  // The constant representing a Queen belonging to player.
        FigureType playerPawn;  // The constant representing a Pawn belonging to player.
        if (player == WHITE) {
            playerQueen = WHITE_QUEEN;
            playerPawn = WHITE_PAWN;
        } else {
            playerQueen = BLACK_QUEEN;
            playerPawn = BLACK_PAWN;
        }
        ArrayList<Move> moves = new ArrayList<>();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (board[row][col] == playerPawn || board[row][col] == playerQueen) {
                    if (canJump(playerPawn, row, col, row + 1, col + 1, row + 2, col + 2))
                        moves.add(new Move(row, col, row + 2, col + 2));
                    if (canJump(playerPawn, row, col, row - 1, col + 1, row - 2, col + 2))
                        moves.add(new Move(row, col, row - 2, col + 2));
                    if (canJump(playerPawn, row, col, row + 1, col - 1, row + 2, col - 2))
                        moves.add(new Move(row, col, row + 2, col - 2));
                    if (canJump(playerPawn, row, col, row - 1, col - 1, row - 2, col - 2))
                        moves.add(new Move(row, col, row - 2, col - 2));
                }
            }
        }
        if (moves.size() == 0) {
            for (int row = 0; row < 10
                    ; row++) {
                for (int col = 0; col < 10
                        ; col++) {
                    if (board[row][col] == playerPawn || board[row][col] == playerQueen) {
                        if (canMove(playerPawn, row, col, row + 1, col + 1))
                            moves.add(new Move(row, col, row + 1, col + 1));
                        if (canMove(playerPawn, row, col, row - 1, col + 1))
                            moves.add(new Move(row, col, row - 1, col + 1));
                        if (canMove(playerPawn, row, col, row + 1, col - 1))
                            moves.add(new Move(row, col, row + 1, col - 1));
                        if (canMove(playerPawn, row, col, row - 1, col - 1))
                            moves.add(new Move(row, col, row - 1, col - 1));
                    }
                }
            }
        }
        if (moves.size() == 0)
            return null;
        else {
            Move[] moveArray = new Move[moves.size()];
            for (int i = 0; i < moves.size(); i++)
                moveArray[i] = moves.get(i);
            return moveArray;
        }
    }

    public Move[] getLegalJumpsFrom(PlayerType player, int row, int col) {
        if (player != WHITE && player != BLACK)
            return null;
        FigureType playerQueen;  // The constant representing a Queen belonging to player.
        FigureType playerPawn;  // The constant representing a Pawn belonging to player.
        if (player == WHITE) {
            playerQueen = WHITE_QUEEN;
            playerPawn = WHITE_PAWN;
        } else {
            playerQueen = BLACK_QUEEN;
            playerPawn = BLACK_PAWN;
        }
        ArrayList<Move> moves = new ArrayList<>();  // The legal jumps will be stored in this list.
        if (board[row][col] == playerPawn || board[row][col] == playerQueen) {
            if (canJump(playerPawn, row, col, row + 1, col + 1, row + 2, col + 2))
                moves.add(new Move(row, col, row + 2, col + 2));
            if (canJump(playerPawn, row, col, row - 1, col + 1, row - 2, col + 2))
                moves.add(new Move(row, col, row - 2, col + 2));
            if (canJump(playerPawn, row, col, row + 1, col - 1, row + 2, col - 2))
                moves.add(new Move(row, col, row + 2, col - 2));
            if (canJump(playerPawn, row, col, row - 1, col - 1, row - 2, col - 2))
                moves.add(new Move(row, col, row - 2, col - 2));
        }
        if (moves.size() == 0)
            return null;
        else {
            Move[] moveArray = new Move[moves.size()];
            for (int i = 0; i < moves.size(); i++)
                moveArray[i] = moves.get(i);
            return moveArray;
        }
    }

    private boolean canJump(FigureType playerFigure, int r1, int c1, int r2, int c2, int r3, int c3) {
        if (r3 < 0 || r3 >= 10 || c3 < 0 || c3 >= 10)
            return false;  // (r3,c3) is off the board.
        if (board[r3][c3] != EMPTY)
            return false;  // (r3,c3) already contains a piece.
        if (playerFigure == WHITE_PAWN) {
            if (board[r1][c1] == WHITE_PAWN && r3 > r1)
                return false;  // Regular red piece can only move up.
            if (board[r2][c2] != BLACK_PAWN && board[r2][c2] != BLACK_QUEEN)
                return false;  // There is no black piece to jump.
            return true;  // The jump is legal.
        } else {
            if (board[r1][c1] == BLACK_PAWN && r3 < r1)
                return false;  // Regular black piece can only move downn.
            if (board[r2][c2] != WHITE_PAWN && board[r2][c2] != WHITE_QUEEN)
                return false;  // There is no red piece to jump.
            return true;  // The jump is legal.
        }
    }

    private boolean canMove(FigureType playerFigure, int r1, int c1, int r2, int c2) {
        if (r2 < 0 || r2 >= 10 || c2 < 0 || c2 >= 10)
            return false;  // (r2,c2) is off the board.
        if (board[r2][c2] != EMPTY)
            return false;  // (r2,c2) already contains a piece.
        if (playerFigure == WHITE_PAWN) {
            if (board[r1][c1] == WHITE_PAWN && r2 > r1)
                return false;  // Regular red piece can only move down.
            return true;  // The move is legal.
        } else {
            if (board[r1][c1] == BLACK_PAWN && r2 < r1)
                return false;  // Regular black piece can only move up.
            return true;  // The move is legal.
        }
    }
}
