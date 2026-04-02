package com.nvminh.pipeline.core.entities;

import java.math.BigDecimal;

public class Payment {

	private final String paymentId;
	private final String method;
	private final BigDecimal amount;

	public Payment(String paymentId, String method, BigDecimal amount) {
		this.paymentId = paymentId;
		this.method = method;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Payment{" +
				"paymentId='" + paymentId + '\'' +
				", method='" + method + '\'' +
				", amount=" + amount +
				'}';
	}
}
