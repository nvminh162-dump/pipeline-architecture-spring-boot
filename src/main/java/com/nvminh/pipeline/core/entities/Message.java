package com.nvminh.pipeline.core.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Message implements IMessage {

	private final String rawData;
	private final InvoiceInfo invoiceInfo;

	public Message(String rawData) {
		this.rawData = rawData;
		this.invoiceInfo = new InvoiceInfo();
	}
}
