package com.capgemini.javafx_starterkit.controller;

import java.util.ArrayList;
import java.util.Collection;

import com.capgemini.javafx_starterkit.dataprovider.RestClient;
import com.capgemini.javafx_starterkit.model.BookAdd;
import com.capgemini.javafx_starterkit.to.BookTo;

import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BookAddController {

	@FXML
	private TextField titleField;

	@FXML
	private TextField authorsField;

	@FXML
	private Button addButton;

	@FXML
	private Label authorsLabel;

	@FXML
	private Label titleLabel;

	private final RestClient restClient = new RestClient();

	private final BookAdd model = new BookAdd();

	public BookAddController() {
	}

	@FXML
	private void initialize() {

		titleField.textProperty().bindBidirectional(model.titleProperty());
		authorsField.textProperty().bindBidirectional(model.authorsProperty());

		BooleanBinding bind = titleField.textProperty().isEmpty().or(authorsField.textProperty().isEmpty());
		addButton.disableProperty().bind(bind);
	}

	@FXML
	private void addButtonAction(ActionEvent event) {

		Task<Collection<BookTo>> backgroundTask = new Task<Collection<BookTo>>() {

			@Override
			protected Collection<BookTo> call() throws Exception {

				restClient.add(model.getTitle(), model.getAuthors());
				Collection<BookTo> result = restClient.search(model.getTitle());
				return result;
			}

			@Override
			protected void succeeded() {

				Collection<BookTo> result = getValue();

				model.setResult(new ArrayList<BookTo>(result));

				Node source = (Node) event.getSource();
				Stage stage = (Stage) source.getScene().getWindow();
				stage.close();

				showAcceptInformation();
			}
		};

		new Thread(backgroundTask).start();
	}

	public void showAcceptInformation() {

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setContentText("Book was properly added to database");
		alert.showAndWait();
	}
}
