package com.capgemini.javafx_starterkit;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Startup extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("StarterKit-JavaFX");

		Parent root = FXMLLoader.load(getClass().getResource("/com/capgemini/javafx_starterkit/view/book-search.fxml"), //
				ResourceBundle.getBundle("com/capgemini/javafx_starterkit/base/base"));

		Scene scene = new Scene(root);

		primaryStage.setScene(scene);

		primaryStage.show();
	}
}
