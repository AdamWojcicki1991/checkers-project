package com.checkers.UIX.panels;

import com.checkers.engine.figures.Figure;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static com.checkers.UIX.UIXContent.*;

public class TakenFigurePanel {
    private final VBox takenFigurePanel;
    private final Label blackTitleField;
    private final Label whiteTitleField;
    private final Label whiteFiguresCountField;
    private final Label blackFiguresCountField;
    private final FlowPane whiteTakenFigurePanel;
    private final FlowPane blackTakenFigurePanel;
    private int whiteFigureCounter;
    private int blackFigureCounter;

    public TakenFigurePanel() {
        takenFigurePanel = new VBox();
        blackTitleField = new Label();
        whiteTitleField = new Label();
        whiteFiguresCountField = new Label();
        blackFiguresCountField = new Label();
        whiteTakenFigurePanel = new FlowPane(Orientation.HORIZONTAL, 10, 10);
        blackTakenFigurePanel = new FlowPane(Orientation.HORIZONTAL, 10, 10);
        VBox.setMargin(whiteFiguresCountField, new Insets(0.5, 0, 0, 0));
        VBox.setMargin(blackFiguresCountField, new Insets(0.5, 0, 100, 0));
        VBox.setMargin(blackTitleField, new Insets(0, 0, 0.5, 0));
        VBox.setMargin(whiteTitleField, new Insets(0, 0, 0.5, 0));
        takenFigurePanel.setMaxSize(300, 708);
        takenFigurePanel.setAlignment(Pos.TOP_CENTER);
        takenFigurePanel.getChildren().addAll(blackTitleField, blackTakenFigurePanel, blackFiguresCountField, whiteTitleField, whiteTakenFigurePanel, whiteFiguresCountField);
    }

    public void createTakenFigurePanel() {
        whiteTitleField.setFont(Font.font(16));
        whiteTitleField.setTextFill(Color.SIENNA);
        whiteTitleField.setPrefSize(300, 25);
        whiteTitleField.setAlignment(Pos.CENTER);
        whiteTitleField.setBackground(new Background(new BackgroundFill(Color.CORNSILK, CornerRadii.EMPTY, Insets.EMPTY)));
        whiteTitleField.setText("WHITE PLAYER TAKEN FIGURES");
        whiteTitleField.setOnMouseEntered(mouseEvent ->
                                                  whiteTitleField.setBackground(new Background(new BackgroundFill(Color.CORNSILK.darker(), CornerRadii.EMPTY, Insets.EMPTY))));
        whiteTitleField.setOnMouseExited(mouseEvent ->
                                                 whiteTitleField.setBackground(new Background(new BackgroundFill(Color.CORNSILK.brighter(), CornerRadii.EMPTY, Insets.EMPTY))));

        blackTitleField.setFont(Font.font(16));
        blackTitleField.setTextFill(Color.SIENNA);
        blackTitleField.setPrefSize(300, 25);
        blackTitleField.setAlignment(Pos.CENTER);
        blackTitleField.setBackground(new Background(new BackgroundFill(Color.CORNSILK, CornerRadii.EMPTY, Insets.EMPTY)));
        blackTitleField.setText("BLACK PLAYER TAKEN FIGURES");
        blackTitleField.setOnMouseEntered(mouseEvent ->
                                                  blackTitleField.setBackground(new Background(new BackgroundFill(Color.CORNSILK.darker(), CornerRadii.EMPTY, Insets.EMPTY))));
        blackTitleField.setOnMouseExited(mouseEvent ->
                                                 blackTitleField.setBackground(new Background(new BackgroundFill(Color.CORNSILK.brighter(), CornerRadii.EMPTY, Insets.EMPTY))));

        whiteFiguresCountField.setFont(Font.font(16));
        whiteFiguresCountField.setTextFill(Color.SIENNA);
        whiteFiguresCountField.setPrefSize(300, 25);
        whiteFiguresCountField.setAlignment(Pos.CENTER_LEFT);
        whiteFiguresCountField.setBackground(new Background(new BackgroundFill(Color.CORNSILK, CornerRadii.EMPTY, Insets.EMPTY)));
        whiteFiguresCountField.setText("COUNT: ");
        whiteFiguresCountField.setOnMouseEntered(mouseEvent ->
                                                         whiteFiguresCountField.setBackground(new Background(new BackgroundFill(Color.CORNSILK.darker(), CornerRadii.EMPTY, Insets.EMPTY))));
        whiteFiguresCountField.setOnMouseExited(mouseEvent ->
                                                        whiteFiguresCountField.setBackground(new Background(new BackgroundFill(Color.CORNSILK.brighter(), CornerRadii.EMPTY, Insets.EMPTY))));

        blackFiguresCountField.setFont(Font.font(16));
        blackFiguresCountField.setTextFill(Color.SIENNA);
        blackFiguresCountField.setPrefSize(300, 25);
        blackFiguresCountField.setAlignment(Pos.CENTER_LEFT);
        blackFiguresCountField.setBackground(new Background(new BackgroundFill(Color.CORNSILK, CornerRadii.EMPTY, Insets.EMPTY)));
        blackFiguresCountField.setText("COUNT: ");
        blackFiguresCountField.setOnMouseEntered(mouseEvent ->
                                                         blackFiguresCountField.setBackground(new Background(new BackgroundFill(Color.CORNSILK.darker(), CornerRadii.EMPTY, Insets.EMPTY))));
        blackFiguresCountField.setOnMouseExited(mouseEvent ->
                                                        blackFiguresCountField.setBackground(new Background(new BackgroundFill(Color.CORNSILK.brighter(), CornerRadii.EMPTY, Insets.EMPTY))));

        whiteTakenFigurePanel.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        whiteTakenFigurePanel.setMaxSize(300, 250);
        whiteTakenFigurePanel.setPrefSize(300, 250);
        whiteTakenFigurePanel.setAlignment(Pos.CENTER);

        blackTakenFigurePanel.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        blackTakenFigurePanel.setMaxSize(300, 250);
        blackTakenFigurePanel.setPrefSize(300, 250);
        blackTakenFigurePanel.setAlignment(Pos.CENTER);
    }

    public void addTakenFigureToPanel(Figure figure) {
        switch (figure.getFigureType()) {
            case WHITE_PAWN:
                blackTakenFigurePanel.getChildren().addAll(new ImageView(WP_RES));
                addBlackFigureToCounter();
                break;
            case WHITE_QUEEN:
                blackTakenFigurePanel.getChildren().addAll(new ImageView(WQ_RES));
                addBlackFigureToCounter();
                break;
            case BLACK_PAWN:
                whiteTakenFigurePanel.getChildren().addAll(new ImageView(BP_RES));
                addWhiteFigureToCounter();
                break;
            case BLACK_QUEEN:
                whiteTakenFigurePanel.getChildren().addAll(new ImageView(BQ_RES));
                addWhiteFigureToCounter();
                break;
        }
    }

    public void removeTakenFigureFromPanel(Figure figure) {
        switch (figure.getFigureType()) {
            case WHITE_PAWN:
                blackTakenFigurePanel.getChildren().remove(WP_RES_IW);
                subtractBlackFigureFromCounter();
                break;
            case WHITE_QUEEN:
                blackTakenFigurePanel.getChildren().remove(WQ_RES_IW);
                subtractBlackFigureFromCounter();
                break;
            case BLACK_PAWN:
                whiteTakenFigurePanel.getChildren().remove(BP_RES_IW);
                subtractWhiteFigureFromCounter();
                break;
            case BLACK_QUEEN:
                whiteTakenFigurePanel.getChildren().remove(BQ_RES_IW);
                subtractWhiteFigureFromCounter();
                break;
        }
    }

    public void resetFiguresFromPanels() {
        whiteTakenFigurePanel.getChildren().clear();
        blackTakenFigurePanel.getChildren().clear();
    }

    public void resetFiguresCounter() {
        whiteFigureCounter = 0;
        blackFigureCounter = 0;
    }

    public VBox getTakenFigurePanel() {
        return takenFigurePanel;
    }

    private void addWhiteFigureToCounter() {
        whiteFigureCounter++;
        whiteFiguresCountField.setText("COUNT: " + whiteFigureCounter);
    }

    private void addBlackFigureToCounter() {
        blackFigureCounter++;
        blackFiguresCountField.setText("COUNT: " + blackFigureCounter);
    }

    private void subtractWhiteFigureFromCounter() {
        whiteFigureCounter--;
        whiteFiguresCountField.setText("COUNT: " + whiteFigureCounter);
    }

    private void subtractBlackFigureFromCounter() {
        blackFigureCounter--;
        blackFiguresCountField.setText("COUNT: " + blackFiguresCountField);
    }
}
