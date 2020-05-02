package com.checkers.UIX.panels;

import com.checkers.engine.board.BoardField;
import com.checkers.engine.move.Move;
import com.checkers.engine.players.Player.PlayerType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static com.checkers.engine.players.Player.PlayerType.WHITE;

public class GameHistoryPanel {
    private final HBox gameHistoryPanel;
    private final VBox whiteHistoryPanel;
    private final VBox blackHistoryPanel;
    private final VBox whiteContent;
    private final VBox blackContent;
    private final ScrollPane whiteScrollPane;
    private final ScrollPane blackScrollPane;
    private final Label whiteTitleField;
    private final Label blackTitleField;
    private int whiteNotationCounter;
    private int blackNotationCounter;

    public GameHistoryPanel() {
        gameHistoryPanel = new HBox();
        whiteHistoryPanel = new VBox();
        blackHistoryPanel = new VBox();
        whiteContent = new VBox();
        blackContent = new VBox();
        whiteScrollPane = new ScrollPane();
        blackScrollPane = new ScrollPane();
        whiteTitleField = new Label();
        blackTitleField = new Label();
        gameHistoryPanel.setMaxSize(300, 708);
        gameHistoryPanel.setAlignment(Pos.TOP_CENTER);
        gameHistoryPanel.getChildren().addAll(whiteHistoryPanel, blackHistoryPanel);
    }

    public void createGameHistoryPanel() {
        gameHistoryPanel.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));

        whiteTitleField.setFont(Font.font(16));
        whiteTitleField.setTextFill(Color.SIENNA);
        whiteTitleField.setPrefSize(150, 30);
        whiteTitleField.setAlignment(Pos.CENTER);
        whiteTitleField.setBackground(new Background(new BackgroundFill(Color.CORNSILK, CornerRadii.EMPTY, Insets.EMPTY)));
        whiteTitleField.setText("WHITE MOVES");
        whiteTitleField.setOnMouseEntered(mouseEvent ->
                whiteTitleField.setBackground(new Background(new BackgroundFill(Color.CORNSILK.darker(), CornerRadii.EMPTY, Insets.EMPTY))));
        whiteTitleField.setOnMouseExited(mouseEvent ->
                whiteTitleField.setBackground(new Background(new BackgroundFill(Color.CORNSILK.brighter(), CornerRadii.EMPTY, Insets.EMPTY))));

        blackTitleField.setFont(Font.font(16));
        blackTitleField.setTextFill(Color.SIENNA);
        blackTitleField.setPrefSize(150, 30);
        blackTitleField.setAlignment(Pos.CENTER);
        blackTitleField.setBackground(new Background(new BackgroundFill(Color.CORNSILK, CornerRadii.EMPTY, Insets.EMPTY)));
        blackTitleField.setText("BLACK MOVES");
        blackTitleField.setOnMouseEntered(mouseEvent ->
                blackTitleField.setBackground(new Background(new BackgroundFill(Color.CORNSILK.darker(), CornerRadii.EMPTY, Insets.EMPTY))));
        blackTitleField.setOnMouseExited(mouseEvent ->
                blackTitleField.setBackground(new Background(new BackgroundFill(Color.CORNSILK.brighter(), CornerRadii.EMPTY, Insets.EMPTY))));

        whiteContent.setMaxSize(150, 708);
        whiteContent.setPrefSize(150, 708);
        whiteContent.setBackground(new Background(new BackgroundFill(Color.CORNSILK, CornerRadii.EMPTY, Insets.EMPTY)));
        whiteContent.setAlignment(Pos.TOP_RIGHT);

        blackContent.setMaxSize(150, 708);
        blackContent.setPrefSize(150, 708);
        blackContent.setBackground(new Background(new BackgroundFill(Color.CORNSILK, CornerRadii.EMPTY, Insets.EMPTY)));
        blackContent.setAlignment(Pos.TOP_RIGHT);

        whiteScrollPane.setContent(whiteContent);
        whiteScrollPane.setVmax(708);
        whiteScrollPane.setPrefSize(150, 708);
        whiteScrollPane.setFitToWidth(true);
        whiteScrollPane.setFitToHeight(true);
        whiteScrollPane.setPannable(true);
        whiteScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

        blackScrollPane.setContent(blackContent);
        blackScrollPane.setVmax(708);
        blackScrollPane.setPrefSize(150, 708);
        blackScrollPane.setFitToWidth(true);
        blackScrollPane.setFitToHeight(true);
        blackScrollPane.setPannable(true);
        blackScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

        whiteHistoryPanel.setMaxSize(150, 708);
        whiteHistoryPanel.setPrefSize(150, 708);
        whiteHistoryPanel.setBackground(new Background(new BackgroundFill(Color.CORNSILK, CornerRadii.EMPTY, Insets.EMPTY)));
        whiteHistoryPanel.setAlignment(Pos.TOP_RIGHT);

        whiteHistoryPanel.getChildren().addAll(whiteTitleField, whiteScrollPane);

        blackHistoryPanel.setMaxSize(150, 708);
        blackHistoryPanel.setPrefSize(150, 708);
        blackHistoryPanel.setBackground(new Background(new BackgroundFill(Color.CORNSILK, CornerRadii.EMPTY, Insets.EMPTY)));
        blackHistoryPanel.setAlignment(Pos.TOP_RIGHT);

        blackHistoryPanel.getChildren().addAll(blackTitleField, blackScrollPane);
    }

    public void resetNotationFromPanels() {
        whiteContent.getChildren().clear();
        blackContent.getChildren().clear();
        whiteHistoryPanel.getChildren().clear();
        blackHistoryPanel.getChildren().clear();
    }

    public void resetNotationCounters() {
        whiteNotationCounter = 0;
        blackNotationCounter = 0;
    }

    public void addNotationToGameHistoryPanel(PlayerType playerType, BoardField[][] board, Move move) {
        if (playerType == WHITE) {
            whiteNotationCounter++;
            whiteContent.getChildren().addAll(createNotationLabel(board, move, whiteNotationCounter));
        } else {
            blackNotationCounter++;
            blackContent.getChildren().addAll(createNotationLabel(board, move, blackNotationCounter));
        }
    }

    private Label createNotationLabel(BoardField[][] board, Move move, int notationCounter) {
        Label notationLabel = new Label();
        notationLabel.setFont(Font.font(15));
        notationLabel.setTextFill(Color.SIENNA);
        notationLabel.setPrefSize(130, 30);
        notationLabel.setAlignment(Pos.TOP_LEFT);
        notationLabel.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        notationLabel.setText(createNotationMessage(board, move, notationCounter));
        return notationLabel;
    }

    private String createNotationMessage(BoardField[][] board, Move move, int notationCounter) {
        String firstPrefix = "";
        String secondPrefix = "";
        if (board[move.initialRow][move.initialColumn].getBoardFieldNumber() < 10) {
            firstPrefix = "0";
        }
        if (board[move.destinationRow][move.destinationColumn].getBoardFieldNumber() < 10) {
            secondPrefix = "0";
        }
        if (move.isPawnMajorMove()) {
            return notationCounter + ". " + firstPrefix + board[move.initialRow][move.initialColumn].getBoardFieldNumber() +
                    " - " + secondPrefix + board[move.destinationRow][move.destinationColumn].getBoardFieldNumber();
        } else if (move.isPawnAttackMove()) {
            return notationCounter + ". " + firstPrefix + board[move.initialRow][move.initialColumn].getBoardFieldNumber() +
                    " x " + secondPrefix + board[move.destinationRow][move.destinationColumn].getBoardFieldNumber();
        } else if (move.isQueenMajorMove()) {
            return notationCounter + ". " + firstPrefix + board[move.initialRow][move.initialColumn].getBoardFieldNumber() + "(D)"
                    + " - " + secondPrefix + board[move.destinationRow][move.destinationColumn].getBoardFieldNumber() + "(D)";
        } else {
            return notationCounter + ". " + firstPrefix + board[move.initialRow][move.initialColumn].getBoardFieldNumber() + "(D)"
                    + " x " + secondPrefix + board[move.destinationRow][move.destinationColumn].getBoardFieldNumber() + "(D)";
        }
    }

    public void addAttackChainNotationToGameHistoryPanel(PlayerType playerType, BoardField[][] board, Move move) {
        if (playerType == WHITE) {
            whiteContent.getChildren().addAll(createAttackChainNotationLabel(board, move));
        } else {
            blackContent.getChildren().addAll(createAttackChainNotationLabel(board, move));
        }
    }

    private Label createAttackChainNotationLabel(BoardField[][] board, Move move) {
        Label notationLabel = new Label();
        notationLabel.setFont(Font.font(15));
        notationLabel.setTextFill(Color.CORNSILK);
        notationLabel.setPrefSize(130, 30);
        notationLabel.setAlignment(Pos.TOP_LEFT);
        notationLabel.setBackground(new Background(new BackgroundFill(Color.SIENNA, CornerRadii.EMPTY, Insets.EMPTY)));
        notationLabel.setText(createNotationMessageChainAttack(board, move));
        return notationLabel;
    }

    private String createNotationMessageChainAttack(BoardField[][] board, Move move) {
        String firstPrefix = "";
        String secondPrefix = "";
        if (board[move.initialRow][move.initialColumn].getBoardFieldNumber() < 10) {
            firstPrefix = "0";
        }
        if (board[move.destinationRow][move.destinationColumn].getBoardFieldNumber() < 10) {
            secondPrefix = "0";
        }
        if (move.isPawnAttackMove()) {
            return "x " + firstPrefix + board[move.initialRow][move.initialColumn].getBoardFieldNumber() +
                    " x " + secondPrefix + board[move.destinationRow][move.destinationColumn].getBoardFieldNumber();
        } else {
            return "x " + firstPrefix + board[move.initialRow][move.initialColumn].getBoardFieldNumber() + "(D)"
                    + " x " + secondPrefix + board[move.destinationRow][move.destinationColumn].getBoardFieldNumber() + "(D)";
        }
    }

    public HBox getGameHistoryPanel() {
        return gameHistoryPanel;
    }
}
