package ru.otus.softwarearchitect.defimov.lesson15.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "products")
public class Product {
	@Id
	private String id;
	@Indexed
	@JsonProperty(value = "code", required = true)
	private String vendorCode;
	@Indexed
	private String name;
	private String description;
	private double price;
	private int discount;
	private double actualPrice;

	@JsonCreator
	public Product(String vendorCode, String name, String description, double price, Integer discount) {
		this.vendorCode = vendorCode;
		this.name = name;
		this.description = description;
		this.price = price;
		this.discount = Objects.requireNonNullElse(discount, 0);
		this.actualPrice = (1 - this.discount / 100.0) * price;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * catalog price
	 *
	 * @return
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * percent value of discount
	 *
	 * @return
	 */
	public int getDiscount() {
		return discount;
	}

	/**
	 * price with discount
	 *
	 * @return
	 */
	public double getActualPrice() {
		return actualPrice;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return String
				.format("Product{vendorCode=%s, firstName='%s', lastName='%s', price=%.2f, discount=%d, actualCost=%.2f}",
						vendorCode, name, description, price, discount, actualPrice);
	}
}
