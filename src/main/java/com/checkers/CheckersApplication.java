package com.checkers;

import com.checkers.controler.GameBoard;
import com.checkers.UIX.panels.GameHistoryPanel;
import com.checkers.UIX.panels.TakenFigurePanel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static com.checkers.UIX.UIXContent.BACKGROUND_MAIN_VIEW;
import static com.checkers.UIX.panels.ControlsInitializer.*;

public class CheckersApplication extends Application {
    private MenuBar menuBar;
    private Label messageWindow;
    private GameBoard gameBoard;
    private TakenFigurePanel takenFigurePanel;
    private GameHistoryPanel gameHistoryPanel;

    @Override
    public void start(Stage primaryStage) {
        messageWindow = createMessageWindow();
        takenFigurePanel = new TakenFigurePanel();
        gameHistoryPanel = new GameHistoryPanel();
        gameBoard = new GameBoard(messageWindow, takenFigurePanel, gameHistoryPanel);
        menuBar = createMenuBar(gameBoard);
        Background background = initializeBackground(1600, 900, BACKGROUND_MAIN_VIEW);
        BorderPane root = initializeRoot(background);
        initializeScene(primaryStage, root);
        mouseEvents();
    }

    public static void main(String[] args) {//
        launch(args);
    }

    private void mouseEvents() {
        gameBoard.setOnMousePressed(mouseEvent -> gameBoard.mousePressed(mouseEvent));
    }

    private void initializeBorderPane() {
        BorderPane.setMargin(messageWindow, new Insets(0, 0, 25, 0));
        BorderPane.setMargin(takenFigurePanel.getTakenFigurePanel(), new Insets(0, 0, 0, 50));
        BorderPane.setMargin(gameHistoryPanel.getGameHistoryPanel(), new Insets(0, 50, 0, 0));
        BorderPane.setAlignment(messageWindow, Pos.BOTTOM_CENTER);
        BorderPane.setAlignment(menuBar, Pos.TOP_CENTER);
        BorderPane.setAlignment(gameBoard, Pos.CENTER);
        BorderPane.setAlignment(takenFigurePanel.getTakenFigurePanel(), Pos.CENTER_LEFT);
        BorderPane.setAlignment(gameHistoryPanel.getGameHistoryPanel(), Pos.CENTER_RIGHT);
    }

    private BorderPane initializeRoot(Background background) {
        initializeBorderPane();
        BorderPane root = new BorderPane();
        root.setBackground(background);
        root.setCenter(gameBoard);
        root.setTop(menuBar);
        root.setBottom(messageWindow);
        root.setLeft(takenFigurePanel.getTakenFigurePanel());
        root.setRight(gameHistoryPanel.getGameHistoryPanel());
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
