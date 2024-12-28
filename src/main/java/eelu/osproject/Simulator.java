package eelu.osproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Simulator extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("simulator.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 620);
        stage.setMinWidth(1200);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("CPU Scheduling Simulator - Loyalixa");
        stage.getIcons().add(new Image(getClass().getResource("logo.png").toExternalForm()));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}