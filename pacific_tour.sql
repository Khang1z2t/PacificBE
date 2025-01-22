-- Xóa database nếu đã tồn tại
DROP DATABASE IF EXISTS pacific_tour;
GO

-- Tạo mới database
CREATE DATABASE pacific_tour;
GO

USE pacific_tour;
GO

-- Table: Roles
CREATE TABLE Roles (
    RoleID TINYINT PRIMARY KEY,
    RoleName NVARCHAR(50) NOT NULL,
    Description NVARCHAR(255)
);

-- Table: Statuses
CREATE TABLE Statuses (
    StatusID INT IDENTITY(1,1) PRIMARY KEY,
    StatusName NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX)
);

-- Table: Region
CREATE TABLE Regions (
    RegionID INT IDENTITY(1,1) PRIMARY KEY,
    RegionName NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX)
);

-- Table: TourType
CREATE TABLE TourTypes (
    TourTypeID INT IDENTITY(1,1) PRIMARY KEY,
    TourTypeName NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX)
);

-- Table: Users
CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    FullName NVARCHAR(255) NOT NULL,
    Email NVARCHAR(255) UNIQUE NOT NULL,
    PhoneNumber NVARCHAR(20),
    Password NVARCHAR(255) NOT NULL,
    RoleID TINYINT FOREIGN KEY REFERENCES Roles(RoleID),
    StatusID INT FOREIGN KEY REFERENCES Statuses(StatusID)
);

-- Table: Tours
CREATE TABLE Tours (
    TourID INT IDENTITY(1,1) PRIMARY KEY,
    TourName NVARCHAR(255) NOT NULL,
    Destination NVARCHAR(255),
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    Price DECIMAL(18, 2) NOT NULL,
    Capacity INT NOT NULL,
    Description NVARCHAR(MAX),
    ImageURL NVARCHAR(255),
    StatusID INT FOREIGN KEY REFERENCES Statuses(StatusID),
    RegionID INT FOREIGN KEY REFERENCES Regions(RegionID),
    TourTypeID INT FOREIGN KEY REFERENCES TourTypes(TourTypeID)
);

-- Table: Bookings
CREATE TABLE Bookings (
    BookingID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT FOREIGN KEY REFERENCES Users(UserID),
    TourID INT FOREIGN KEY REFERENCES Tours(TourID),
    BookingDate DATE,
    TotalPrice DECIMAL(18, 2) NOT NULL,
    StatusID INT FOREIGN KEY REFERENCES Statuses(StatusID),
    TotalPersons INT NOT NULL
);

-- Table: BookingDetails
CREATE TABLE BookingDetails (
    BookingDetailID INT IDENTITY(1,1) PRIMARY KEY,
    BookingID INT FOREIGN KEY REFERENCES Bookings(BookingID),
    PassengerName NVARCHAR(255) NOT NULL,
    PassengerAge INT NOT NULL,
    PassengerGender NVARCHAR(10),
    SpecialRequest NVARCHAR(MAX),
    AdultCount INT DEFAULT 0 NOT NULL,
    ChildCount INT DEFAULT 0 NOT NULL
);

-- Table: Schedule
CREATE TABLE Schedule (
    ScheduleID INT IDENTITY(1,1) PRIMARY KEY,
    TourID INT FOREIGN KEY REFERENCES Tours(TourID),
    DayNumber INT NOT NULL,
    StartTime TIME NOT NULL,
    EndTime TIME NOT NULL,
    Activity NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX),
    StatusID INT FOREIGN KEY REFERENCES Statuses(StatusID)
);

-- Table: Rooms
CREATE TABLE Rooms (
    RoomID INT IDENTITY(1,1) PRIMARY KEY,
    TourID INT FOREIGN KEY REFERENCES Tours(TourID),
    RoomType NVARCHAR(50) NOT NULL,
    Capacity INT NOT NULL,
    Price DECIMAL(18, 2) NOT NULL,
    Description NVARCHAR(MAX),
    StatusID INT FOREIGN KEY REFERENCES Statuses(StatusID)
);

-- Table: Transportation
CREATE TABLE Transportation (
    TransportID INT IDENTITY(1,1) PRIMARY KEY,
    TourID INT FOREIGN KEY REFERENCES Tours(TourID),
    TransportType NVARCHAR(50) NOT NULL,
    Price DECIMAL(18, 2) NOT NULL,
    Description NVARCHAR(MAX),
    StatusID INT FOREIGN KEY REFERENCES Statuses(StatusID)
);

-- Table: Services
CREATE TABLE Services (
    ServiceID INT IDENTITY(1,1) PRIMARY KEY,
    ServiceName NVARCHAR(255) NOT NULL,
    Price DECIMAL(18, 2) NOT NULL,
    Description NVARCHAR(MAX),
    StatusID INT FOREIGN KEY REFERENCES Statuses(StatusID)
);

-- Table: BookingServices
CREATE TABLE BookingServices (
    BookingServiceID INT IDENTITY(1,1) PRIMARY KEY,
    BookingID INT FOREIGN KEY REFERENCES Bookings(BookingID),
    ServiceID INT FOREIGN KEY REFERENCES Services(ServiceID),
    Quantity INT NOT NULL
);

-- Table: Vouchers
CREATE TABLE Vouchers (
    VoucherID INT IDENTITY(1,1) PRIMARY KEY,
    Code NVARCHAR(50) UNIQUE NOT NULL,
    Discount DECIMAL(18, 2) NOT NULL,
    DiscountType NVARCHAR(50) NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    MinSpend DECIMAL(18, 2) DEFAULT 0,
    MaxDiscount DECIMAL(18, 2),
    StatusID INT FOREIGN KEY REFERENCES Statuses(StatusID),
    Description NVARCHAR(MAX)
);

-- Table: BookingVouchers
CREATE TABLE BookingVouchers (
    BookingVoucherID INT IDENTITY(1,1) PRIMARY KEY,
    BookingID INT FOREIGN KEY REFERENCES Bookings(BookingID),
    VoucherID INT FOREIGN KEY REFERENCES Vouchers(VoucherID),
    AppliedDate DATE,
    DiscountAmount DECIMAL(18, 2) NOT NULL
);

-- Table: Payment
CREATE TABLE Payment (
    PaymentID INT IDENTITY(1,1) PRIMARY KEY,
    BookingID INT FOREIGN KEY REFERENCES Bookings(BookingID),
    PaymentDate DATE,
    Amount DECIMAL(18, 2) NOT NULL,
    PaymentMethod NVARCHAR(50) NOT NULL,
    StatusID INT FOREIGN KEY REFERENCES Statuses(StatusID)
);

-- Table: Feedback
CREATE TABLE Feedback (
    FeedbackID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT FOREIGN KEY REFERENCES Users(UserID),
    TourID INT FOREIGN KEY REFERENCES Tours(TourID),
    Rating INT NOT NULL,
    Comment NVARCHAR(MAX),
    FeedbackDate DATE,
    StatusID INT FOREIGN KEY REFERENCES Statuses(StatusID)
);

-- Table: Notifications
CREATE TABLE Notifications (
    NotificationID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT FOREIGN KEY REFERENCES Users(UserID),
    Message NVARCHAR(MAX) NOT NULL,
    NotificationDate DATE,
    StatusID INT FOREIGN KEY REFERENCES Statuses(StatusID)
);

-- Table: TourGuide
CREATE TABLE TourGuide (
    GuideID INT IDENTITY(1,1) PRIMARY KEY,
    FullName NVARCHAR(255) NOT NULL,
    PhoneNumber NVARCHAR(20),
    Email NVARCHAR(255) UNIQUE NOT NULL,
    ExperienceYears INT NOT NULL,
    Description NVARCHAR(MAX),
    StatusID INT FOREIGN KEY REFERENCES Statuses(StatusID)
);

-- Table: TourGuideAssignment
CREATE TABLE TourGuideAssignment (
    AssignmentID INT IDENTITY(1,1) PRIMARY KEY,
    TourID INT FOREIGN KEY REFERENCES Tours(TourID),
    GuideID INT FOREIGN KEY REFERENCES TourGuide(GuideID),
    AssignedDate DATE NOT NULL,
    StatusID INT FOREIGN KEY REFERENCES Statuses(StatusID)
);


-- Thêm dữ liệu mặc định
-- Thêm dữ liệu cho bảng Roles
INSERT INTO Roles (RoleID, RoleName, Description) VALUES
(1, 'Customer', 'Khách hàng'),
(2, 'Admin', 'Quản trị viên'),
(3, 'Employee', 'Nhân viên');

-- Thêm dữ liệu cho bảng Statuses
INSERT INTO Statuses (StatusName, Description) VALUES
('Active', 'Hoạt động'),
('Inactive', 'Ngưng hoạt động'),
('Maintenance', 'Bảo trì'),
('Approved', 'Đã phê duyệt'),
('Rejected', 'Bị từ chối'),
('Payed', 'Đã thanh toán'),
('Pending', 'Chờ'),
('Cancelled', 'Hủy bỏ')
;

-- Dữ liệu mẫu cho bảng Regions
INSERT INTO Regions (RegionName) VALUES
('North'),
('South'),
('Other');

-- Dữ liệu mẫu cho bảng TourTypes
INSERT INTO TourTypes (TourTypeName) VALUES
('Domestic'),
('International');

-- Dữ liệu mẫu cho bảng Users
INSERT INTO Users (FullName, Email, PhoneNumber, Password, RoleID, StatusID)
VALUES
('Nguyen Van A', 'nguyenvana@example.com', '0901234567', 'password123', 3, 2),
('Tran Thi B', 'tranthib@example.com', '0912345678', 'password456', 3, 2),
('Admin User', 'admin@example.com', '0987654321', 'adminpass', 1, 2);

-- Dữ liệu mẫu cho bảng Tours (Cập nhật)
INSERT INTO Tours (TourName, Destination, StartDate, EndDate, Price, Capacity, Description, ImageURL, StatusID, RegionID, TourTypeID)
VALUES
('Tour Ha Long Bay', 'Quang Ninh', '2025-02-01', '2025-02-05', 5000000, 30, 'Tham quan Vịnh Hạ Long', '/images/halong.jpg', 2, 1, 1),
('Tour Da Nang', 'Da Nang', '2025-03-10', '2025-03-15', 7000000, 20, 'Tham quan Đà Nẵng và Hội An', '/images/danang.jpg', 2, 1, 1),
('Tour Sapa', 'Lào Cai', '2025-04-01', '2025-04-05', 4000000, 25, 'Khám phá Sapa mù sương', '/images/sapa.jpg', 2, 1, 1),
('Tour Nha Trang', 'Khánh Hòa', '2025-05-10', '2025-05-15', 6000000, 20, 'Tham quan biển Nha Trang', '/images/nhatrang.jpg', 2, 1, 1),
('Tour Hà Nội - Hạ Long', 'Hà Nội, Hạ Long', '2025-02-01', '2025-02-05', 5000000, 30, 'Khám phá miền Bắc', 'hanoi.jpg', 1, 1, 1),
('Tour Bangkok - Pattaya', 'Bangkok, Pattaya', '2025-03-01', '2025-03-05', 15000000, 20, 'Tour Thái Lan', 'thailand.jpg', 1, 2, 2),
('Tour Mỹ', 'Washington', '2025-03-01', '2025-03-05', 15000000, 20, 'Tour Mỹ', 'thailand.jpg', 1, 3, 2),
('Tour Trung Quốc', 'Bắc Kinh', '2025-03-01', '2025-03-05', 15000000, 20, 'Tour Trung Quốc', 'thailand.jpg', 1, 3, 2);


-- Dữ liệu mẫu cho bảng Bookings
INSERT INTO Bookings (UserID, TourID, BookingDate, TotalPrice, TotalPersons, StatusID)
VALUES
(1, 1, GETDATE(), 10000000, 2, 1),
(2, 2, GETDATE(), 7000000, 1, 1),
(1, 3, GETDATE(), 8000000, 2, 1),
(2, 4, GETDATE(), 12000000, 4, 1),
(2, 5, GETDATE(), 20000000, 4, 1),
(1, 6, GETDATE(), 45000000, 3, 1),
(2, 7, GETDATE(), 20000000, 4, 1),
(2, 8, GETDATE(), 15000000, 1, 1)
;

-- Dữ liệu mẫu cho bảng BookingDetails
INSERT INTO BookingDetails (BookingID, PassengerName, PassengerAge, PassengerGender, AdultCount, ChildCount, SpecialRequest)
VALUES
(1, 'Nguyen Van A', 30, 'Male', 1, 0, 'Yêu cầu ăn chay'),
(1, 'Tran Thi B', 28, 'Female', 1, 0, NULL),
(2, 'Nguyen Van C', 35, 'Male', 1, 0, 'Ghế gần cửa sổ'),
(3, 'Nguyen Van D', 40, 'Male', 1, 0, 'Yêu cầu ghế gần cửa sổ'),
(3, 'Nguyen Thi E', 38, 'Female', 1, 0, NULL),
(4, 'Tran Van F', 45, 'Male', 2, 2, 'Yêu cầu thêm chăn cho trẻ em'),
(5, 'Nguyen Thi G', 30, 'Female', 1, 0, 'Yêu cầu phòng gần bể bơi'),
(6, 'Tran Thi H', 33, 'Female', 1, 0, 'Yêu cầu dịch vụ xe đưa đón'),
(7, 'Nguyen Thi I', 25, 'Female', 1, 0, NULL),
(8, 'Tran Thi J', 29, 'Female', 1, 0, 'Yêu cầu chỗ ngồi gần cửa sổ')
;

-- Dữ liệu mẫu cho bảng Schedule
INSERT INTO Schedule (TourID, DayNumber, StartTime, EndTime, Activity, Description, StatusID)
VALUES
(1, 1, '08:00', '12:00', 'Tham quan đảo Ti Tốp', 'Thưởng ngoạn cảnh đẹp và chụp ảnh', 2),
(1, 2, '14:00', '18:00', 'Khám phá hang Sửng Sốt', 'Tham quan hang động lớn nhất vịnh', 2),
(3, 1, '09:00', '12:00', 'Tham quan núi Hàm Rồng', 'Ngắm cảnh và chụp ảnh tại núi Hàm Rồng', 2),
(3, 2, '13:00', '17:00', 'Khám phá bản Cát Cát', 'Tham quan và tìm hiểu văn hóa dân tộc H’Mông', 2),
(4, 1, '08:00', '12:00', 'Tham quan Vinpearl Land', 'Trải nghiệm các hoạt động tại công viên nước', 2)
;

-- Dữ liệu mẫu cho bảng Rooms
INSERT INTO Rooms (TourID, RoomType, Capacity, Price, Description, StatusID)
VALUES
(1, 'Phòng đôi', 2, 1500000, 'Phòng dành cho 2 người, view biển', 2),
(2, 'Phòng gia đình', 4, 3000000, 'Phòng dành cho gia đình, rộng rãi', 2),
(3, 'Phòng đơn', 1, 800000, 'Phòng nhỏ gọn, tiện nghi', 2),
(4, 'Phòng đôi', 2, 1600000, 'Phòng dành cho 2 người, view biển', 2)
;

-- Dữ liệu mẫu cho bảng Transportation
INSERT INTO Transportation (TourID, TransportType, Price, Description, StatusID)
VALUES
(1, 'Xe du lịch', 500000, 'Xe đưa đón từ Hà Nội đến Hạ Long', 2),
(2, 'Tàu cao tốc', 1000000, 'Tàu cao tốc di chuyển trên sông', 2),
(3, 'Phòng đơn',  800000, 'Phòng nhỏ gọn, tiện nghi', 2),
(4, 'Phòng đôi', 1600000, 'Phòng dành cho 2 người, view biển', 2)
;

-- Dữ liệu mẫu cho bảng Services
INSERT INTO Services (ServiceName, Price, Description, StatusID)
VALUES
('Hướng dẫn viên', 300000, 'Hướng dẫn viên du lịch chuyên nghiệp', 2),
('Dịch vụ ảnh', 200000, 'Chụp ảnh và quay phim lưu niệm', 2),
('Thuê trang phục dân tộc', 150000, 'Trang phục truyền thống cho chụp ảnh', 2),
('Dịch vụ spa', 500000, 'Thư giãn sau chuyến đi', 2)
;

-- Dữ liệu mẫu cho bảng Vouchers
INSERT INTO Vouchers (Code, Discount, DiscountType, StartDate, EndDate, MinSpend, MaxDiscount, StatusID, Description)
VALUES
('SUMMER2025', 10, 'percent', '2025-01-01', '2025-06-30', 5000000, 1000000, 2, 'Khuyến mãi mùa hè'),
('NEWYEAR2025', 500000, 'amount', '2025-01-01', '2025-01-31', 0, NULL, 2, 'Ưu đãi năm mới'),
('SPRING2025', 15, 'percent', '2025-03-01', '2025-05-31', 3000000, 500000, 2, 'Khuyến mãi mùa xuân'),
('TRAVEL2025', 1000000, 'amount', '2025-01-01', '2025-12-31', 5000000, NULL, 2, 'Ưu đãi đặc biệt cho khách hàng thân thiết')
;

-- Dữ liệu mẫu cho bảng BookingVouchers
INSERT INTO BookingVouchers (BookingID, VoucherID, AppliedDate, DiscountAmount)
VALUES
(1, 1, GETDATE(), 500000),
(2, 2, GETDATE(), 500000),
(3, 3, GETDATE(), 1200000),
(4, 4, GETDATE(), 1000000)
;

-- Dữ liệu mẫu cho bảng Payment
INSERT INTO Payment (BookingID, PaymentDate, Amount, PaymentMethod, StatusID)
VALUES
(1, GETDATE(), 9500000, 'Chuyển khoản', 2),
(2, GETDATE(), 6500000, 'Tiền mặt', 2),
(3, GETDATE(), 6800000, 'Chuyển khoản', 2),
(4, GETDATE(), 11000000, 'Thẻ tín dụng', 2);

-- Dữ liệu mẫu cho bảng Feedback
INSERT INTO Feedback (UserID, TourID, Rating, Comment, FeedbackDate, StatusID)
VALUES
(1, 1, 5, 'Chuyến đi tuyệt vời, mọi thứ đều hoàn hảo!', GETDATE(), 2),
(2, 2, 4, 'Dịch vụ tốt nhưng thời gian hơi ngắn.', GETDATE(), 2),
(1, 3, 5, 'Phong cảnh đẹp, dịch vụ tốt.', GETDATE(), 2),
(2, 4, 4, 'Dịch vụ tốt nhưng giá hơi cao.', GETDATE(), 2);

-- Dữ liệu mẫu cho bảng Notifications
INSERT INTO Notifications (UserID, Message, NotificationDate, StatusID)
VALUES
(1, 'Đặt tour thành công! Cảm ơn bạn đã sử dụng dịch vụ.', GETDATE(), 2),
(2, 'Tour Đà Nẵng của bạn đã được phê duyệt.', GETDATE(), 2),
(1, 'Tour Sapa của bạn đã được xác nhận.', GETDATE(), 2),
(2, 'Bạn có khuyến mãi 15% cho tour tiếp theo.', GETDATE(), 2);

-- Dữ liệu mẫu cho bảng TourGuide
INSERT INTO TourGuide (FullName, PhoneNumber, Email, ExperienceYears, Description, StatusID)
VALUES
('Nguyen Van Tien', '0912345678', 'tiennv@example.com', 5, 'Hướng dẫn viên chuyên nghiệp, am hiểu miền Bắc', 2),
('Tran Thi Lan', '0923456789', 'lantr@example.com', 3, 'Hướng dẫn viên năng động, chuyên tour miền Trung', 2),
('Le Hoang Minh', '0934567890', 'minhlh@example.com', 7, 'Hướng dẫn viên giàu kinh nghiệm, chuyên tour quốc tế', 2),
('Pham Thi Mai', '0945678901', 'maipt@example.com', 4, 'Hướng dẫn viên tận tâm, chuyên tour miền Nam', 2),
('Do Quang Huy', '0956789012', 'huydq@example.com', 6, 'Hướng dẫn viên chuyên nghiệp, thông thạo nhiều ngôn ngữ', 2);

-- Dữ liệu mẫu cho bảng TourGuideAssignment
INSERT INTO TourGuideAssignment (TourID, GuideID, AssignedDate, StatusID)
VALUES
(1, 1, '2025-01-20', 2), -- Nguyen Van Tien được phân công cho Tour Ha Long Bay
(2, 2, '2025-01-25', 2), -- Tran Thi Lan được phân công cho Tour Da Nang
(3, 1, '2025-02-01', 2), -- Nguyen Van Tien được phân công cho Tour Sapa
(4, 4, '2025-02-15', 2), -- Pham Thi Mai được phân công cho Tour Nha Trang
(6, 3, '2025-03-01', 2), -- Le Hoang Minh được phân công cho Tour Bangkok - Pattaya
(7, 5, '2025-03-10', 2), -- Do Quang Huy được phân công cho Tour Mỹ
(8, 5, '2025-03-20', 2); -- Do Quang Huy được phân công cho Tour Trung Quốc


-- Lấy tất cả dữ liệu từ bảng Roles
SELECT * FROM Roles;

-- Lấy tất cả dữ liệu từ bảng Statuses
SELECT * FROM Statuses;

-- Lấy tất cả dữ liệu từ bảng Regions
SELECT * FROM Regions;

-- Lấy tất cả dữ liệu từ bảng TourTypes
SELECT * FROM TourTypes;

-- Lấy tất cả dữ liệu từ bảng Users
SELECT * FROM Users;

-- Lấy tất cả dữ liệu từ bảng Tours
SELECT * FROM Tours;

-- Lấy tất cả dữ liệu từ bảng Bookings
SELECT * FROM Bookings;

-- Lấy tất cả dữ liệu từ bảng BookingDetails
SELECT * FROM BookingDetails;

-- Lấy tất cả dữ liệu từ bảng Schedule
SELECT * FROM Schedule;

-- Lấy tất cả dữ liệu từ bảng Rooms
SELECT * FROM Rooms;

-- Lấy tất cả dữ liệu từ bảng Transportation
SELECT * FROM Transportation;

-- Lấy tất cả dữ liệu từ bảng Services
SELECT * FROM Services;

-- Lấy tất cả dữ liệu từ bảng BookingServices
SELECT * FROM BookingServices;

-- Lấy tất cả dữ liệu từ bảng Vouchers
SELECT * FROM Vouchers;

-- Lấy tất cả dữ liệu từ bảng BookingVouchers
SELECT * FROM BookingVouchers;

-- Lấy tất cả dữ liệu từ bảng Payment
SELECT * FROM Payment;

-- Lấy tất cả dữ liệu từ bảng Feedback
SELECT * FROM Feedback;

-- Lấy tất cả dữ liệu từ bảng Notifications
SELECT * FROM Notifications;

-- Lấy tất cả dữ liệu từ bảng Notifications
SELECT * FROM TourGuide;

-- Lấy tất cả dữ liệu từ bảng Notifications
SELECT * FROM TourGuideAssignment;

--thử câu lệch
select
	u.UserID
	,u.FullName
	,b.BookingDate
	,b.TotalPrice
	,b.TotalPersons
	,t.TourName
	,b.StatusID
	,st.StatusName
	,b.BookingID
	,r.RegionName
	,tt.TourTypeName
	,b.StatusID
	,st.StatusName
from Users u
	join Bookings b on u.UserID = b.UserID
	join BookingDetails bd on b.BookingID = bd.BookingID
	join Tours t on t.TourID = b.TourID
	join Statuses st on st.StatusID = b.StatusID
	join Regions r on r.RegionID = t.RegionID
	join TourTypes tt on tt.TourTypeID = t.TourTypeID
where u.UserID = 1
group by 
	b.BookingID
	,u.UserID
	,u.FullName
	,b.BookingDate
	,b.TotalPrice
	,b.TotalPersons
	,t.TourName
	,b.StatusID
	,st.StatusName
	,r.RegionName
	,tt.TourTypeName
order by b.BookingID
go

select
*
from Tours t
join Regions r on r.RegionID = t.RegionID
join TourTypes tt on tt.TourTypeID = t.TourTypeID
where tt.TourTypeID = 1
and r.RegionID = 1
go

select 
	s.DayNumber
	,s.StartTime
	,s.EndTime
	,s.Activity
	,s.Description
	,s.TourID
	,t.TourName
from Tours t
	left join Schedule s on t.TourID = s.TourID
	where t.TourID = 8

select 
* 
from Tours
where Price between 5000000 and 7000000


--EXEC SearchToursByTypeAndRegion @TourTypeID = null, @RegionID = NULL;
--EXEC GetScheduleByTourID @TourID = 1;
--EXEC GetToursByPriceAndDate;