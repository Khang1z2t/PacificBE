# Pacific BE

Pacific BE là dự án backend được phát triển bởi nhóm **Musketeers v2 (aka Musketeers TNI)**. Dự án sử dụng **Spring Boot 3.4.2** và chạy trên **JDK 21**, cung cấp nền tảng mạnh mẽ cho việc xây dựng các API và xử lý logic phía server.

## Mục tiêu dự án
Dự án nhằm xây dựng hệ thống backend cho ứng dụng quản lý tài nguyên doanh nghiệp, cung cấp các API để xử lý dữ liệu người dùng, sản phẩm và giao dịch.

## Công nghệ sử dụng
- **Framework**: Spring Boot 3.4.2
- **Ngôn ngữ lập trình**: Java
- **Phiên bản JDK**: 21
- **Cơ sở dữ liệu**: SQL Server
- **Công cụ build**: Maven
- **Cache**: Redis
- **Thư viện phụ trợ**: Spring Data JPA, Spring Security, Lombok

## Yêu cầu cài đặt
Trước khi chạy dự án, đảm bảo bạn đã cài đặt:
1. **JDK 21**: [Link tải JDK](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
2. **Maven**: [Link tải Maven](https://maven.apache.org/download.cgi)
3. **IDE**: Đề xuất sử dụng IntelliJ IDEA.
4. **SQL Server**: Đảm bảo bạn đã cài đặt SQL Server và có thông tin kết nối (hostname, port, username, password).
5. **Postman**: Để kiểm tra các API, bạn có thể sử dụng Postman hoặc bất kỳ công cụ nào khác hỗ trợ gửi HTTP request.
   6   **Redis**: Dự án có sử dụng Redis, hãy đảm bảo bạn đã cài đặt và cấu hình Redis server.

## Hướng dẫn cài đặt và chạy dự án
1. Clone repository về máy:
   ```bash
    git clone [URL của repository]
   ```
2. Tạo file ``.env`` trong thư mục gốc của dự án và cấu hình các biến môi trường cần thiết (ví dụ: thông tin kết nối cơ sở dữ liệu, secret key, v.v.).
3. Cài đặt các phụ thuộc bằng Maven:
   ```bash
   mvn clean install
   ```
4. Chạy dự án:
   ```bash
   mvn spring-boot:run
   ```
5. Truy cập API thông qua Postman hoặc trình duyệt web:
   - Địa chỉ API: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
   - Hoặc truy cập vào file `PacificAPI.postmain_collection.json` trong thư mục `resources`

## Cấu trúc thư mục
```text
pacific-be/
├── src/
│   ├── main/
│   │   ├── java/com/pacific/pacificbe/
│   │   │   ├── annotations/        # Chứa các annotation tùy chỉnh
│   │   │   ├── aspect/             # Chứa các aspect cho AOP
│   │   │   ├── config/             # Chứa các cấu hình cho Spring Boot
│   │   │   ├── controller/         # Chứa các API controller
│   │   │   ├── dto/                # Chứa các lớp DTO (Data Transfer Object)
│   │   │   ├── exception/          # Chứa các lớp xử lý exception
│   │   │   ├── integration/        # Chứa các lớp tích hợp với các dịch vụ bên ngoài
│   │   │   ├── mapper/             # Chứa các lớp mapper để chuyển đổi giữa các đối tượng
│   │   │   ├── model/              # Chứa các lớp model (entity)
│   │   │   ├── repository/         # Chứa các repository truy vấn database
│   │   │   ├── scheduler/          # Chứa các lớp lên lịch công việc
│   │   │   ├── services/           # Chứa các logic nghiệp vụ
│   │   │   └── utils/              # Chứa các lớp tiện ích
│   │   └── resources/
│   │       ├── application.properties  # Cấu hình Spring Boot
│   │       ├── banner.txt           
│   │       └── PacificAPI.postman_collection.json      # File import vô Postman
│   └── test/                   # Chứa các test case
├── pom.xml                     # File cấu hình Maven
├── .env                        # File chứa biến môi trường
├── .env.example                # File chứa biến môi trường mẫu
├── docker-compose.yml          # File docker-compose 
├── Dockerfile                  # File Dockerfile
└── README.md
```

## Known Issues
- Đảm bảo Redis server đang chạy trước khi khởi động ứng dụng, nếu không sẽ gặp lỗi kết nối. 
- Trên Windows, cần cài đặt SQL Server với chế độ xác thực hỗn hợp (Mixed Mode Authentication).


## Liên hệ 
Nếu bạn có bất kỳ câu hỏi nào về dự án, vui lòng liên hệ với nhóm phát triển qua các kênh sau:
- Nhóm phát triển: Musketeers v2 (aka Musketeers TNI)
- Email: [pacific.musketeers.tni@gmail.com](mailto:pacific.musketeers.tni@gmail.com)
- Discord: [yuno.k](https://discord.com/users/628955171107635259) (Yuno)

---

### Lưu ý cuối
- Bạn nên kiểm tra lại các thông tin như **URL repository**, **endpoint API**, và **biến môi trường** để đảm bảo chúng chính xác với dự án của bạn.
- Nếu dự án có tài liệu API riêng (như Swagger/OpenAPI), bạn có thể thêm link vào README.
- Nếu bạn muốn dịch sang tiếng Anh hoặc thêm phần nào khác, cứ nói, tôi sẽ hỗ trợ!