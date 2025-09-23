package com.furkanoffice.clock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    private double xOffset = 0;
    private double yOffset = 0;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        scene.setFill(null);
        stage.setTitle("Clock App");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setHeight(650);
        stage.setWidth(810);
        scene.lookup(".title-bar").setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        scene.lookup(".title-bar").setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();

    }

}