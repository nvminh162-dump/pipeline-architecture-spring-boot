package com.nvminh.pipeline.core.entities;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Payment {

	private final String paymentId;
	private final String method;
	private final BigDecimal amount;
}
