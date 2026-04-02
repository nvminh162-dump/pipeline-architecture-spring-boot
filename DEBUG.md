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

## 5. Giải thích output thực tế

Output bạn thấy trong terminal là kết quả của 3 lớp thông tin khác nhau:

1. Phần mở đầu của app
2. Phần log từng filter
3. Phần kết quả tổng hợp cuối cùng

### 5.1. Phần mở đầu

```text
============================================================
	PIPELINE ARCHITECTURE DEMO
============================================================
[DATA SOURCE] Created message: {"source": "ERP_SYSTEM", "batchId": "BATCH-2024-001"}
[PIPELINE] Starting execution...
============================================================
```

Ý nghĩa:

- Dòng đầu và dòng cuối là separator để tách khối log lớn.
- `PIPELINE ARCHITECTURE DEMO` là tiêu đề chung của app.
- `[DATA SOURCE] Created message: ...` cho biết `Message` đầu vào đã được tạo.
- `[PIPELINE] Starting execution...` báo rằng pipeline sắp bắt đầu chạy.

### 5.2. Separator trước từng bước

```text
------------------------------------------------------------
```

Dòng này xuất hiện trước mỗi filter để bạn nhìn ra ranh giới giữa các process.

Nó không mang dữ liệu nghiệp vụ, chỉ là dấu phân cách để log dễ đọc hơn.

### 5.3. Dòng `[STEP x/4]`

Ví dụ:

```text
[STEP 1/4] InvoiceReader
```

Ý nghĩa:

- `STEP 1/4` nghĩa là đây là bước đầu tiên trong tổng 4 bước.
- `InvoiceReader` là tên filter đang chạy.

Mục đích của dòng này là giúp bạn biết chính xác pipeline đang đứng ở đâu.

### 5.4. Dòng `Processing ... from ...`

Ví dụ:

```text
[InvoiceReader] Processing invoices from: {"source": "ERP_SYSTEM", "batchId": "BATCH-2024-001"}
```

Ý nghĩa:

- Filter đang nhận cùng một `Message` đầu vào.
- Nó đọc `rawData` từ `Message`.
- Dữ liệu gốc này được dùng làm nguồn để filter xử lý.

Điểm quan trọng:

- Tất cả filter đều nhận chung một `Message`.
- Nhưng mỗi filter chỉ bổ sung một phần riêng vào `invoiceInfo`.

### 5.5. Dòng `Found ...`

Ví dụ:

```text
[InvoiceReader] Found 2 invoices
```

Ý nghĩa:

- Filter vừa tạo xong dữ liệu mẫu.
- Con số phía sau cho biết số lượng object đã được thêm vào `InvoiceInfo`.

Tương tự:

- `InvoiceReader` thêm `invoices`
- `PaymentReader` thêm `payments`
- `NoteReader` thêm `notes`
- `CreditNoteReader` thêm `creditNotes`

### 5.6. Dòng kết thúc khối step

Ở bước cuối có thêm separator sau log:

```text
------------------------------------------------------------
```

Dòng này đánh dấu filter cuối đã chạy xong.

### 5.7. Phần consumer

```text
[CONSUMER] Pipeline completed!
[CONSUMER] Final result: InvoiceInfo(...)
============================================================
```

Ý nghĩa:

- `Pipeline completed!` nghĩa là toàn bộ filter đã chạy xong.
- `Final result:` là dữ liệu cuối cùng sau khi pipeline xử lý.
- `InvoiceInfo(...)` chính là kết quả tổng hợp từ tất cả các step.

## 6. Đọc output theo thứ tự nào

Bạn có thể đọc console theo công thức sau:

```text
Header
→ data source
→ start pipeline
→ step 1
→ step 2
→ step 3
→ step 4
→ consumer output
```

Nếu map vào dữ liệu thật:

```text
Message đầu vào
→ InvoiceReader thêm invoices
→ PaymentReader thêm payments
→ NoteReader thêm notes
→ CreditNoteReader thêm creditNotes
→ InvoiceInfo hoàn chỉnh
```

## 7. Tư duy kiến trúc

Pipeline này chạy theo kiểu:

```text
Producer → Pipeline → Filter 1 → Filter 2 → Filter 3 → Filter 4 → Consumer
```

Ý nghĩa:

- `Producer`: tạo dữ liệu đầu vào
- `Pipeline`: điều phối thứ tự xử lý
- `Filter`: xử lý từng phần dữ liệu
- `Consumer`: nhận kết quả cuối và hiển thị

## 8. Điều cần nhớ khi debug

1. `PipelineBase` không biết business logic.
2. Mỗi filter chỉ làm một việc.
3. Tất cả filter cùng sửa trên cùng một `Message`.
4. Thứ tự `addFilter(...)` quyết định thứ tự chạy.
5. Nếu đổi thứ tự filter, output cũng đổi theo.

## 9. Nếu muốn debug thực tế hơn

Bạn có thể đặt breakpoint ở 4 chỗ này:

1. `PipelineArchitectureSpringBootApplication.run(...)`
2. `PipelineBase.execute(...)`
3. `InvoiceReader.filter(...)`
4. `PaymentReader.filter(...)`
5. `NoteReader.filter(...)`
6. `CreditNoteReader.filter(...)`

Khi step through, bạn sẽ thấy cùng một `Message` được truyền đi và `InvoiceInfo` được điền dần.
