package com.checkers;

import com.checkers.engine.board.Board;
import com.checkers.engine.board.Move;
import com.checkers.engine.playres.PlayerType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static com.checkers.engine.playres.PlayerType.BLACK;
import static com.checkers.engine.playres.PlayerType.WHITE;

public class CheckersApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    CheckersBoard board;
    private Button newGameButton;
    private Button surrenderButton;
    private Label message;
    private final Image background = new Image("image/background.jpg");
    private final Image BP = new Image("figures/BP.png");
    private final Image BQ = new Image("figures/BQ.png");
    private final Image WP = new Image("figures/WP.png");
    private final Image WQ = new Image("figures/WQ.png");

    @Override
    public void start(Stage primaryStage) {
        message = new Label("Click \"New Game\" to begin.");
        message.setTextFill(Color.rgb(100, 255, 100)); // Light green.
        message.setFont(Font.font(null, FontWeight.BOLD, 18));
        newGameButton = new Button("New Game");
        surrenderButton = new Button("Surrender");
        board = new CheckersBoard(); // a subclass of Canvas, defined below
        board.drawBoard();  // draws the content of the checkerboard
        newGameButton.setOnAction(e -> board.doNewGame());
        surrenderButton.setOnAction(e -> board.doSurrender());
        board.setOnMousePressed(e -> board.mousePressed(e));
        newGameButton.relocate(1270, 120);
        surrenderButton.relocate(1270, 200);
        surrenderButton.setManaged(false);
        surrenderButton.resize(100, 30);
        newGameButton.setManaged(false);
        newGameButton.resize(100, 30);
        BackgroundSize backgroundSize = new BackgroundSize(720, 720, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        BorderPane root = new BorderPane();
        root.setBackground(background);
        root.setCenter(board);
        root.setBottom(message);
        root.setPrefWidth(1600);
        root.setPrefHeight(900);
        root.getChildren().addAll(newGameButton, surrenderButton);
        root.setStyle("-fx-border-color: darkred; -fx-border-width:3");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Checkers Application!");
        primaryStage.show();
    }

    private class CheckersBoard extends Canvas {
        Board board; // The data for the checkers board is kept here.
        boolean gameInProgress; // Is a game currently in progress?
        PlayerType currentPlayer;      // Whose turn is it now?  The possible values
        int selectedRow, selectedCol;   // If the current player has selected a piece to
        Move[] legalMoves;  // An array containing the legal moves for the

        CheckersBoard() {
            super(800, 800);
            board = new Board();
            doNewGame();
        }

        void doNewGame() {
            if (gameInProgress) {
                // This should not be possible, but it doesn't hurt to check.
                message.setText("Finish the current game first!");
                return;
            }
            board.setUpGame();   // Set up the pieces.
            currentPlayer = WHITE;   // WHITE moves first.
            legalMoves = board.getLegalMoves(currentPlayer);  // Get WHITE's legal moves.
            selectedRow = -1;   // WHITE has not yet selected a piece to move.
            message.setText("WHITE:  Make your move.");
            gameInProgress = true;
            newGameButton.setDisable(true);
            surrenderButton.setDisable(false);
            drawBoard();
        }

        void doSurrender() {
            if (!gameInProgress) {  // Should be impossible.
                message.setText("There is no game in progress!");
                return;
            }
            if (currentPlayer == WHITE)
                gameOver("WHITE surrender.  BLACK wins.");
            else
                gameOver("BLACK surrender.  WHITE wins.");
        }

        void gameOver(String str) {
            message.setText(str);
            newGameButton.setDisable(false);
            surrenderButton.setDisable(true);
            gameInProgress = false;
        }

        void doClickSquare(int row, int col) {
            for (int i = 0; i < legalMoves.length; i++)
                if (legalMoves[i].fromRow == row && legalMoves[i].fromCol == col) {
                    selectedRow = row;
                    selectedCol = col;
                    if (currentPlayer == WHITE)
                        message.setText("WHITE:  Make your move.");
                    else
                        message.setText("BLACK:  Make your move.");
                    drawBoard();
                    return;
                }
            if (selectedRow < 0) {
                message.setText("Click the piece you want to move.");
                return;
            }
            for (int i = 0; i < legalMoves.length; i++)
                if (legalMoves[i].fromRow == selectedRow && legalMoves[i].fromCol == selectedCol
                        && legalMoves[i].toRow == row && legalMoves[i].toCol == col) {
                    doMakeMove(legalMoves[i]);
                    return;
                }
            message.setText("Click the square you want to move to.");
        }

        void doMakeMove(Move move) {
            board.makeMove(move);
            if (move.isJump()) {
                legalMoves = board.getLegalJumpsFrom(currentPlayer, move.toRow, move.toCol);
                if (legalMoves != null) {
                    if (currentPlayer == WHITE)
                        message.setText("WHITE:  You must continue jumping.");
                    else
                        message.setText("BLACK:  You must continue jumping.");
                    selectedRow = move.toRow;  // Since only one piece can be moved, select it.
                    selectedCol = move.toCol;
                    drawBoard();
                    return;
                }
            }
            if (currentPlayer == WHITE) {
                currentPlayer = BLACK;
                legalMoves = board.getLegalMoves(currentPlayer);
                if (legalMoves == null)
                    gameOver("BLACK has no moves.  WHITE wins.");
                else if (legalMoves[0].isJump())
                    message.setText("BLACK:  Make your move.  You must jump.");
                else
                    message.setText("BLACK:  Make your move.");
            } else {
                currentPlayer = WHITE;
                legalMoves = board.getLegalMoves(currentPlayer);
                if (legalMoves == null)
                    gameOver("WHITE has no moves.  BLACK wins.");
                else if (legalMoves[0].isJump())
                    message.setText("WHITE:  Make your move.  You must jump.");
                else
                    message.setText("WHITE:  Make your move.");
            }
            selectedRow = -1;
            if (legalMoves != null) {
                boolean sameStartSquare = true;
                for (int i = 1; i < legalMoves.length; i++)
                    if (legalMoves[i].fromRow != legalMoves[0].fromRow
                            || legalMoves[i].fromCol != legalMoves[0].fromCol) {
                        sameStartSquare = false;
                        break;
                    }
                if (sameStartSquare) {
                    selectedRow = legalMoves[0].fromRow;
                    selectedCol = legalMoves[0].fromCol;
                }
            }
            drawBoard();
        }

        public void drawBoard() {
            GraphicsContext g = getGraphicsContext2D();
            g.setFont(Font.font(18));
            g.setStroke(Color.DARKRED);
            g.setLineWidth(4);
            g.strokeRect(1, 1, 702, 702);
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    if (row % 2 == col % 2)
                        g.setFill(Color.CORNSILK);
                    else
                        g.setFill(Color.SIENNA);
                    g.fillRect(2 + col * 70, 2 + row * 70, 70, 70);
                    switch (board.figureAt(row, col)) {
                        case WHITE_PAWN:
                            g.drawImage(WP, 12.5 + col * 70, 12.5 + row * 70, 50, 50);
                            break;
                        case BLACK_PAWN:
                            g.drawImage(BP, 12.5 + col * 70, 12.5 + row * 70, 50, 50);
                            break;
                        case WHITE_QUEEN:
                            g.drawImage(WQ, 12.5 + col * 70, 12.5 + row * 70, 50, 50);
                            break;
                        case BLACK_QUEEN:
                            g.drawImage(BQ, 12.5 + col * 70, 12.5 + row * 70, 50, 50);
                            break;
                    }
                }
            }
            if (gameInProgress) {
                g.setStroke(Color.CYAN);
                g.setLineWidth(4);
                for (int i = 0; i < legalMoves.length; i++) {
                    g.strokeRect(2 + legalMoves[i].fromCol * 70, 2 + legalMoves[i].fromRow * 70, 70, 70);
                }
                if (selectedRow >= 0) {
                    g.setStroke(Color.YELLOW);
                    g.setLineWidth(4);
                    g.strokeRect(2 + selectedCol * 70, 2 + selectedRow * 70, 70, 70);
                    g.setStroke(Color.LIME);
                    g.setLineWidth(4);
                    for (int i = 0; i < legalMoves.length; i++) {
                        if (legalMoves[i].fromCol == selectedCol && legalMoves[i].fromRow == selectedRow) {
                            g.strokeRect(2 + legalMoves[i].toCol * 70, 2 + legalMoves[i].toRow * 70, 70, 70);
                        }
                    }
                }
            }
        }

        public void mousePressed(MouseEvent mouseEvent) {
            if (!gameInProgress)
                message.setText("Click \"New Game\" to start a new game.");
            else {
                int col = (int) ((mouseEvent.getX() - 2) / 70);
                int row = (int) ((mouseEvent.getY() - 2) / 70);
                if (col >= 0 && col < 10 && row >= 0 && row < 10)
                    doClickSquare(row, col);
            }
        }
    }
}
