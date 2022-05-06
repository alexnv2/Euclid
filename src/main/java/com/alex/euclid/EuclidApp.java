package com.alex.euclid;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Screen;

import java.io.IOException;
import java.util.Objects;

public class EuclidApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Screen screen = Screen.getPrimary();
        FXMLLoader fxmlLoader = new FXMLLoader(EuclidApp.class.getResource("Euclid-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), screen.getBounds().getWidth()-15, screen.getBounds().getHeight()-80);
        stage.setTitle("Геометрия");
        stage.setX(0);
        stage.setY(0);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/com/alex/euclid/Images/Euclid.png")).toString()));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}