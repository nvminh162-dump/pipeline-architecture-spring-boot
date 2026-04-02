package com.nvminh.pipeline.core.entities;

public class Message implements IMessage {

	private final String rawData;
	private final InvoiceInfo invoiceInfo;

	public Message(String rawData) {
		this.rawData = rawData;
		this.invoiceInfo = new InvoiceInfo();
	}

	@Override
	public String getRawData() {
		return rawData;
	}

	@Override
	public InvoiceInfo getInvoiceInfo() {
		return invoiceInfo;
	}

	@Override
	public String toString() {
		return "Message{rawData='" + rawData + "', invoiceInfo=" + invoiceInfo + '}';
	}
}
