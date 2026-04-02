package com.nvminh.pipeline.core.entities;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Invoice {

	private final String invoiceNumber;
	private final String description;
	private final BigDecimal amount;
}
