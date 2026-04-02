# Pipeline Architecture Demo - Spring Boot

## Kiến trúc tổng quan

```
Data Source → [Process Invoices] → [Process Payments] → [Process Notes] → [Process Credit Notes] → End
```

Mỗi bước là một **Filter** xử lý **Message** và truyền qua **Pipe**.

---

## Cấu trúc package

```
pipes/
├── client/
│   └── Main.java                  ← Entry point, chạy pipeline
├── core/
│   ├── entities/
│   │   ├── IFilter.java           ← Interface: mọi filter phải implement
│   │   ├── IMessage.java          ← Interface: contract của message
│   │   ├── Message.java           ← Payload chứa InvoiceInfo
│   │   ├── InvoiceInfo.java       ← DTO trung tâm (có invoices, payments, notes, creditNotes)
│   │   ├── Invoice.java
│   │   ├── Payment.java
│   │   ├── Note.java
│   │   └── CreditNote.java
│   └── pipes/
│       ├── PipelineBase.java      ← Abstract: giữ danh sách filters, gọi tuần tự
│       └── Pipeline.java         ← Concrete pipeline, kết nối filters
└── process/read/
    ├── invoice/
    │   └── InvoiceReader.java     ← Filter 1: đọc invoices từ message
    ├── payment/
    │   └── PaymentReader.java     ← Filter 2: đọc payments
    ├── note/
    │   └── NoteReader.java        ← Filter 3: đọc notes
    └── credit/
        └── CreditNoteReader.java  ← Filter 4: đọc credit notes
```

---

## Luồng hoạt động

```
Message (raw JSON/object)
    │
    ▼
InvoiceReader.filter(message)      → đọc invoices, ghi vào InvoiceInfo
    │
    ▼  (Message với invoices đã có)
PaymentReader.filter(message)      → đọc payments, ghi vào InvoiceInfo
    │
    ▼
NoteReader.filter(message)         → đọc notes
    │
    ▼
CreditNoteReader.filter(message)   → đọc creditNotes
    │
    ▼
InvoiceInfo hoàn chỉnh (result)
```

---

## Các khái niệm Pipeline Architecture trong code này

| Khái niệm      | Class tương ứng         | Vai trò                              |
|----------------|-------------------------|--------------------------------------|
| **Pipe**       | `PipelineBase`          | Kênh truyền dữ liệu giữa các filter  |
| **Filter**     | `IFilter<T>`            | Interface xử lý một bước             |
| **Producer**   | `Main` (data source)    | Tạo Message ban đầu                  |
| **Transformer**| `InvoiceReader`, etc.   | Đọc và transform data từ message     |
| **Consumer**   | `Main` (in kết quả)     | Nhận kết quả cuối, hiển thị/lưu      |
| **Message**    | `Message`               | Payload di chuyển qua pipeline       |

---

## Cách mở rộng

Thêm filter mới chỉ cần:
1. Tạo class implements `IFilter<Message>`
2. Đăng ký vào `Pipeline` trong `Main.java`
3. Không cần sửa bất kỳ filter nào khác → **Open/Closed Principle**