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
        FXMLLoader fxmlLoader = new FXMLLoader(EuclidApp.class.getResource("Euclid-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setTitle("Геометрия");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/com/alex/euclid/Images/Euclid.png")).toString()));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}