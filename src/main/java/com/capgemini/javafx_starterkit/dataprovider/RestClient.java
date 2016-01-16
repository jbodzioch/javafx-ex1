package com.capgemini.javafx_starterkit.dataprovider;

import java.util.ArrayList;
import java.util.Collection;

import com.capgemini.javafx_starterkit.to.BookTo;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RestClient {

	public Collection<BookTo> search(String title) {
		try {
			/*
			 * REV: te obiekty powinny byc zdefiniowane jako pola klasy i tworzone tylko raz
			 */
			Client client = Client.create();
			/*
			 * REV: adres serwera powinien byc wczytany z pliku konfiguracyjnego
			 */
			WebResource webResource = client.resource("http://localhost:9721/workshop/books-by-title");
			ClientResponse response;

			/*
			 * REV: !title != null
			 */
			if (!title.equals(null)) {
				response = webResource.queryParam("titlePrefix", title).accept("application/json")
						.get(ClientResponse.class);
			} else {
				/*
				 * REV: j.w.
				 */
				webResource = client.resource("http://localhost:9721/workshop/books-by-title?titlePrefix=");
				response = webResource.accept("application/json").get(ClientResponse.class);
			}

			if (response.getStatus() != 200) {
				/*
				 * REV: ten wyjatek nie wyleci z tej metody
				 */
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.getEntity(String.class);

			Gson gson = new Gson();

			BookTo[] tempBooks = gson.fromJson(output, BookTo[].class);

			Collection<BookTo> result = new ArrayList<BookTo>();

			for (BookTo bookTo : tempBooks) {
				result.add(bookTo);
			}

			/*
			 * REV: uzywaj loggera
			 */
			System.out.println("Output from Server .... \n");
			System.out.println(output);

			return result;

		} catch (Exception e) {
			/*
			 * REV: wyjatek powinien byc wyrzucony wyzej i obsluzony w kontrolerze
			 */
			e.printStackTrace();

		}
		/*
		 * REV: NPE przy bledzie
		 */
		return null;
	}

	public void add(String title, String authors) {
		try {

			BookTo to = new BookTo(title, authors);
			Gson gson = new Gson();
			/*
			 * REV: j.w.
			 */
			Client client = Client.create();
			/*
			 * REV: j.w.
			 */
			WebResource webResource = client.resource("http://localhost:9721/workshop/book");
			String input = gson.toJson(to);
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);

			if (response.getStatus() != 201 && response.getStatus() != 200) {
				/*
				 * REV: j.w.
				 */
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

		} catch (Exception e) {
			/*
			 * REV: j.w.
			 */
			e.printStackTrace();

		}

	}

}
