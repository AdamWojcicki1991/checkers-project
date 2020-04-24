package com.checkers.UIX.panels;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameHistoryPanel extends Canvas {

    public GameHistoryPanel() {
        super(300, 708);
    }

    public void drawGameHistoryPanel() {
        GraphicsContext graphics = getGraphicsContext2D();
        graphics.setFill(Color.TRANSPARENT);
        graphics.fillRect(0, 0, 300, 708);
        graphics.setStroke(Color.GREY);
        graphics.setLineWidth(8);
        graphics.strokeRect(0, 0, 300, 708);
    }
}
