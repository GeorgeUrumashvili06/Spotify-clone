package com.example.spotifyapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img.png")));
        stage.getIcons().add(icon);

        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("Chinese Spotify");
        stage.setScene(scene);
        stage.show();
    }
    public static void main (String[] args){
        launch();
    }
}