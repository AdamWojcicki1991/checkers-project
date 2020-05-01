package com.checkers.UIX.panels;

import com.checkers.UIX.CheckersBoard;
import com.checkers.UIX.UIXContent;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static com.checkers.UIX.UIXContent.BACKGROUND_MENU_BAR;
import static com.checkers.engine.strategy.ai.DifficultyLevel.*;

public class ControlsInitializer {

    public static Label createMessageWindow() {
        Label message = new Label("Click on Top Menu Bar \"File -> New Game\" to start a new game or \"Help -> Game Manual\"");
        message.setTextFill(Color.SIENNA);
        message.setFont(Font.font(null, FontWeight.BOLD, 20));
        message.setAlignment(Pos.CENTER);
        message.setPrefWidth(850);
        message.setPrefHeight(78);
        message.setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, null, new BorderWidths(4))));
        message.setBackground(new Background(new BackgroundFill(Color.CORNSILK, CornerRadii.EMPTY, Insets.EMPTY)));
        return message;
    }

    public static Background initializeBackground(int width, int height, Image image) {
        BackgroundSize backgroundSize = new BackgroundSize(width, height, false, false, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                backgroundSize);
        return new Background(backgroundImage);
    }

    private static void createPopUpWindow(String title, String content) {
        Stage popUpGameManual = new Stage();
        popUpGameManual.initModality(Modality.APPLICATION_MODAL);
        popUpGameManual.setTitle(title);

        Label titleField = new Label();
        titleField.setFont(Font.font(22));
        titleField.setTextFill(Color.CORNSILK);
        titleField.setText(title);

        Label textField = new Label();
        textField.setFont(Font.font(18));
        textField.setTextFill(Color.CORNSILK);
        textField.setText(content);

        Button exitButton = new Button("Close");
        exitButton.setOnAction(mouseEvent -> popUpGameManual.close());

        BorderPane layout = new BorderPane();
        BorderPane.setAlignment(exitButton, Pos.BOTTOM_CENTER);
        BorderPane.setMargin(exitButton, new Insets(30, 0, 50, 0));
        BorderPane.setAlignment(titleField, Pos.TOP_CENTER);
        BorderPane.setMargin(titleField, new Insets(30, 0, 0, 0));
        BorderPane.setAlignment(textField, Pos.CENTER);
        Background background = initializeBackground(600, 600, BACKGROUND_MENU_BAR);
        layout.setBackground(background);
        layout.setPrefHeight(600);
        layout.setPrefWidth(600);
        layout.setTop(titleField);
        layout.setCenter(textField);
        layout.setBottom(exitButton);

        Scene scene = new Scene(layout);
        popUpGameManual.setScene(scene);
        popUpGameManual.showAndWait();
    }

    private static void createPreferencesWindow(CheckersBoard board) {
        Stage popUpPreferences = new Stage();
        popUpPreferences.initModality(Modality.APPLICATION_MODAL);
        popUpPreferences.setTitle("PREFERENCES");

        Label difficultyTitle = new Label();
        difficultyTitle.setFont(Font.font(20));
        difficultyTitle.setTextFill(Color.CORNSILK);
        difficultyTitle.setText("Select AI difficulty");

        ToggleGroup aiGroup = new ToggleGroup();
        RadioButton randomMove = new RadioButton("EASY");
        RadioButton miniMaxMedium = new RadioButton("MEDIUM");
        RadioButton miniMaxHard = new RadioButton("HARD");

        randomMove.setSelected(true);
        board.setDifficultyLevel(EASY);
        randomMove.setFont(Font.font(16));
        randomMove.setTextFill(Color.CORNSILK);
        randomMove.setOnAction(mouseEvent -> board.setDifficultyLevel(EASY));

        miniMaxMedium.setFont(Font.font(16));
        miniMaxMedium.setTextFill(Color.CORNSILK);
        miniMaxMedium.setOnAction(mouseEvent -> board.setDifficultyLevel(MEDIUM));

        miniMaxHard.setFont(Font.font(16));
        miniMaxHard.setTextFill(Color.CORNSILK);
        miniMaxHard.setOnAction(mouseEvent -> board.setDifficultyLevel(HARD));

        randomMove.setToggleGroup(aiGroup);
        miniMaxMedium.setToggleGroup(aiGroup);
        miniMaxHard.setToggleGroup(aiGroup);

        VBox radioButtonsAIPanel = new VBox();
        radioButtonsAIPanel.setAlignment(Pos.TOP_LEFT);
        VBox.setMargin(randomMove, new Insets(0, 0, 20, 0));
        VBox.setMargin(miniMaxMedium, new Insets(0, 0, 20, 0));
        VBox.setMargin(miniMaxHard, new Insets(0, 0, 20, 0));
        VBox.setMargin(difficultyTitle, new Insets(0, 0, 30, 0));

        radioButtonsAIPanel.getChildren().addAll(difficultyTitle, randomMove, miniMaxMedium, miniMaxHard);

        Label titleField = new Label();
        titleField.setFont(Font.font(22));
        titleField.setTextFill(Color.CORNSILK);
        titleField.setText("PREFERENCES");

        Label vBoxTitle = new Label();
        vBoxTitle.setFont(Font.font(20));
        vBoxTitle.setTextFill(Color.CORNSILK);
        vBoxTitle.setText("Select players");

        Label whiteText = new Label();
        whiteText.setFont(Font.font(18));
        whiteText.setTextFill(Color.CORNSILK);
        whiteText.setText("White player");

        Label blackText = new Label();
        blackText.setFont(Font.font(18));
        blackText.setTextFill(Color.CORNSILK);
        blackText.setText("Black player");

        ToggleGroup whiteGroup = new ToggleGroup();
        ToggleGroup blackGroup = new ToggleGroup();
        RadioButton whiteComputer = new RadioButton("Computer");
        RadioButton whiteHuman = new RadioButton("Human");
        RadioButton blackComputer = new RadioButton("Computer");
        RadioButton blackHuman = new RadioButton("Human");

        whiteHuman.setSelected(true);
        board.setWhitePlayerAsComputer(false);
        whiteHuman.setFont(Font.font(16));
        whiteHuman.setTextFill(Color.CORNSILK);
        whiteHuman.setOnAction(mouseEvent -> {
            board.setWhitePlayerAsComputer(false);
            if (blackHuman.isSelected()) {
                randomMove.setDisable(true);
                miniMaxMedium.setDisable(true);
                miniMaxHard.setDisable(true);
            }
        });

        blackHuman.setSelected(true);
        board.setBlackPlayerAsComputer(false);
        blackHuman.setFont(Font.font(16));
        blackHuman.setTextFill(Color.CORNSILK);
        blackHuman.setOnAction(mouseEvent -> {
            board.setBlackPlayerAsComputer(false);
            if (whiteHuman.isSelected()) {
                randomMove.setDisable(true);
                miniMaxMedium.setDisable(true);
                miniMaxHard.setDisable(true);
            }
        });

        whiteComputer.setFont(Font.font(16));
        whiteComputer.setTextFill(Color.CORNSILK);
        whiteComputer.setOnAction(mouseEvent -> {
            board.setWhitePlayerAsComputer(true);
            randomMove.setDisable(false);
            miniMaxMedium.setDisable(false);
            miniMaxHard.setDisable(false);
        });

        blackComputer.setFont(Font.font(16));
        blackComputer.setTextFill(Color.CORNSILK);
        blackComputer.setOnAction(mouseEvent -> {
            board.setBlackPlayerAsComputer(true);
            randomMove.setDisable(false);
            miniMaxMedium.setDisable(false);
            miniMaxHard.setDisable(false);
        });

        if (whiteHuman.isSelected() && blackHuman.isSelected()) {
            randomMove.setDisable(true);
            miniMaxMedium.setDisable(true);
            miniMaxHard.setDisable(true);
        }

        whiteHuman.setToggleGroup(whiteGroup);
        whiteComputer.setToggleGroup(whiteGroup);
        blackHuman.setToggleGroup(blackGroup);
        blackComputer.setToggleGroup(blackGroup);

        VBox radioButtonsPanel = new VBox();
        radioButtonsPanel.setAlignment(Pos.TOP_LEFT);

        VBox.setMargin(whiteHuman, new Insets(0, 0, 10, 0));
        VBox.setMargin(blackHuman, new Insets(0, 0, 10, 0));
        VBox.setMargin(whiteText, new Insets(0, 0, 10, 0));
        VBox.setMargin(blackText, new Insets(30, 0, 10, 0));
        VBox.setMargin(vBoxTitle, new Insets(0, 0, 30, 0));

        radioButtonsPanel.getChildren().addAll(vBoxTitle, whiteText, whiteHuman, whiteComputer, blackText, blackHuman, blackComputer);

        Button applyButton = new Button("Apply");
        applyButton.setOnAction(mouseEvent -> popUpPreferences.close());

        Button resetButton = new Button("Set Default");
        resetButton.setOnAction(mouseEvent -> {
            randomMove.setSelected(true);
            randomMove.setDisable(true);
            miniMaxMedium.setSelected(false);
            miniMaxMedium.setDisable(true);
            miniMaxHard.setSelected(false);
            miniMaxHard.setDisable(true);
            whiteHuman.setSelected(true);
            blackHuman.setSelected(true);
            whiteComputer.setSelected(false);
            blackComputer.setSelected(false);
        });

        HBox normalButtons = new HBox();
        normalButtons.setAlignment(Pos.CENTER);
        HBox.setMargin(applyButton, new Insets(0, 10, 0, 0));
        HBox.setMargin(resetButton, new Insets(0, 0, 0, 10));

        normalButtons.getChildren().addAll(applyButton, resetButton);

        BorderPane layout = new BorderPane();
        BorderPane.setAlignment(normalButtons, Pos.BOTTOM_CENTER);
        BorderPane.setMargin(normalButtons, new Insets(30, 0, 50, 0));
        BorderPane.setAlignment(titleField, Pos.TOP_CENTER);
        BorderPane.setMargin(titleField, new Insets(50, 0, 0, 0));
        BorderPane.setAlignment(radioButtonsPanel, Pos.CENTER_LEFT);
        BorderPane.setMargin(radioButtonsPanel, new Insets(50, 0, 0, 80));
        BorderPane.setAlignment(radioButtonsAIPanel, Pos.CENTER_RIGHT);
        BorderPane.setMargin(radioButtonsAIPanel, new Insets(50, 80, 0, 0));
        Background background = initializeBackground(600, 600, BACKGROUND_MENU_BAR);
        layout.setBackground(background);
        layout.setPrefHeight(600);
        layout.setPrefWidth(600);
        layout.setTop(titleField);
        layout.setLeft(radioButtonsPanel);
        layout.setRight(radioButtonsAIPanel);
        layout.setBottom(normalButtons);

        Scene scene = new Scene(layout);
        popUpPreferences.setScene(scene);
        popUpPreferences.showAndWait();
    }

    public static MenuBar createMenuBar(CheckersBoard board) {
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        Menu edit = new Menu("Edit");
        Menu options = new Menu("Options");
        Menu help = new Menu("Help");
        MenuItem preferences = new MenuItem("Preferences");
        MenuItem giveUp = new MenuItem("Give Up");
        MenuItem saveGame = new MenuItem("Save Game");
        MenuItem newGame = new MenuItem("New Game");
        MenuItem loadGame = new MenuItem("Load Game");
        MenuItem exit = new MenuItem("Exit");
        MenuItem nextMove = new MenuItem("Next Move");
        MenuItem undoMove = new MenuItem("Undo Move");
        MenuItem gameManual = new MenuItem("Game Manual");
        MenuItem about = new MenuItem("About");
        SeparatorMenuItem separator = new SeparatorMenuItem();

        giveUp.setDisable(true);
        saveGame.setDisable(true);
        nextMove.setDisable(true);
        undoMove.setDisable(true);

        newGame.setOnAction(mouseEvent -> {
            board.createGame();
            createPreferencesWindow(board);
            giveUp.setDisable(false);
            saveGame.setDisable(false);
            board.computerStartGame();
            board.computerVsComputer();
        });
        giveUp.setOnAction(mouseEvent -> board.giveUp());
        exit.setOnAction(mouseEvent -> {
                    Platform.exit();
                    System.exit(0);
                }
        );
        preferences.setOnAction(mouseEvent -> createPreferencesWindow(board));
        gameManual.setOnAction(mouseEvent -> createPopUpWindow("GAME MANUAL", UIXContent.printManual()));
        about.setOnAction(mouseEvent -> createPopUpWindow("ABOUT", UIXContent.printAbout()));

        file.getItems().addAll(newGame, giveUp, saveGame, loadGame, exit);
        edit.getItems().addAll(nextMove, undoMove);
        options.getItems().addAll(preferences);
        help.getItems().addAll(gameManual, separator, about);
        menuBar.getMenus().addAll(file, edit, options, help);

        return menuBar;
    }
}
