package com.capgemini.javafx_starterkit.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import com.capgemini.javafx_starterkit.dataprovider.RestClient;
import com.capgemini.javafx_starterkit.model.BookSearch;
import com.capgemini.javafx_starterkit.to.BookTo;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class BookSearchController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField titleField;

	@FXML
	private Button searchButton;

	@FXML
	private Button addButton;

	@FXML
	private TableView<BookTo> resultTable;

	@FXML
	private TableColumn<BookTo, String> titleColumn;

	@FXML
	private TableColumn<BookTo, String> authorsColumn;

	private final RestClient restClient = new RestClient();

	private final BookSearch model = new BookSearch();

	public BookSearchController() {
	}

	@FXML
	private void initialize() {

		initializeResultTable();

		titleField.textProperty().bindBidirectional(model.titleProperty());
		resultTable.itemsProperty().bind(model.resultProperty());
	}

	private void initializeResultTable() {

		titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
		authorsColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAuthors()));

		resultTable.setPlaceholder(new Label(resources.getString("table.emptyText")));
	}

	@FXML
	private void searchButtonAction(ActionEvent event) {

		searchBook();
	}

	@FXML
	private void addButtonAction(ActionEvent event) {

		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/com/capgemini/javafx_starterkit/view/book-add.fxml"),
					resources);
			Scene scene = new Scene(root, 500, 250);
			Stage stageAddBook = new Stage();
			/*
			 * REV: tekst powinien byc pobrany z bundla
			 */
			stageAddBook.setTitle("Add book");
			stageAddBook.initModality(Modality.APPLICATION_MODAL);
			stageAddBook.initOwner(addButton.getScene().getWindow());
			stageAddBook.setScene(scene);

			stageAddBook.show();

			stageAddBook.setOnHiding(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					/*
					 * REV: wyszukiwanie nie jest konieczne przy zamykaniu okna bez dodania ksiazki
					 */
					searchBook();
				}
			});

		} catch (IOException e) {
			/*
			 * REV: obsluga wyjatkow
			 */
			e.printStackTrace();
		}
	}

	private void searchBook() {

		Task<Collection<BookTo>> backgroundTask = new Task<Collection<BookTo>>() {

			@Override
			protected Collection<BookTo> call() throws Exception {

				if (model.getTitle() == null) {
					model.setTitle("");
				}

				Collection<BookTo> result = restClient.search(model.getTitle());

				return result;
			}

			@Override
			protected void succeeded() {

				Collection<BookTo> result = getValue();

				model.setResult(new ArrayList<BookTo>(result));

				resultTable.getSortOrder().clear();
			}
		};

		new Thread(backgroundTask).start();
	}
}
