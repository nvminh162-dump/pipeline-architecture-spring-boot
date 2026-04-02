package com.nvminh.pipeline.process.read.payment;

import com.nvminh.pipeline.core.entities.IFilter;
import com.nvminh.pipeline.core.entities.Message;
import com.nvminh.pipeline.core.entities.Payment;
import java.math.BigDecimal;
import java.util.List;

public class PaymentReader implements IFilter<Message> {

    @Override
    public Message filter(Message message) {
        System.out.println("[PaymentReader] Processing payments from: " + message.getRawData());

        List<Payment> payments = List.of(
                new Payment("PAY-001", "BANK_TRANSFER", new BigDecimal("3000.00")),
                new Payment("PAY-002", "CASH", new BigDecimal("1500.00"))
        );

        message.getInvoiceInfo().setPayments(payments);

        System.out.println("[PaymentReader] Found " + payments.size() + " payments");
        return message;
    }
}
