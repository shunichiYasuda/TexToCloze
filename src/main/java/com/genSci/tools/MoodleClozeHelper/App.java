package com.genSci.tools.MoodleClozeHelper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
    	AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("primary.fxml"));
        scene = new Scene(root);
        stage.setTitle("Moodle 穴埋め問題：数値コード支援");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }

}