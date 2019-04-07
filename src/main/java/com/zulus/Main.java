package com.zulus;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 500, 400);
        scene.setFill(Color.YELLOW);
        Polygon bigPolygon = new Polygon(10, 10, 60, 200, 10, 390, 250, 200);
        Polygon smallPolygon = new Polygon(470, 160, 400, 200, 470, 240, 450, 200);

        Circle circle = new Circle(50);
        circle.setCenterX(155);
        circle.setCenterY(200);
        bigPolygon.setFill(Color.BLUE);
        smallPolygon.setFill(Color.BLUE);
        circle.setFill(Color.RED);
        root.getChildren().addAll(bigPolygon, circle, smallPolygon);
        for (int i = 0; i < 7; i++) {
            Line line = new Line(155, 187+ i * 5, 445, 187 + i * 5);
            root.getChildren().add(line);
        }
        stage.setTitle("Lab1");
        stage.setScene(scene);
        stage.show();
    }
}
