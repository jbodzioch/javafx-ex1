package com.capgemini.javafx_starterkit.model;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.javafx_starterkit.to.BookTo;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class BookAdd {

	private final StringProperty title = new SimpleStringProperty();
	private final StringProperty authors = new SimpleStringProperty();
	/*
	 * REV: po co tu wynik?
	 */
	private final ListProperty<BookTo> result = new SimpleListProperty<>(
			FXCollections.observableList(new ArrayList<>()));

	/*
	 * REV: brak method wymaganych w JavaFX beanie
	 */
	public String getTitle() {
		return title.get();
	}

	public void setTitle(String title) {
		this.title.set(title);
	}

	public String getAuthors() {
		return authors.get();
	}

	public List<BookTo> getResult() {
		return result.get();
	}

	public StringProperty titleProperty() {
		return title;
	}

	public StringProperty authorsProperty() {
		return authors;
	}

	public ListProperty<BookTo> resultProperty() {
		return result;
	}

	public final void setResult(List<BookTo> value) {
		result.setAll(value);
	}
}
