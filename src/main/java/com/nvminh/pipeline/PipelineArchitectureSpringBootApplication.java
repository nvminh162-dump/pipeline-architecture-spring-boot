package com.nvminh.pipeline;

import com.nvminh.pipeline.core.entities.Message;
import com.nvminh.pipeline.core.pipes.Pipeline;
import com.nvminh.pipeline.process.read.credit.CreditNoteReader;
import com.nvminh.pipeline.process.read.invoice.InvoiceReader;
import com.nvminh.pipeline.process.read.note.NoteReader;
import com.nvminh.pipeline.process.read.payment.PaymentReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PipelineArchitectureSpringBootApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PipelineArchitectureSpringBootApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Pipeline pipeline = new Pipeline();
		pipeline.addFilter(new InvoiceReader())
				.addFilter(new PaymentReader())
				.addFilter(new NoteReader())
				.addFilter(new CreditNoteReader());

		Message message = new Message("{\"source\": \"ERP_SYSTEM\", \"batchId\": \"BATCH-2024-001\"}");

		System.out.println("============================================================");
		System.out.println("   PIPELINE ARCHITECTURE DEMO");
		System.out.println("============================================================");
		System.out.println("[DATA SOURCE] Created message: " + message.getRawData());
		System.out.println("[PIPELINE] Starting execution...");

		Message result = pipeline.execute(message);

		System.out.println("[CONSUMER] Pipeline completed!");
		System.out.println("[CONSUMER] Final result: " + result.getInvoiceInfo());
		System.out.println("============================================================");
	}

}
