package com.nvminh.pipeline.process.read.credit;

import com.nvminh.pipeline.core.entities.IFilter;
import com.nvminh.pipeline.core.entities.Message;

public class CreditNoteReader implements IFilter<Message> {

    @Override
    public Message filter(Message message) {
        return message;
    }
}
