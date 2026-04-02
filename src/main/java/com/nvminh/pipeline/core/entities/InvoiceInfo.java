package com.nvminh.pipeline.core.entities;

import java.util.ArrayList;
import java.util.List;

public class InvoiceInfo {

	private List<Invoice> invoices = new ArrayList<>();
	private List<Payment> payments = new ArrayList<>();
	private List<Note> notes = new ArrayList<>();
	private List<CreditNote> creditNotes = new ArrayList<>();

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public List<CreditNote> getCreditNotes() {
		return creditNotes;
	}

	public void setCreditNotes(List<CreditNote> creditNotes) {
		this.creditNotes = creditNotes;
	}

	@Override
	public String toString() {
		return "InvoiceInfo{" +
				"invoices=" + invoices +
				", payments=" + payments +
				", notes=" + notes +
				", creditNotes=" + creditNotes +
				'}';
	}
}
