package com.nvminh.pipeline.core.entities;

import java.math.BigDecimal;

public class Invoice {

	private final String invoiceNumber;
	private final String description;
	private final BigDecimal amount;

	public Invoice(String invoiceNumber, String description, BigDecimal amount) {
		this.invoiceNumber = invoiceNumber;
		this.description = description;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Invoice{" +
				"invoiceNumber='" + invoiceNumber + '\'' +
				", description='" + description + '\'' +
				", amount=" + amount +
				'}';
	}
}
