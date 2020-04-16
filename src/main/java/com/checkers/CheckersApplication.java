package com.checkers;

import com.checkers.UIX.CheckersBoard;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static com.checkers.UIX.Images.BACKGROUND;

public class CheckersApplication extends Application {
    private CheckersBoard board;
    private Button newGameButton;
    private Button surrenderButton;
    private Label message;

    @Override
    public void start(Stage primaryStage) {
        initializeMessageField();
        initializeButtons();
        board = new CheckersBoard(newGameButton, surrenderButton, message);
        Background background = initializeBackground();
        BorderPane root = initializeRoot(background);
        initializeScene(primaryStage, root);
        mouseEvents();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initializeMessageField() {
        message = new Label("Click \"New Game\" to begin.");
        message.setTextFill(Color.rgb(100, 255, 100));
        message.setFont(Font.font(null, FontWeight.BOLD, 18));
    }

    private void initializeButtons() {
        newGameButton = new Button("New Game");
        newGameButton.relocate(1270, 120);
        newGameButton.setManaged(false);
        newGameButton.resize(100, 30);
        surrenderButton = new Button("Surrender");
        surrenderButton.relocate(1270, 200);
        surrenderButton.setManaged(false);
        surrenderButton.resize(100, 30);
    }

    private void mouseEvents() {
        newGameButton.setOnAction(mouseEvent -> board.createGame());
        surrenderButton.setOnAction(mouseEvent -> board.surrenderGame());
        board.setOnMousePressed(mouseEvent -> board.mousePressed(mouseEvent));
    }

    private Background initializeBackground() {
        BackgroundSize backgroundSize = new BackgroundSize(1600, 900, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(BACKGROUND, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize);
        return new Background(backgroundImage);
    }

    private BorderPane initializeRoot(Background background) {
        BorderPane root = new BorderPane();
        root.setBackground(background);
        root.setCenter(board);
        root.setBottom(message);
        root.setPrefWidth(1600);
        root.setPrefHeight(900);
        root.getChildren().addAll(newGameButton, surrenderButton);
        root.setStyle("-fx-border-color: darkred; -fx-border-width:3");
        return root;
    }

    private void initializeScene(Stage primaryStage, BorderPane root) {
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Checkers Application!");
        primaryStage.show();
    }
}
