package com.nvminh.pipeline.core.entities;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CreditNote {

	private final String creditNoteNumber;
	private final BigDecimal amount;
}
