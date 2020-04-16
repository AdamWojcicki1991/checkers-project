package com.checkers.UIX;

import com.checkers.engine.board.BoardProcessor;
import com.checkers.engine.board.Move;
import com.checkers.engine.playres.PlayerType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

import static com.checkers.UIX.Images.*;
import static com.checkers.engine.playres.PlayerType.BLACK;
import static com.checkers.engine.playres.PlayerType.WHITE;
import static com.checkers.engine.utils.Constants.*;

public class CheckersBoard extends Canvas {
    private final BoardProcessor boardProcessor;
    private final Button newGameButton, surrenderButton;
    private final Label message;
    private PlayerType currentPlayer;
    private List<Move> legalMoves;
    private boolean gameInProgress;
    private int selectedRow, selectedCol;

    public CheckersBoard(final Button newGameButton, final Button surrenderButton, final Label message) {
        super(708, 708);
        this.newGameButton = newGameButton;
        this.surrenderButton = surrenderButton;
        this.message = message;
        this.boardProcessor = new BoardProcessor();
        createGame();
    }

    public void createGame() {
        initGame();
        computeLegalMoves();
        drawBoard();
    }

    public void surrenderGame() {
        if (currentPlayer == WHITE) {
            gameOver("WHITE surrender.  BLACK wins.");
        } else {
            gameOver("BLACK surrender.  WHITE wins.");
        }
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (!gameInProgress) {
            printTextInMessageField("Click \"New Game\" to start a new game.");
        } else {
            int clickedColumn = (int) ((mouseEvent.getX() - 4) / BOARD_FIELD_SIZE);
            int clickedRow = (int) ((mouseEvent.getY() - 4) / BOARD_FIELD_SIZE);
            if (clickedColumn >= 0 && clickedColumn < COLUMN_COUNT && clickedRow >= 0 && clickedRow < ROW_COUNT)
                actionClickSquare(clickedRow, clickedColumn);
        }
    }

    private void actionClickSquare(int clickedRow, int clickedColumn) {
        for (Move move : legalMoves) {
            if (move.initialRow == clickedRow && move.initialColumn == clickedColumn) {
                selectedRow = clickedRow;
                selectedCol = clickedColumn;
                if (currentPlayer == WHITE) {
                    printTextInMessageField("WHITE:  Make your move.");
                } else {
                    printTextInMessageField("BLACK:  Make your move.");
                }
                drawBoard();
                return;
            }
        }
        if (selectedRow < 0) {
            printTextInMessageField("Click the figure you want to move.");
            return;
        }
        for (Move legalMove : legalMoves) {
            if (legalMove.initialRow == selectedRow && legalMove.initialColumn == selectedCol && legalMove.destinationRow == clickedRow && legalMove.destinationColumn == clickedColumn) {
                actionMakeMove(legalMove);
                return;
            }
        }
        printTextInMessageField("Click the square you want to move to.");
    }

    private void actionMakeMove(Move move) {
        boardProcessor.makeMove(move);
        if (move.isJump()) {
            legalMoves = boardProcessor.getLegalJumps(currentPlayer, move.destinationRow, move.destinationColumn);
            if (legalMoves != null) {
                if (currentPlayer == WHITE) {
                    printTextInMessageField("WHITE:  You must continue jumping.");
                } else {
                    printTextInMessageField("BLACK:  You must continue jumping.");
                }
                selectedRow = move.destinationRow;
                selectedCol = move.destinationColumn;
                drawBoard();
                return;
            }
        }
        if (currentPlayer == WHITE) {
            currentPlayer = BLACK;
            computeLegalMoves();
            if (legalMoves == null) {
                gameOver("BLACK has no moves.  WHITE wins.");
            } else if (legalMoves.get(0).isJump()) {
                printTextInMessageField("BLACK:  Make your move.  You must jump.");
            } else {
                printTextInMessageField("BLACK:  Make your move.");
            }
        } else {
            currentPlayer = WHITE;
            computeLegalMoves();
            if (legalMoves == null) {
                gameOver("WHITE has no moves.  BLACK wins.");
            } else if (legalMoves.get(0).isJump()) {
                printTextInMessageField("WHITE:  Make your move.  You must jump.");
            } else {
                printTextInMessageField("WHITE:  Make your move.");
            }
        }
        selectedRow = -1;
        if (legalMoves != null) {
            boolean sameStartSquare = true;
            for (int i = 1; i < legalMoves.size(); i++) {
                if (legalMoves.get(i).initialRow != legalMoves.get(0).initialRow || legalMoves.get(i).initialColumn != legalMoves.get(0).initialColumn) {
                    sameStartSquare = false;
                    break;
                }
            }
            if (sameStartSquare) {
                selectedRow = legalMoves.get(0).initialRow;
                selectedCol = legalMoves.get(0).initialColumn;
            }
        }
        drawBoard();
    }

    private void drawBoard() {
        int fieldNumber = 1;
        GraphicsContext graphics = initGraphicsContext();
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COLUMN_COUNT; col++) {
                if (row % 2 == col % 2) {
                    drawBoardField(graphics, row, col, Color.CORNSILK);
                } else {
                    drawBoardField(graphics, row, col, Color.SIENNA);
                    drawFieldNumber(fieldNumber++, graphics, row, col);
                }
                drawFigures(graphics, row, col);
            }
        }
        if (gameInProgress) {
            for (Move legalMove : legalMoves) {
                drawHelpContur(graphics, Color.VIOLET, legalMove.initialColumn, legalMove.initialRow);
            }
            if (selectedRow >= 0) {
                drawHelpContur(graphics, Color.YELLOW, selectedCol, selectedRow);
                for (Move legalMove : legalMoves) {
                    if (legalMove.initialColumn == selectedCol && legalMove.initialRow == selectedRow) {
                        drawHelpContur(graphics, Color.LIME, legalMove.destinationColumn, legalMove.destinationRow);
                    }
                }
            }
        }
    }

    private void initGame() {
        boardProcessor.initFigures();
        currentPlayer = WHITE;
        selectedRow = -1;
        printTextInMessageField("WHITE:  Make your move.");
        gameInProgress = true;
        newGameButton.setDisable(true);
        surrenderButton.setDisable(false);
    }

    private void gameOver(String text) {
        printTextInMessageField(text);
        newGameButton.setDisable(false);
        surrenderButton.setDisable(true);
        gameInProgress = false;
    }

    private void computeLegalMoves() {
        legalMoves = boardProcessor.getLegalMoves(currentPlayer);
    }

    private void drawHelpContur(GraphicsContext graphics, Color color, int fromCol, int fromRow) {
        graphics.setStroke(color);
        graphics.setLineWidth(3);
        graphics.strokeRect(4 + fromCol * BOARD_FIELD_SIZE, 4 + fromRow * BOARD_FIELD_SIZE, BOARD_FIELD_SIZE, BOARD_FIELD_SIZE);
    }

    private void drawFigures(GraphicsContext graphics, int row, int col) {
        switch (boardProcessor.getFigureFromBoard(row, col)) {
            case WHITE_PAWN:
                graphics.drawImage(WP, 14 + col * BOARD_FIELD_SIZE, 14 + row * BOARD_FIELD_SIZE, 50, 50);
                break;
            case BLACK_PAWN:
                graphics.drawImage(BP, 14 + col * BOARD_FIELD_SIZE, 14 + row * BOARD_FIELD_SIZE, 50, 50);
                break;
            case WHITE_QUEEN:
                graphics.drawImage(WQ, 14 + col * BOARD_FIELD_SIZE, 14 + row * BOARD_FIELD_SIZE, 50, 50);
                break;
            case BLACK_QUEEN:
                graphics.drawImage(BQ, 14 + col * BOARD_FIELD_SIZE, 14 + row * BOARD_FIELD_SIZE, 50, 50);
                break;
        }
    }

    private void drawFieldNumber(int fieldNumber, GraphicsContext graphics, int row, int col) {
        graphics.setFill(Color.CORNSILK);
        graphics.fillText("" + fieldNumber, 7 + col * BOARD_FIELD_SIZE, 18 + row * BOARD_FIELD_SIZE);
    }

    private void drawBoardField(GraphicsContext graphics, int row, int col, Color color) {
        graphics.setFill(color);
        graphics.fillRect(4 + col * BOARD_FIELD_SIZE, 4 + row * BOARD_FIELD_SIZE, BOARD_FIELD_SIZE, BOARD_FIELD_SIZE);
    }

    private GraphicsContext initGraphicsContext() {
        GraphicsContext graphics = getGraphicsContext2D();
        graphics.setFont(Font.font(14));
        graphics.setFill(Color.CORNSILK);
        graphics.setStroke(Color.GREY);
        graphics.setLineWidth(8);
        graphics.strokeRect(0, 0, 708, 708);
        return graphics;
    }

    private void printTextInMessageField(String text) {
        message.setText(text);
    }
}
