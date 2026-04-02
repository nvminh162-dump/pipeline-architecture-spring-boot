package com.nvminh.pipeline.core.entities;

import java.math.BigDecimal;

public class CreditNote {

	private final String creditNoteNumber;
	private final BigDecimal amount;

	public CreditNote(String creditNoteNumber, BigDecimal amount) {
		this.creditNoteNumber = creditNoteNumber;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "CreditNote{" +
				"creditNoteNumber='" + creditNoteNumber + '\'' +
				", amount=" + amount +
				'}';
	}
}
