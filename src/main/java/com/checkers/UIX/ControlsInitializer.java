package com.checkers.UIX;

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

import static com.checkers.UIX.UIContent.BACKGROUND_MENU_BAR;

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

        Button exitButton = new Button("Exit " + title);
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

    public static MenuBar createMenuBar(CheckersBoard board) {
        Menu file = new Menu("File");
        MenuItem giveUp = new MenuItem("Give Up");
        giveUp.setDisable(true);
        giveUp.setOnAction(mouseEvent -> board.giveUp());
        MenuItem saveGame = new MenuItem("Save Game");
        saveGame.setDisable(true);
        //saveGame.setOnAction(mouseEvent ->);
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(mouseEvent -> {
            board.createGame();
            giveUp.setDisable(false);
            saveGame.setDisable(false);
        });
        MenuItem loadGame = new MenuItem("Load Game");
        //loadGame.setOnAction(mouseEvent ->);
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(mouseEvent -> {
                    Platform.exit();
                    System.exit(0);
                }
        );
        file.getItems().addAll(newGame, giveUp, saveGame, loadGame, exit);

        Menu edit = new Menu("Edit");
        MenuItem nextMove = new MenuItem("Next Move");
        //nextMove.setOnAction(mouseEvent ->);
        MenuItem undoMove = new MenuItem("Undo Move");
        //undoMove.setOnAction(mouseEvent ->);
        edit.getItems().addAll(nextMove, undoMove);

        Menu options = new Menu("Options");

        Menu preferences = new Menu("Preferences");

        Menu help = new Menu("Help");
        SeparatorMenuItem separator = new SeparatorMenuItem();
        MenuItem gameManual = new MenuItem("Game Manual");
        gameManual.setOnAction(mouseEvent -> createPopUpWindow("GAME MANUAL", UIContent.printManual()));
        MenuItem about = new MenuItem("About");
        about.setOnAction(mouseEvent -> createPopUpWindow("ABOUT", UIContent.printAbout()));
        help.getItems().addAll(gameManual, separator, about);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(file, edit, preferences, options, help);
        return menuBar;
    }
}
