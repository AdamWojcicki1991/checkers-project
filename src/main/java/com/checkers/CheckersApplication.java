package com.checkers;

import com.checkers.UIX.CheckersBoard;
import com.checkers.UIX.GameHistoryPanel;
import com.checkers.UIX.TakenFigurePanel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static com.checkers.UIX.ControlsInitializer.*;
import static com.checkers.UIX.UIContent.BACKGROUND_MAIN_VIEW;

public class CheckersApplication extends Application {
    private MenuBar menuBar;
    private Label messageWindow;
    private CheckersBoard board;
    private TakenFigurePanel takenFigurePanel;
    private GameHistoryPanel gameHistoryPanel;

    @Override
    public void start(Stage primaryStage) {
        messageWindow = createMessageWindow();
        takenFigurePanel = new TakenFigurePanel();
        gameHistoryPanel = new GameHistoryPanel();
        board = new CheckersBoard(messageWindow, takenFigurePanel, gameHistoryPanel);
        menuBar = createMenuBar(board);
        Background background = initializeBackground(1600, 900, BACKGROUND_MAIN_VIEW);
        BorderPane root = initializeRoot(background);
        initializeScene(primaryStage, root);
        mouseEvents();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void mouseEvents() {
        board.setOnMousePressed(mouseEvent -> board.mousePressed(mouseEvent));
    }

    private BorderPane initializeBorderPane() {
        BorderPane root = new BorderPane();
        BorderPane.setMargin(messageWindow, new Insets(0, 0, 25, 0));
        BorderPane.setMargin(takenFigurePanel, new Insets(0, 0, 0, 50));
        BorderPane.setMargin(gameHistoryPanel, new Insets(0, 50, 0, 0));
        BorderPane.setAlignment(messageWindow, Pos.BOTTOM_CENTER);
        BorderPane.setAlignment(menuBar, Pos.TOP_CENTER);
        BorderPane.setAlignment(board, Pos.CENTER);
        BorderPane.setAlignment(takenFigurePanel, Pos.CENTER_LEFT);
        BorderPane.setAlignment(gameHistoryPanel, Pos.CENTER_RIGHT);
        return root;
    }

    private BorderPane initializeRoot(Background background) {
        BorderPane root = initializeBorderPane();
        root.setBackground(background);
        root.setCenter(board);
        root.setTop(menuBar);
        root.setBottom(messageWindow);
        root.setLeft(takenFigurePanel);
        root.setRight(gameHistoryPanel);
        root.setPrefWidth(1600);
        root.setPrefHeight(900);
        return root;
    }

    private void initializeScene(Stage primaryStage, BorderPane root) {
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Checkers Application");
        primaryStage.show();
    }
}
