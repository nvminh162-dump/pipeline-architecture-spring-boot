package com.nvminh.pipeline.process.read.credit;

import com.nvminh.pipeline.core.entities.IFilter;
import com.nvminh.pipeline.core.entities.CreditNote;
import com.nvminh.pipeline.core.entities.Message;

import java.math.BigDecimal;
import java.util.List;

public class CreditNoteReader implements IFilter<Message> {

	private static final String SEPARATOR = "------------------------------------------------------------";

    @Override
    public Message filter(Message message) {
		System.out.println(SEPARATOR);
		System.out.println("[STEP 4/4] CreditNoteReader");
		System.out.println("[CreditNoteReader] Processing credit notes from: " + message.getRawData());

        List<CreditNote> creditNotes = List.of(
                new CreditNote("CRN-001", new BigDecimal("200.00"))
        );

        message.getInvoiceInfo().setCreditNotes(creditNotes);

        System.out.println("[CreditNoteReader] Found " + creditNotes.size() + " credit notes");
        System.out.println(SEPARATOR);
        return message;
    }
}
