package com.checkers.UIX;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TakenFigurePanel extends Canvas {

    public TakenFigurePanel() {
        super(300, 708);
    }

    public void drawTakenFigurePanel() {
        GraphicsContext graphics = getGraphicsContext2D();
        graphics.setFill(Color.TRANSPARENT);
        graphics.fillRect(0, 0, 300, 708);
        graphics.setStroke(Color.GREY);
        graphics.setLineWidth(8);
        graphics.strokeRect(0, 0, 300, 708);
    }
}
