package com.nvminh.pipeline.process.read.payment;

import com.nvminh.pipeline.core.entities.IFilter;
import com.nvminh.pipeline.core.entities.Message;

public class PaymentReader implements IFilter<Message> {

    @Override
    public Message filter(Message message) {
        return message;
    }
}
