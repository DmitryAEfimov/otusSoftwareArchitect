package ru.otus.softwarearchitect.defimov.lesson15.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "products")
public class Product {
	@Id
	private UUID id;
	@Indexed
	private String name;
	private String description;


	private Product() {
		// for DB
	}

	public Product(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return String.format("Product{id=%s, firstName='%s', lastName='%s'}", id, name, description);
	}
}
