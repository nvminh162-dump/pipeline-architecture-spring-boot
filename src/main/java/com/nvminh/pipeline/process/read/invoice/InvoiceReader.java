package com.nvminh.pipeline.process.read.invoice;

import com.nvminh.pipeline.core.entities.IFilter;
import com.nvminh.pipeline.core.entities.Invoice;
import com.nvminh.pipeline.core.entities.InvoiceInfo;
import com.nvminh.pipeline.core.entities.Message;

import java.math.BigDecimal;
import java.util.List;

public class InvoiceReader implements IFilter<Message> {

	private static final String SEPARATOR = "------------------------------------------------------------";

    @Override
    public Message filter(Message message) {
		System.out.println(SEPARATOR);
		System.out.println("[STEP 1/4] InvoiceReader");
		System.out.println("[InvoiceReader] Processing invoices from: " + message.getRawData());

        List<Invoice> invoices = List.of(
                new Invoice("INV-001", "Web Development Service", new BigDecimal("5000.00")),
                new Invoice("INV-002", "Consulting Fee", new BigDecimal("2500.00"))
        );

        InvoiceInfo invoiceInfo = message.getInvoiceInfo();
        invoiceInfo.setInvoices(invoices);

        System.out.println("[InvoiceReader] Found " + invoices.size() + " invoices");
        return message;
    }
}
