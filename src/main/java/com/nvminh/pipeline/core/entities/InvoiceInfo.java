package com.nvminh.pipeline.core.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InvoiceInfo {

	private List<Invoice> invoices = new ArrayList<>();
	private List<Payment> payments = new ArrayList<>();
	private List<Note> notes = new ArrayList<>();
	private List<CreditNote> creditNotes = new ArrayList<>();
}
