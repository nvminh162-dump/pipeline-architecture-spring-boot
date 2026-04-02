package com.nvminh.pipeline.process.read.invoice;

import com.nvminh.pipeline.core.entities.IFilter;
import com.nvminh.pipeline.core.entities.Message;

public class InvoiceReader implements IFilter<Message> {

    @Override
    public Message filter(Message message) {
        return message;
    }
}
