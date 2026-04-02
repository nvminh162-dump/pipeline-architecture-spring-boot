# Debug Luồng Pipeline

Mục tiêu của file này là giúp hình dung chính xác chương trình chạy từ đâu, đi qua đâu, và dữ liệu thay đổi như thế nào.

## 1. Điểm khởi động

Ứng dụng bắt đầu từ [PipelineArchitectureSpringBootApplication.java](src/main/java/com/nvminh/pipeline/PipelineArchitectureSpringBootApplication.java).

Flow:

```text
Spring Boot start
→ tạo Application Context
→ gọi run(String... args)
→ dựng Pipeline
→ thêm các filter
→ tạo Message ban đầu
→ execute pipeline
→ in kết quả cuối
```

## 2. Luồng chạy từng bước

### Bước 1: Khởi động app

Khi chạy file `PipelineArchitectureSpringBootApplication`, Spring Boot sẽ khởi động bình thường.

Vì class này implement `CommandLineRunner`, nên sau khi Spring Boot lên xong, method `run(...)` sẽ được gọi tự động.

### Bước 2: Tạo pipeline

Trong `run(...)`:

```java
Pipeline pipeline = new Pipeline();
```

Lúc này pipeline mới chỉ là một container rỗng, chưa có filter nào.

### Bước 3: Đăng ký các filter theo thứ tự

```java
pipeline.addFilter(new InvoiceReader())
		.addFilter(new PaymentReader())
		.addFilter(new NoteReader())
		.addFilter(new CreditNoteReader());
```

Thứ tự này rất quan trọng vì dữ liệu sẽ đi tuần tự từ trên xuống dưới.

### Bước 4: Tạo message ban đầu

```java
Message message = new Message("{\"source\": \"ERP_SYSTEM\", \"batchId\": \"BATCH-2024-001\"}");
```

`Message` là payload đi qua toàn bộ pipeline.

Nó có 2 phần:

- `rawData`: dữ liệu gốc đầu vào
- `invoiceInfo`: nơi các filter ghi dần kết quả vào

### Bước 5: Chạy pipeline

```java
Message result = pipeline.execute(message);
```

Đây là lúc pipeline thực sự hoạt động.

## 3. Debug theo từng filter

Trong [PipelineBase.java](src/main/java/com/nvminh/pipeline/core/pipes/PipelineBase.java), method `execute(...)` chạy vòng lặp:

```java
for (IFilter<T> filter : filters) {
	currentMessage = filter.filter(currentMessage);
}
```

Nghĩa là:

```text
message
→ InvoiceReader
→ PaymentReader
→ NoteReader
→ CreditNoteReader
→ result
```

### Filter 1: InvoiceReader

File: [InvoiceReader.java](src/main/java/com/nvminh/pipeline/process/read/invoice/InvoiceReader.java)

Luồng:

```text
nhận Message
→ đọc message.getRawData()
→ tạo danh sách Invoice mẫu
→ gán vào message.getInvoiceInfo().setInvoices(...)
→ trả lại chính Message đó
```

Sau bước này:

- `invoices` đã có dữ liệu
- các field khác vẫn còn rỗng

### Filter 2: PaymentReader

File: [PaymentReader.java](src/main/java/com/nvminh/pipeline/process/read/payment/PaymentReader.java)

Luồng:

```text
nhận lại cùng Message
→ đọc rawData
→ tạo danh sách Payment mẫu
→ gán vào message.getInvoiceInfo().setPayments(...)
→ trả lại Message
```

Sau bước này:

- `invoices` vẫn còn
- `payments` đã được thêm vào

### Filter 3: NoteReader

File: [NoteReader.java](src/main/java/com/nvminh/pipeline/process/read/note/NoteReader.java)

Luồng:

```text
nhận Message
→ thêm notes
→ trả lại Message
```

### Filter 4: CreditNoteReader

File: [CreditNoteReader.java](src/main/java/com/nvminh/pipeline/process/read/credit/CreditNoteReader.java)

Luồng:

```text
nhận Message
→ thêm creditNotes
→ trả lại Message
```

## 4. Kết quả cuối

Sau khi qua hết filter, `message` đã chứa đầy đủ dữ liệu trong `InvoiceInfo`.

Khi in:

```java
System.out.println("[CONSUMER] Final result: " + result.getInvoiceInfo());
```

ta thấy toàn bộ dữ liệu tổng hợp từ 4 bước xử lý.

## 5. Tư duy kiến trúc

Pipeline này chạy theo kiểu:

```text
Producer → Pipeline → Filter 1 → Filter 2 → Filter 3 → Filter 4 → Consumer
```

Ý nghĩa:

- `Producer`: tạo dữ liệu đầu vào
- `Pipeline`: điều phối thứ tự xử lý
- `Filter`: xử lý từng phần dữ liệu
- `Consumer`: nhận kết quả cuối và hiển thị

## 6. Điều cần nhớ khi debug

1. `PipelineBase` không biết business logic.
2. Mỗi filter chỉ làm một việc.
3. Tất cả filter cùng sửa trên cùng một `Message`.
4. Thứ tự `addFilter(...)` quyết định thứ tự chạy.
5. Nếu đổi thứ tự filter, output cũng đổi theo.

## 7. Nếu muốn debug thực tế hơn

Bạn có thể đặt breakpoint ở 4 chỗ này:

1. `PipelineArchitectureSpringBootApplication.run(...)`
2. `PipelineBase.execute(...)`
3. `InvoiceReader.filter(...)`
4. `PaymentReader.filter(...)`
5. `NoteReader.filter(...)`
6. `CreditNoteReader.filter(...)`

Khi step through, bạn sẽ thấy cùng một `Message` được truyền đi và `InvoiceInfo` được điền dần.
