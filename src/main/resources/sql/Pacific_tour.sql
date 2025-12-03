-- Xóa database nếu đã tồn tại
DROP DATABASE IF EXISTS pacific_tour;
GO

-- Tạo mới database
CREATE DATABASE pacific_tour;
GO

USE pacific_tour;
GO

create table category
(
    id varchar(255)  not null primary key,
    status nvarchar(50),
    title  nvarchar(255) not null,
    type   nvarchar(50)
)
go

create table combo
(	id varchar(255) not null primary key,
    combo_type  nvarchar(100),
    price_combo numeric(10, 2)
)
go

create table destination
(    id varchar(255)  not null primary key,
    city         nvarchar(255) not null,
    country      nvarchar(255) not null,
    full_address nvarchar(500) not null,
    name         nvarchar(255) not null,
    region       nvarchar(255)
)
go

create table guide
(   id varchar(255)  not null primary key,
    address          nvarchar(255) not null,
    email            nvarchar(100) not null,
    experience_years int,
    first_name       nvarchar(50)  not null,
    last_name        nvarchar(50)  not null,
    phone            nvarchar(20)  not null
)
go

create table hotel
(   id varchar(255) not null primary key,
    name       nvarchar(255),
    price      numeric(10, 2),
    type_hotel nvarchar(50)
)
go

create table otp
(   id varchar(255) not null primary key,
    email      varchar(255),
    expires_at datetimeoffset(6),
    otp_code   varchar(10)
)
go

create table promotion
(   id varchar(255)  not null primary key,
    active         bit,
    created_at     datetime2(6),
    delete_at      datetime2(6),
    updated_at     datetime2(6),
    discount       numeric(5, 2),
    end_date       date          not null,
    name_promotion nvarchar(255) not null,
    quantity       int,
    start_date     date          not null,
    status         nvarchar(50) default 'pending'
)
go

create table transport
(
    id             varchar(255)   not null
        primary key,
    active         bit,
    created_at     datetime2(6),
    delete_at      datetime2(6),
    updated_at     datetime2(6),
    name           nvarchar(255)  not null,
    price          numeric(10, 2) not null,
    type_transport nvarchar(50)   not null
)
go

create table users
(
    id             varchar(255)  not null
        primary key,
    active         bit,
    created_at     datetime2(6),
    delete_at      datetime2(6),
    updated_at     datetime2(6),
    address        nvarchar(255),
    avatar_url     varchar(255),
    birthday       date,
    deposit        numeric(18, 2) default 0,
    email          nvarchar(100),
    email_verified bit,
    first_name     nvarchar(50),
    gender         nvarchar(50),
    last_name      nvarchar(50),
    password       nvarchar(255) not null,
    phone          nvarchar(20),
    phone_verified bit,
    role           nvarchar(20),
    status         nvarchar(50)   default 'active',
    username       nvarchar(50)  not null
)
go

create table payment
(   id varchar(255) not null  primary key,
    active         bit,
    created_at     datetime2(6),
    delete_at      datetime2(6),
    updated_at     datetime2(6),
    note           nvarchar(500),
    payment_method nvarchar(50),
    status         nvarchar(20) default 'pending',
    total_amount   numeric(18, 2),
    transaction_id nvarchar(50)
)
go

create table invoice
(   id varchar(255) not null primary key,
    discount       numeric(5, 2) default 0,
    note           nvarchar(500),
    payment_status nvarchar(50),
    subtotal       numeric(18, 2),
    total_amount   numeric(18, 2),
    vat            numeric(5, 2) default 10,
    payment_id     varchar(255)
	constraint FKbaxa82hce5x7dqj0sotnc1cxf references payment
)
go

create unique index UK5vvlr4mmb6jbwiu4dyqwevd0d
    on invoice (payment_id)
    where [payment_id] IS NOT NULL
go

create table tour
(
    id             varchar(255)  not null
        primary key,
    active         bit,
    created_at     datetime2(6),
    delete_at      datetime2(6),
    updated_at     datetime2(6),
    available      bit default 1,
    description    nvarchar(max),
    status         nvarchar(50),
    title          nvarchar(255) not null,
    category_id    varchar(255)
        constraint FKfi3if9cr2rk7llxvky6boui8h
            references category,
    destination_id varchar(255)
        constraint FKkgv42wekext9k6slhxxm26ev3
            references destination,
    guide_id       varchar(255)
        constraint FKbhtkr75hypdnlymbgcommv13j
            references guide,
    promotion_id   varchar(255)
        constraint FKebpp50j3f0ycjcjacu4qvavm6
            references promotion
)
go

create table image
(
    id          varchar(255)  not null
        primary key,
    active      bit,
    created_at  datetime2(6),
    delete_at   datetime2(6),
    updated_at  datetime2(6),
    description nvarchar(255),
    image_url   nvarchar(500) not null,
    tour_id     varchar(255)  not null
        constraint FKmbgj1irf3sy7672g02dedrea
            references tour
            on delete cascade
)
go

create table tour_details
(
    id             varchar(255) not null
        primary key,
    active         bit,
    created_at     datetime2(6),
    delete_at      datetime2(6),
    updated_at     datetime2(6),
    duration       int          not null,
    end_date       date         not null,
    price_adults   numeric(10, 2),
    price_children numeric(10, 2),
    quantity       int          not null,
    rating_avg     float,
    start_date     date         not null,
    status         varchar(50),
    combo_id       varchar(255) not null
        constraint FKjekgh9qlrlh9tnrbchetsa90r
            references combo
            on delete cascade,
    hotel_id       varchar(255) not null
        constraint FKkfwi1rkf52lro3ly2accpvhh6
            references hotel
            on delete cascade,
    tour_id        varchar(255) not null
        constraint FK27g89or55p5ovep89frmbyva2
            references tour
            on delete cascade,
    transport_id   varchar(255) not null
        constraint FKdehvthno0rf0ulsi7k1rtagkc
            references transport
            on delete cascade
)
go

create table itinerary
(
    id             varchar(255) not null
        primary key,
    active         bit,
    created_at     datetime2(6),
    delete_at      datetime2(6),
    updated_at     datetime2(6),
    day_detail     varchar(255),
    day_number     int,
    notes          varchar(255),
    tour_detail_id varchar(255) not null
        constraint FKksovo57qteu5fxgc1iqvnpjmh
            references tour_details
            on delete cascade
)
go

create table blogs
(
    id         varchar(255)  not null
        primary key,
    active     bit,
    created_at datetime2(6),
    delete_at  datetime2(6),
    updated_at datetime2(6),
    content    nvarchar(max) not null,
    status     nvarchar(50) default 'draft',
    title      nvarchar(255) not null,
    user_id    varchar(255)  not null
        constraint FKpg4damav6db6a6fh5peylcni5
            references users
            on delete cascade
)
go

create table booking
(
    id               varchar(255) not null
        primary key,
    active           bit,
    created_at       datetime2(6),
    delete_at        datetime2(6),
    updated_at       datetime2(6),
    adult_num        int,
    booking_status   nvarchar(50) default 'pending',
    children_num     int,
    payment_method   nvarchar(50),
    special_requests nvarchar(255),
    total_amount     numeric(10, 2),
    total_number     int,
    combo_id         varchar(255)
        constraint FK9lbyal6mpaw5ty082ds4pcthy
            references combo
            on delete set null,
    payment_id       varchar(255)
        constraint FK70t92vvx289ayx2hq2v4hdcjl
            references payment
            on delete set null,
    tour_detail_id   varchar(255) not null
        constraint FK1t6kd99fon9agi6qkp14vxj9i
            references tour_details,
    user_id          varchar(255) not null
        constraint FK7udbel7q86k041591kj6lfmvw
            references users
            on delete cascade
)
go

create table review
(
    id         varchar(255) not null
        primary key,
    active     bit,
    created_at datetime2(6),
    delete_at  datetime2(6),
    updated_at datetime2(6),
    comment    nvarchar(max),
    rating     numeric(2, 1),
    booking_id varchar(255) not null
        constraint FKk4xawqohtguy5yx5nnpba6yf3
            references booking
            on delete cascade,
    tour_id    varchar(255) not null
        constraint FK2yxuruefnrj0xan64vi2gg7ag
            references tour,
    user_id    varchar(255) not null
        constraint FK6cpw2nlklblpvc7hyt7ko6v3e
            references users
)
go

create table support
(
    id         varchar(255)  not null
        primary key,
    active     bit,
    created_at datetime2(6),
    delete_at  datetime2(6),
    updated_at datetime2(6),
    message    nvarchar(max) not null,
    status     nvarchar(50) default 'pending',
    subject    nvarchar(255) not null,
    user_id    varchar(255)  not null
        constraint FKscurbcmiqkf55xdnwqkg5teqt
            references users
            on delete cascade
)
go

create table voucher
(
    id           varchar(255)  not null
        primary key,
    active       bit,
    created_at   datetime2(6),
    delete_at    datetime2(6),
    updated_at   datetime2(6),
    code_voucher nvarchar(255) not null,
    discount     numeric(5, 2),
    end_date     date          not null,
    name_voucher nvarchar(255),
    quantity     int,
    start_date   date          not null,
    status       nvarchar(50) default 'pending',
    payment_id   varchar(255)  not null
        constraint FK5jbuv5myb16k0erara8c4owfp
            references payment
)
go

create table wishlist
(
    id      varchar(255) not null
        primary key,
    tour_id varchar(255)
        constraint FKeurw23pxwwlda633hh0dbtsrt
            references tour,
    user_id varchar(255)
        constraint FKtrd6335blsefl2gxpb8lr0gr7
            references users
)
go

INSERT INTO category (id, status, title, type) VALUES
('CATE001', 'active', 'Adventure', 'Outdoor'),
('CATE002', 'active', 'Sports', 'Outdoor'),
('CATE003', 'active', 'Technology', 'Electronics'),
('CATE004', 'active', 'Fashion', 'Clothing'),
('CATE005', 'active', 'Books', 'Education'),
('CATE006', 'active', 'Music', 'Entertainment'),
('CATE007', 'active', 'Health & Fitness', 'Wellness'),
('CATE008', 'active', 'Home Decor', 'Furniture'),
('CATE009', 'active', 'Gaming', 'Electronics'),
('CATE010', 'active', 'Food & Beverage', 'Lifestyle');

INSERT INTO combo (id, combo_type, price_combo) VALUES
('COMBO001', 'Family', 150.00),
('COMBO002', 'Couple', 80.00),
('COMBO003', 'Student', 50.00),
('COMBO004', 'Business', 200.00),
('COMBO005', 'VIP', 300.00),
('COMBO006', 'Premium', 250.00),
('COMBO007', 'Basic', 40.00),
('COMBO008', 'Deluxe', 180.00),
('COMBO009', 'Economy', 60.00),
('COMBO010', 'Ultimate', 350.00);

INSERT INTO destination (id, city, country, full_address, name, region) VALUES
('DES001', 'Hanoi', 'Vietnam', '123 Street, Hanoi', 'Hanoi City', 'North'),
('DES002', 'Ho Chi Minh City', 'Vietnam', '456 Avenue, HCMC', 'Saigon', 'South'),
('DES003', 'Da Nang', 'Vietnam', '789 Boulevard, Da Nang', 'Da Nang Beach', 'Central'),
('DES004', 'Nha Trang', 'Vietnam', '101 Beach Road, Nha Trang', 'Nha Trang Bay', 'Central'),
('DES005', 'Ha Long', 'Vietnam', '202 Island Street, Ha Long', 'Ha Long Bay', 'North'),
('DES006', 'Bangkok', 'Thailand', '303 Sukhumvit Road, Bangkok', 'Bangkok City', 'Southeast Asia'),
('DES007', 'Singapore', 'Singapore', '404 Marina Bay, Singapore', 'Marina Bay Sands', 'Southeast Asia'),
('DES008', 'Tokyo', 'Japan', '505 Shibuya, Tokyo', 'Tokyo Tower', 'East Asia'),
('DES009', 'Paris', 'France', '606 Champs-Elysees, Paris', 'Eiffel Tower', 'Europe'),
('DES010', 'New York', 'USA', '707 5th Avenue, New York', 'Times Square', 'North America');

INSERT INTO guide (id, address, email, experience_years, first_name, last_name, phone) VALUES
('GUI001', '123 Street, Hanoi', 'guide1@example.com', 5, 'John', 'Doe', '123456789'),
('GUI002', '456 Avenue, HCMC', 'guide2@example.com', 8, 'Anna', 'Nguyen', '987654321'),
('GUI003', '789 Boulevard, Da Nang', 'guide3@example.com', 3, 'David', 'Tran', '456123789'),
('GUI004', '101 Beach Road, Nha Trang', 'guide4@example.com', 6, 'Emily', 'Le', '789321456'),
('GUI005', '202 Island Street, Ha Long', 'guide5@example.com', 10, 'Michael', 'Pham', '321654987'),
('GUI006', '303 Sukhumvit Road, Bangkok', 'guide6@example.com', 4, 'Sophia', 'Vo', '159357486'),
('GUI007', '404 Marina Bay, Singapore', 'guide7@example.com', 7, 'Daniel', 'Hoang', '753951852'),
('GUI008', '505 Shibuya, Tokyo', 'guide8@example.com', 12, 'Olivia', 'Bui', '852147963'),
('GUI009', '606 Champs-Elysees, Paris', 'guide9@example.com', 9, 'James', 'Dang', '369258147'),
('GUI010', '707 5th Avenue, New York', 'guide10@example.com', 15, 'Emma', 'Lam', '741852963');

INSERT INTO hotel (id, name, price, type_hotel) VALUES
('HTL001', 'Grand Palace Hotel', 200.00, 'Luxury'),
('HTL002', 'Sunset Resort', 150.00, 'Resort'),
('HTL003', 'City Center Inn', 100.00, 'Budget'),
('HTL004', 'Ocean View Hotel', 180.00, 'Luxury'),
('HTL005', 'Mountain Retreat', 130.00, 'Boutique'),
('HTL006', 'Royal Garden Hotel', 220.00, 'Luxury'),
('HTL007', 'Business Tower Hotel', 170.00, 'Business'),
('HTL008', 'Cozy Stay Hostel', 50.00, 'Hostel'),
('HTL009', 'Lakeside Lodge', 140.00, 'Boutique'),
('HTL010', 'Skyline Suites', 250.00, 'Luxury');

INSERT INTO promotion (id, active, created_at, delete_at, updated_at, discount, end_date, name_promotion, quantity, start_date, status) VALUES
('PROMO001', 1, NOW(), NULL, NOW(), 20.00, '2023-12-31', 'New Year Promo', 100, '2023-01-01', 'active'),
('PROMO002', 1, NOW(), NULL, NOW(), 15.00, '2023-11-30', 'Black Friday Deal', 200, '2023-11-01', 'active'),
('PROMO003', 1, NOW(), NULL, NOW(), 10.00, '2023-10-15', 'Autumn Sale', 150, '2023-10-01', 'active'),
('PROMO004', 1, NOW(), NULL, NOW(), 25.00, '2023-07-31', 'Summer Discount', 120, '2023-07-01', 'active'),
('PROMO005', 1, NOW(), NULL, NOW(), 30.00, '2023-06-15', 'Mid-Year Sale', 180, '2023-06-01', 'active'),
('PROMO006', 1, NOW(), NULL, NOW(), 5.00, '2023-09-30', 'Back to School', 300, '2023-09-01', 'active'),
('PROMO007', 1, NOW(), NULL, NOW(), 12.00, '2023-04-30', 'Spring Festival', 90, '2023-04-01', 'active'),
('PROMO008', 1, NOW(), NULL, NOW(), 50.00, '2023-02-14', 'Valentine Special', 50, '2023-02-01', 'active'),
('PROMO009', 1, NOW(), NULL, NOW(), 40.00, '2023-08-31', 'Independence Day Sale', 70, '2023-08-01', 'active'),
('PROMO010', 1, NOW(), NULL, NOW(), 35.00, '2023-05-05', 'Golden Week Offer', 110, '2023-05-01', 'active');

INSERT INTO transport (id, active, created_at, delete_at, updated_at, name, price, type_transport) VALUES
('TRP001', 1, NOW(), NULL, NOW(), 'Bus', 50.00, 'Land'),
('TRP002', 1, NOW(), NULL, NOW(), 'Train', 80.00, 'Land'),
('TRP003', 1, NOW(), NULL, NOW(), 'Domestic Flight', 150.00, 'Air'),
('TRP004', 1, NOW(), NULL, NOW(), 'International Flight', 500.00, 'Air'),
('TRP005', 1, NOW(), NULL, NOW(), 'Cruise Ship', 300.00, 'Sea'),
('TRP006', 1, NOW(), NULL, NOW(), 'Ferry', 100.00, 'Sea'),
('TRP007', 1, NOW(), NULL, NOW(), 'Car Rental', 60.00, 'Land'),
('TRP008', 1, NOW(), NULL, NOW(), 'Motorbike Rental', 20.00, 'Land'),
('TRP009', 1, NOW(), NULL, NOW(), 'Helicopter Tour', 600.00, 'Air'),
('TRP010', 1, NOW(), NULL, NOW(), 'Bicycle Rental', 15.00, 'Land');

INSERT INTO users (id, active, created_at, delete_at, updated_at, address, avatar_url, birthday, deposit, email, email_verified, first_name, gender, last_name, password, phone, phone_verified, role, status, username) VALUES
('USER001', 1, NOW(), NULL, NOW(), '123 Street, Hanoi', 'http://example.com/avatar1.jpg', '1990-01-01', 0.00, 'user1@example.com', 1, 'John', 'Male', 'Doe', 'password1', '123456789', 1, 'user', 'active', 'johndoe'),
('USER002', 1, NOW(), NULL, NOW(), '456 Avenue, HCMC', 'http://example.com/avatar2.jpg', '1992-02-15', 100.00, 'admin1@example.com', 1, 'Anna', 'Female', 'Nguyen', 'password2', '987654321', 1, 'admin', 'active', 'annanguyen'),
('USER003', 1, NOW(), NULL, NOW(), '789 Boulevard, Da Nang', 'http://example.com/avatar3.jpg', '1995-03-20', 50.00, 'user2@example.com', 1, 'David', 'Male', 'Tran', 'password3', '456123789', 1, 'user', 'active', 'davidtran'),
('USER004', 1, NOW(), NULL, NOW(), '101 Beach Road, Nha Trang', 'http://example.com/avatar4.jpg', '1988-07-10', 200.00, 'admin2@example.com', 1, 'Emily', 'Female', 'Le', 'password4', '789321456', 1, 'admin', 'active', 'emilyle'),
('USER005', 1, NOW(), NULL, NOW(), '202 Island Street, Ha Long', 'http://example.com/avatar5.jpg', '1991-05-25', 30.00, 'user3@example.com', 1, 'Michael', 'Male', 'Pham', 'password5', '321654987', 1, 'user', 'active', 'michaelpham'),
('USER006', 1, NOW(), NULL, NOW(), '303 Sukhumvit Road, Bangkok', 'http://example.com/avatar6.jpg', '1993-08-18', 150.00, 'admin3@example.com', 1, 'Sophia', 'Female', 'Vo', 'password6', '159357486', 1, 'admin', 'active', 'sophiavo'),
('USER007', 1, NOW(), NULL, NOW(), '404 Marina Bay, Singapore', 'http://example.com/avatar7.jpg', '1996-09-30', 70.00, 'user4@example.com', 1, 'Daniel', 'Male', 'Hoang', 'password7', '753951852', 1, 'user', 'active', 'danielhoang'),
('USER008', 1, NOW(), NULL, NOW(), '505 Shibuya, Tokyo', 'http://example.com/avatar8.jpg', '1985-11-11', 300.00, 'admin4@example.com', 1, 'Olivia', 'Female', 'Bui', 'password8', '852147963', 1, 'admin', 'active', 'oliviabui'),
('USER009', 1, NOW(), NULL, NOW(), '606 Champs-Elysees, Paris', 'http://example.com/avatar9.jpg', '1997-12-05', 90.00, 'user5@example.com', 1, 'James', 'Male', 'Dang', 'password9', '369258147', 1, 'user', 'active', 'jamesdang'),
('USER010', 1, NOW(), NULL, NOW(), '707 5th Avenue, New York', 'http://example.com/avatar10.jpg', '1989-04-22', 500.00, 'admin5@example.com', 1, 'Emma', 'Female', 'Lam', 'password10', '741852963', 1, 'admin', 'active', 'emmalam');

INSERT INTO blogs (id, active, created_at, delete_at, updated_at, content, status, title, user_id) VALUES
('BLOG001', 1, NOW(), NULL, NOW(), 'Exploring the beauty of Hanoi.', 'published', 'A Journey Through Hanoi', 'USER001'),
('BLOG002', 1, NOW(), NULL, NOW(), 'The best beaches to visit in Vietnam.', 'draft', 'Top Beaches in Vietnam', 'USER003'),
('BLOG003', 1, NOW(), NULL, NOW(), 'Delicious street food in Ho Chi Minh City.', 'published', 'Street Food Adventure', 'USER002'),
('BLOG004', 1, NOW(), NULL, NOW(), 'A guide to trekking in Sapa.', 'draft', 'Trekking in Sapa', 'USER004'),
('BLOG005', 1, NOW(), NULL, NOW(), 'Exploring ancient temples in Hue.', 'published', 'Hue’s Historic Temples', 'USER005'),
('BLOG006', 1, NOW(), NULL, NOW(), 'A complete guide to Ha Long Bay cruises.', 'published', 'Ha Long Bay Experience', 'USER001'),
('BLOG007', 1, NOW(), NULL, NOW(), 'The hidden gems of Da Nang.', 'draft', 'Hidden Gems in Da Nang', 'USER003'),
('BLOG008', 1, NOW(), NULL, NOW(), 'Why Nha Trang is the perfect summer getaway.', 'published', 'Nha Trang Travel Guide', 'USER002'),
('BLOG009', 1, NOW(), NULL, NOW(), 'Local markets you must visit in Vietnam.', 'draft', 'Vietnam’s Best Markets', 'USER005'),
('BLOG010', 1, NOW(), NULL, NOW(), 'Travel tips for first-time visitors to Vietnam.', 'published', 'Vietnam Travel Tips', 'USER004');

INSERT INTO support (id, active, created_at, delete_at, updated_at, message, status, subject, user_id) VALUES
('SUP001', 1, NOW(), NULL, NOW(), 'Need help with booking', 'pending', 'Booking Issue', 'USER001'),
('SUP002', 1, NOW(), NULL, NOW(), 'Payment not going through', 'resolved', 'Payment Issue', 'USER003'),
('SUP003', 1, NOW(), NULL, NOW(), 'How to cancel a booking?', 'pending', 'Cancellation Request', 'USER002'),
('SUP004', 1, NOW(), NULL, NOW(), 'Issue with login credentials', 'in-progress', 'Login Problem', 'USER004'),
('SUP005', 1, NOW(), NULL, NOW(), 'Request for refund', 'pending', 'Refund Request', 'USER005'),
('SUP006', 1, NOW(), NULL, NOW(), 'Discount code not working', 'resolved', 'Promo Code Issue', 'USER001'),
('SUP007', 1, NOW(), NULL, NOW(), 'Need help with rescheduling my trip', 'in-progress', 'Rescheduling Assistance', 'USER003'),
('SUP008', 1, NOW(), NULL, NOW(), 'Unable to update my profile', 'pending', 'Profile Update Issue', 'USER002'),
('SUP009', 1, NOW(), NULL, NOW(), 'Lost my booking confirmation email', 'resolved', 'Lost Confirmation Email', 'USER005'),
('SUP010', 1, NOW(), NULL, NOW(), 'Inquiry about travel insurance', 'pending', 'Travel Insurance Info', 'USER004');

INSERT INTO tour (id, active, created_at, delete_at, updated_at, available, description, status, title, category_id, destination_id, guide_id, promotion_id) VALUES
('TOUR001', true, NOW(), NULL, NOW(), 1, 'Explore the ancient city of Hanoi.', 'active', 'Hanoi Heritage Tour', 'CATE001', 'DES001', 'GUI001', 'PROMO001'),
('TOUR002', true, NOW(), NULL, NOW(), 1, 'Discover the beaches of Da Nang.', 'active', 'Da Nang Beach Getaway', 'CATE002', 'DES002', 'GUI002', 'PROMO002'),
('TOUR003', true, NOW(), NULL, NOW(), 1, 'Trekking through Sapa mountains.', 'active', 'Sapa Adventure Trek', 'CATE003', 'DES003', 'GUI003', 'PROMO003'),
('TOUR004', true, NOW(), NULL, NOW(), 1, 'A cultural experience in Hue.', 'active', 'Hue Cultural Tour', 'CATE001', 'DES004', 'GUI001', 'PROMO001'),
('TOUR005', true, NOW(), NULL, NOW(), 1, 'Cruising through Ha Long Bay.', 'active', 'Ha Long Bay Cruise', 'CATE002', 'DES005', 'GUI002', 'PROMO002'),
('TOUR006', true, NOW(), NULL, NOW(), 1, 'Island hopping in Nha Trang.', 'active', 'Nha Trang Island Tour', 'CATE003', 'DES006', 'GUI003', 'PROMO003'),
('TOUR007', true, NOW(), NULL, NOW(), 1, 'A day trip to the Mekong Delta.', 'active', 'Mekong Delta Excursion', 'CATE001', 'DES007', 'GUI001', 'PROMO001'),
('TOUR008', true, NOW(), NULL, NOW(), 1, 'Exploring Ho Chi Minh City.', 'active', 'Saigon City Tour', 'CATE002', 'DES008', 'GUI002', 'PROMO002'),
('TOUR009', true, NOW(), NULL, NOW(), 1, 'Enjoy the scenery of Da Lat.', 'active', 'Da Lat Nature Retreat', 'CATE003', 'DES009', 'GUI003', 'PROMO003'),
('TOUR010', true, NOW(), NULL, NOW(), 1, 'Visit the historic sites of Quang Binh.', 'active', 'Phong Nha Cave Adventure', 'CATE001', 'DES010', 'GUI001', 'PROMO001'),
('TOUR011', true, NOW(), NULL, NOW(), 1, 'Explore Hanoi at night.', 'active', 'Hanoi Night Tour', 'CATE001', 'DES001', 'GUI002', 'PROMO002'),
('TOUR012', true, NOW(), NULL, NOW(), 1, 'Da Nang city and Marble Mountains.', 'active', 'Da Nang City Tour', 'CATE002', 'DES002', 'GUI003', 'PROMO003'),
('TOUR013', true, NOW(), NULL, NOW(), 1, 'Sapa local village experience.', 'active', 'Sapa Homestay Experience', 'CATE003', 'DES003', 'GUI001', 'PROMO001'),
('TOUR014', true, NOW(), NULL, NOW(), 1, 'Historical trip to Hue.', 'active', 'Hue Imperial City Tour', 'CATE001', 'DES004', 'GUI002', 'PROMO002'),
('TOUR015', true, NOW(), NULL, NOW(), 1, 'Luxury cruise in Ha Long Bay.', 'active', 'Luxury Ha Long Cruise', 'CATE002', 'DES005', 'GUI003', 'PROMO003'),
('TOUR016', true, NOW(), NULL, NOW(), 1, 'Diving and snorkeling in Nha Trang.', 'active', 'Nha Trang Diving Tour', 'CATE003', 'DES006', 'GUI001', 'PROMO001'),
('TOUR017', true, NOW(), NULL, NOW(), 1, 'Experience rural life in the Mekong.', 'active', 'Mekong Delta Farm Stay', 'CATE001', 'DES007', 'GUI002', 'PROMO002'),
('TOUR018', true, NOW(), NULL, NOW(), 1, 'Learn about the Vietnam War in Saigon.', 'active', 'Saigon War History Tour', 'CATE002', 'DES008', 'GUI003', 'PROMO003'),
('TOUR019', true, NOW(), NULL, NOW(), 1, 'A romantic getaway in Da Lat.', 'active', 'Da Lat Honeymoon Tour', 'CATE003', 'DES009', 'GUI001', 'PROMO001'),
('TOUR020', true, NOW(), NULL, NOW(), 1, 'Exploring the caves of Quang Binh.', 'active', 'Quang Binh Cave Expedition', 'CATE001', 'DES010', 'GUI002', 'PROMO002');

INSERT INTO tour_details (id, active, created_at, delete_at, updated_at, duration, end_date, price_adults, price_children, quantity, rating_avg, start_date, status, combo_id, hotel_id, tour_id, transport_id) VALUES
-- TOUR001
('TD001', 1, NOW(), NULL, NOW(), 3, '2025-03-15', 150.00, 100.00, 30, 4.5, '2025-03-12', 'active', 'COMBO001', 'HTL001', 'TOUR001', 'TRP001'),
('TD002', 1, NOW(), NULL, NOW(), 5, '2025-04-10', 200.00, 130.00, 25, 4.7, '2025-04-06', 'active', 'COMBO002', 'HTL002', 'TOUR001', 'TRP002'),

-- TOUR002
('TD003', 1, NOW(), NULL, NOW(), 4, '2025-05-20', 180.00, 120.00, 40, 4.6, '2025-05-16', 'active', 'COMBO003', 'HTL003', 'TOUR002', 'TRP003'),
('TD004', 1, NOW(), NULL, NOW(), 3, '2025-06-05', 170.00, 110.00, 35, 4.5, '2025-06-02', 'active', 'COMBO004', 'HTL004', 'TOUR002', 'TRP004'),

-- TOUR003
('TD005', 1, NOW(), NULL, NOW(), 6, '2025-07-10', 220.00, 140.00, 20, 4.8, '2025-07-04', 'active', 'COMBO005', 'HTL005', 'TOUR003', 'TRP005'),
('TD006', 1, NOW(), NULL, NOW(), 4, '2025-08-12', 190.00, 125.00, 30, 4.6, '2025-08-08', 'active', 'COMBO006', 'HTL006', 'TOUR003', 'TRP006'),

-- TOUR004
('TD007', 1, NOW(), NULL, NOW(), 5, '2025-09-15', 210.00, 135.00, 25, 4.7, '2025-09-10', 'active', 'COMBO007', 'HTL007', 'TOUR004', 'TRP007'),
('TD008', 1, NOW(), NULL, NOW(), 3, '2025-10-01', 180.00, 120.00, 40, 4.5, '2025-09-28', 'active', 'COMBO008', 'HTL008', 'TOUR004', 'TRP008'),

-- TOUR005
('TD009', 1, NOW(), NULL, NOW(), 7, '2025-11-20', 250.00, 160.00, 15, 4.9, '2025-11-13', 'active', 'COMBO009', 'HTL009', 'TOUR005', 'TRP009'),
('TD010', 1, NOW(), NULL, NOW(), 4, '2025-12-05', 200.00, 130.00, 30, 4.6, '2025-12-01', 'active', 'COMBO010', 'HTL010', 'TOUR005', 'TRP010'),

-- TOUR006 
('TD011', 1, NOW(), NULL, NOW(), 3, '2025-03-10', 150.00, 100.00, 25, 4.5, '2025-03-08', 'active', 'COMBO001', 'HTL001', 'TOUR006', 'TRP001'),
('TD012', 1, NOW(), NULL, NOW(), 5, '2025-04-20', 200.00, 130.00, 20, 4.7, '2025-04-15', 'active', 'COMBO002', 'HTL002', 'TOUR006', 'TRP002'),

-- TOUR007
('TD013', 1, NOW(), NULL, NOW(), 4, '2025-06-25', 170.00, 110.00, 35, 4.6, '2025-06-21', 'active', 'COMBO003', 'HTL003', 'TOUR007', 'TRP003'),
('TD014', 1, NOW(), NULL, NOW(), 3, '2025-07-12', 160.00, 105.00, 30, 4.5, '2025-07-08', 'active', 'COMBO004', 'HTL004', 'TOUR007', 'TRP004'),

-- TOUR008
('TD015', 1, NOW(), NULL, NOW(), 3, '2025-05-10', 160.00, 110.00, 30, 4.5, '2025-05-07', 'active', 'COMBO005', 'HTL005', 'TOUR008', 'TRP005'),
('TD016', 1, NOW(), NULL, NOW(), 4, '2025-06-20', 180.00, 120.00, 25, 4.6, '2025-06-16', 'active', 'COMBO006', 'HTL006', 'TOUR008', 'TRP006'),

-- TOUR009
('TD017', 1, NOW(), NULL, NOW(), 5, '2025-07-15', 200.00, 130.00, 20, 4.7, '2025-07-10', 'active', 'COMBO007', 'HTL007', 'TOUR009', 'TRP007'),
('TD018', 1, NOW(), NULL, NOW(), 3, '2025-08-05', 170.00, 115.00, 35, 4.5, '2025-08-02', 'active', 'COMBO008', 'HTL008', 'TOUR009', 'TRP008'),

-- TOUR010
('TD019', 1, NOW(), NULL, NOW(), 6, '2025-09-10', 220.00, 140.00, 15, 4.8, '2025-09-04', 'active', 'COMBO009', 'HTL009', 'TOUR010', 'TRP009'),
('TD020', 1, NOW(), NULL, NOW(), 4, '2025-10-12', 190.00, 125.00, 30, 4.6, '2025-10-08', 'active', 'COMBO010', 'HTL010', 'TOUR010', 'TRP010'),

-- TOUR011
('TD021', 1, NOW(), NULL, NOW(), 3, '2025-11-05', 170.00, 110.00, 30, 4.5, '2025-11-02', 'active', 'COMBO001', 'HTL001', 'TOUR011', 'TRP001'),
('TD022', 1, NOW(), NULL, NOW(), 5, '2025-12-15', 200.00, 130.00, 20, 4.7, '2025-12-10', 'active', 'COMBO002', 'HTL002', 'TOUR011', 'TRP002'),

-- TOUR012
('TD023', 1, NOW(), NULL, NOW(), 4, '2026-01-20', 180.00, 120.00, 35, 4.6, '2026-01-16', 'active', 'COMBO003', 'HTL003', 'TOUR012', 'TRP003'),
('TD024', 1, NOW(), NULL, NOW(), 3, '2026-02-10', 160.00, 110.00, 30, 4.5, '2026-02-07', 'active', 'COMBO004', 'HTL004', 'TOUR012', 'TRP004'),

-- TOUR013
('TD025', 1, NOW(), NULL, NOW(), 6, '2026-03-25', 230.00, 150.00, 15, 4.8, '2026-03-19', 'active', 'COMBO005', 'HTL005', 'TOUR013', 'TRP005'),
('TD026', 1, NOW(), NULL, NOW(), 4, '2026-04-12', 190.00, 130.00, 25, 4.6, '2026-04-08', 'active', 'COMBO006', 'HTL006', 'TOUR013', 'TRP006'),

-- TOUR014
('TD027', 1, NOW(), NULL, NOW(), 5, '2026-05-10', 210.00, 135.00, 20, 4.7, '2026-05-06', 'active', 'COMBO007', 'HTL007', 'TOUR014', 'TRP007'),
('TD028', 1, NOW(), NULL, NOW(), 3, '2026-06-02', 180.00, 120.00, 40, 4.5, '2026-05-30', 'active', 'COMBO008', 'HTL008', 'TOUR014', 'TRP008'),

-- TOUR015
('TD029', 1, NOW(), NULL, NOW(), 7, '2026-07-20', 250.00, 160.00, 15, 4.9, '2026-07-13', 'active', 'COMBO009', 'HTL009', 'TOUR015', 'TRP009'),
('TD030', 1, NOW(), NULL, NOW(), 4, '2026-08-05', 200.00, 130.00, 30, 4.6, '2026-08-01', 'active', 'COMBO010', 'HTL010', 'TOUR015', 'TRP010'),

-- TOUR016
('TD031', 1, NOW(), NULL, NOW(), 3, '2026-09-10', 150.00, 100.00, 25, 4.5, '2026-09-08', 'active', 'COMBO001', 'HTL001', 'TOUR016', 'TRP001'),
('TD032', 1, NOW(), NULL, NOW(), 5, '2026-10-20', 200.00, 130.00, 20, 4.7, '2026-10-15', 'active', 'COMBO002', 'HTL002', 'TOUR016', 'TRP002'),

-- TOUR017
('TD033', 1, NOW(), NULL, NOW(), 4, '2026-11-25', 170.00, 110.00, 35, 4.6, '2026-11-21', 'active', 'COMBO003', 'HTL003', 'TOUR017', 'TRP003'),
('TD034', 1, NOW(), NULL, NOW(), 3, '2026-12-10', 160.00, 105.00, 30, 4.5, '2026-12-08', 'active', 'COMBO004', 'HTL004', 'TOUR017', 'TRP004'),

-- TOUR018
('TD035', 1, NOW(), NULL, NOW(), 5, '2027-01-15', 210.00, 135.00, 25, 4.7, '2027-01-10', 'active', 'COMBO005', 'HTL005', 'TOUR018', 'TRP005'),
('TD036', 1, NOW(), NULL, NOW(), 3, '2027-02-01', 180.00, 120.00, 40, 4.5, '2027-01-28', 'active', 'COMBO006', 'HTL006', 'TOUR018', 'TRP006'),

-- TOUR019
('TD037', 1, NOW(), NULL, NOW(), 4, '2027-03-10', 200.00, 130.00, 20, 4.6, '2027-03-06', 'active', 'COMBO007', 'HTL007', 'TOUR019', 'TRP007'),
('TD038', 1, NOW(), NULL, NOW(), 6, '2027-04-20', 230.00, 150.00, 15, 4.8, '2027-04-15', 'active', 'COMBO008', 'HTL008', 'TOUR019', 'TRP008'),

-- TOUR020
('TD039', 1, NOW(), NULL, NOW(), 4, '2025-10-30', 210.00, 140.00, 20, 4.7, '2025-10-26', 'active', 'COMBO009', 'HTL009', 'TOUR020', 'TRP009'),
('TD040', 1, NOW(), NULL, NOW(), 6, '2025-11-15', 230.00, 150.00, 15, 4.8, '2025-11-10', 'active', 'COMBO010', 'HTL010', 'TOUR020', 'TRP010');

INSERT INTO itinerary (id, active, created_at, delete_at, updated_at, day_detail, day_number, notes, tour_detail_id) VALUES
-- TOUR001 - TD001
('ITI001', 1, NOW(), NULL, NOW(), 'Arrival and city tour', 1, 'Welcome dinner included', 'TD001'),
('ITI002', 1, NOW(), NULL, NOW(), 'Visit historical sites', 2, 'Lunch at a local restaurant', 'TD001'),
('ITI003', 1, NOW(), NULL, NOW(), 'Excursion to countryside', 3, 'Try traditional activities', 'TD001'),

-- TOUR001 - TD002
('ITI004', 1, NOW(), NULL, NOW(), 'Morning flight and city exploration', 1, 'Relaxing evening', 'TD002'),
('ITI005', 1, NOW(), NULL, NOW(), 'Beach activities', 2, 'Enjoy seafood specialties', 'TD002'),

-- TOUR002 - TD003
('ITI006', 1, NOW(), NULL, NOW(), 'Arrival and check-in', 1, 'Short introduction tour', 'TD003'),
('ITI007', 1, NOW(), NULL, NOW(), 'Island hopping adventure', 2, 'Visit famous islands', 'TD003'),
('ITI008', 1, NOW(), NULL, NOW(), 'Snorkeling and relaxation', 3, 'Explore marine life', 'TD003'),
('ITI009', 1, NOW(), NULL, NOW(), 'Departure and shopping', 4, 'Souvenir shopping', 'TD003'),

-- TOUR002 - TD004
('ITI010', 1, NOW(), NULL, NOW(), 'City tour with guide', 1, 'Evening cultural show', 'TD004'),
('ITI011', 1, NOW(), NULL, NOW(), 'Mountain trekking', 2, 'Scenic photography', 'TD004'),

-- TOUR003 - TD005
('ITI012', 1, NOW(), NULL, NOW(), 'Start of countryside adventure', 1, 'Meet local artisans', 'TD005'),
('ITI013', 1, NOW(), NULL, NOW(), 'Explore hidden caves', 2, 'Dinner in a traditional setting', 'TD005'),
('ITI014', 1, NOW(), NULL, NOW(), 'Rafting and wildlife spotting', 3, 'Stay in an eco-lodge', 'TD005'),

-- TOUR003 - TD006
('ITI015', 1, NOW(), NULL, NOW(), 'Cultural heritage walk', 1, 'Visit old temples', 'TD006'),
('ITI016', 1, NOW(), NULL, NOW(), 'Local food tasting', 2, 'Participate in cooking class', 'TD006'),
('ITI017', 1, NOW(), NULL, NOW(), 'Departure and summary', 3, 'Personalized souvenirs', 'TD006'),

-- TOUR004 - TD007
('ITI018', 1, NOW(), NULL, NOW(), 'Beach arrival and relaxation', 1, 'Sunset dinner', 'TD007'),
('ITI019', 1, NOW(), NULL, NOW(), 'Water sports adventure', 2, 'Scuba diving and jet skiing', 'TD007'),
('ITI020', 1, NOW(), NULL, NOW(), 'Island tour and snorkeling', 3, 'Enjoy coconut drinks', 'TD007'),
('ITI021', 1, NOW(), NULL, NOW(), 'Farewell breakfast', 4, 'Fly back home', 'TD007'),

-- TOUR004 - TD008
('ITI022', 1, NOW(), NULL, NOW(), 'Explore the famous landmarks', 1, 'City skyline tour', 'TD008'),
('ITI023', 1, NOW(), NULL, NOW(), 'Shopping and nightlife', 2, 'Experience local music', 'TD008'),

-- TOUR005 - TD009
('ITI024', 1, NOW(), NULL, NOW(), 'Start of jungle trek', 1, 'Overnight camping', 'TD009'),
('ITI025', 1, NOW(), NULL, NOW(), 'Wildlife safari', 2, 'Spot rare animals', 'TD009'),
('ITI026', 1, NOW(), NULL, NOW(), 'River kayaking and survival skills', 3, 'Learn from expert guides', 'TD009'),

-- TOUR005 - TD010
('ITI027', 1, NOW(), NULL, NOW(), 'Arrival and city discovery', 1, 'Relaxing spa evening', 'TD010'),
('ITI028', 1, NOW(), NULL, NOW(), 'Historical tour of ancient ruins', 2, 'Sunset photography', 'TD010'),
('ITI029', 1, NOW(), NULL, NOW(), 'Local cuisine exploration', 3, 'Food tour with expert chefs', 'TD010'),

-- TOUR006 - TD011
('ITI030', 1, NOW(), NULL, NOW(), 'Explore countryside farms', 1, 'Participate in fruit picking', 'TD011'),
('ITI031', 1, NOW(), NULL, NOW(), 'Hot air balloon ride', 2, 'Bird-eye view of landscapes', 'TD011'),

-- TOUR007 - TD013
('ITI032', 1, NOW(), NULL, NOW(), 'Cultural dance and welcome ceremony', 1, 'Enjoy live music', 'TD013'),
('ITI033', 1, NOW(), NULL, NOW(), 'Exploration of museums', 2, 'Learn about ancient history', 'TD013'),

-- TOUR008 - TD015
('ITI034', 1, NOW(), NULL, NOW(), 'Trekking in the mountains', 1, 'Meet indigenous tribes', 'TD015'),
('ITI035', 1, NOW(), NULL, NOW(), 'Campfire and storytelling', 2, 'Enjoy folk tales', 'TD015'),

-- TOUR009 - TD017
('ITI036', 1, NOW(), NULL, NOW(), 'Arrival and relaxation', 1, 'Enjoy a luxury spa', 'TD017'),
('ITI037', 1, NOW(), NULL, NOW(), 'Guided city cycling tour', 2, 'Visit hidden alleys', 'TD017'),

-- TOUR010 - TD019
('ITI038', 1, NOW(), NULL, NOW(), 'Explore famous temples', 1, 'Learn about rituals', 'TD019'),
('ITI039', 1, NOW(), NULL, NOW(), 'Boat cruise along river', 2, 'Sunset cocktail party', 'TD019'),

-- TOUR011 - TD021
('ITI040', 1, NOW(), NULL, NOW(), 'Start of adventure', 1, 'Meet local guides', 'TD021'),
('ITI041', 1, NOW(), NULL, NOW(), 'Hiking through forests', 2, 'Picnic with a view', 'TD021'),
('ITI042', 1, NOW(), NULL, NOW(), 'Cave exploration', 3, 'Discover ancient carvings', 'TD021'),

-- TOUR012 - TD023
('ITI043', 1, NOW(), NULL, NOW(), 'Cityscape tour', 1, 'Explore vibrant markets', 'TD023'),
('ITI044', 1, NOW(), NULL, NOW(), 'Traditional handicraft workshop', 2, 'Make your own souvenir', 'TD023'),

-- TOUR020 - TD039
('ITI045', 1, NOW(), NULL, NOW(), 'Final tour adventure', 1, 'Exclusive farewell party', 'TD039');

INSERT INTO image (id, active, created_at, delete_at, updated_at, description, image_url, tour_id) VALUES
-- TOUR001
('IMG001', 1, NOW(), NULL, NOW(), 'Beautiful sunset view', 'http://example.com/tour1_img1.jpg', 'TOUR001'),
('IMG002', 1, NOW(), NULL, NOW(), 'City skyline at night', 'http://example.com/tour1_img2.jpg', 'TOUR001'),
('IMG003', 1, NOW(), NULL, NOW(), 'Famous landmark', 'http://example.com/tour1_img3.jpg', 'TOUR001'),

-- TOUR002
('IMG004', 1, NOW(), NULL, NOW(), 'Beach and crystal clear water', 'http://example.com/tour2_img1.jpg', 'TOUR002'),
('IMG005', 1, NOW(), NULL, NOW(), 'Island tour experience', 'http://example.com/tour2_img2.jpg', 'TOUR002'),
('IMG006', 1, NOW(), NULL, NOW(), 'Snorkeling fun', 'http://example.com/tour2_img3.jpg', 'TOUR002'),

-- TOUR003
('IMG007', 1, NOW(), NULL, NOW(), 'Mountain adventure', 'http://example.com/tour3_img1.jpg', 'TOUR003'),
('IMG008', 1, NOW(), NULL, NOW(), 'Hiking trails', 'http://example.com/tour3_img2.jpg', 'TOUR003'),
('IMG009', 1, NOW(), NULL, NOW(), 'Scenic viewpoint', 'http://example.com/tour3_img3.jpg', 'TOUR003'),

-- TOUR004
('IMG010', 1, NOW(), NULL, NOW(), 'Cultural festival', 'http://example.com/tour4_img1.jpg', 'TOUR004'),
('IMG011', 1, NOW(), NULL, NOW(), 'Traditional dance performance', 'http://example.com/tour4_img2.jpg', 'TOUR004'),
('IMG012', 1, NOW(), NULL, NOW(), 'Local street market', 'http://example.com/tour4_img3.jpg', 'TOUR004'),

-- TOUR005
('IMG013', 1, NOW(), NULL, NOW(), 'Luxury cruise tour', 'http://example.com/tour5_img1.jpg', 'TOUR005'),
('IMG014', 1, NOW(), NULL, NOW(), 'Fine dining on the boat', 'http://example.com/tour5_img2.jpg', 'TOUR005'),
('IMG015', 1, NOW(), NULL, NOW(), 'Sunset from the deck', 'http://example.com/tour5_img3.jpg', 'TOUR005'),

-- TOUR006
('IMG016', 1, NOW(), NULL, NOW(), 'Camping in the jungle', 'http://example.com/tour6_img1.jpg', 'TOUR006'),
('IMG017', 1, NOW(), NULL, NOW(), 'Wildlife photography', 'http://example.com/tour6_img2.jpg', 'TOUR006'),
('IMG018', 1, NOW(), NULL, NOW(), 'Waterfall exploration', 'http://example.com/tour6_img3.jpg', 'TOUR006'),

-- TOUR007
('IMG019', 1, NOW(), NULL, NOW(), 'Winter snowboarding', 'http://example.com/tour7_img1.jpg', 'TOUR007'),
('IMG020', 1, NOW(), NULL, NOW(), 'Cozy cabin in the mountains', 'http://example.com/tour7_img2.jpg', 'TOUR007'),
('IMG021', 1, NOW(), NULL, NOW(), 'Snowy landscape', 'http://example.com/tour7_img3.jpg', 'TOUR007'),

-- TOUR008
('IMG022', 1, NOW(), NULL, NOW(), 'Safari adventure', 'http://example.com/tour8_img1.jpg', 'TOUR008'),
('IMG023', 1, NOW(), NULL, NOW(), 'Wild animals in nature', 'http://example.com/tour8_img2.jpg', 'TOUR008'),
('IMG024', 1, NOW(), NULL, NOW(), 'Desert landscape', 'http://example.com/tour8_img3.jpg', 'TOUR008'),

-- TOUR009
('IMG025', 1, NOW(), NULL, NOW(), 'Ancient temple visit', 'http://example.com/tour9_img1.jpg', 'TOUR009'),
('IMG026', 1, NOW(), NULL, NOW(), 'Spiritual retreat', 'http://example.com/tour9_img2.jpg', 'TOUR009'),
('IMG027', 1, NOW(), NULL, NOW(), 'Monk meditation session', 'http://example.com/tour9_img3.jpg', 'TOUR009'),

-- TOUR010
('IMG028', 1, NOW(), NULL, NOW(), 'Cycling through vineyards', 'http://example.com/tour10_img1.jpg', 'TOUR010'),
('IMG029', 1, NOW(), NULL, NOW(), 'Wine tasting experience', 'http://example.com/tour10_img2.jpg', 'TOUR010'),
('IMG030', 1, NOW(), NULL, NOW(), 'Countryside sunset', 'http://example.com/tour10_img3.jpg', 'TOUR010'),

-- TOUR011 to TOUR020 (Tương tự như trên)
('IMG031', 1, NOW(), NULL, NOW(), 'Rainforest trek', 'http://example.com/tour11_img1.jpg', 'TOUR011'),
('IMG032', 1, NOW(), NULL, NOW(), 'Nature trail adventure', 'http://example.com/tour11_img2.jpg', 'TOUR011'),
('IMG033', 1, NOW(), NULL, NOW(), 'Eco-tourism site', 'http://example.com/tour11_img3.jpg', 'TOUR011'),

('IMG034', 1, NOW(), NULL, NOW(), 'Historical town tour', 'http://example.com/tour12_img1.jpg', 'TOUR012'),
('IMG035', 1, NOW(), NULL, NOW(), 'Architectural landmarks', 'http://example.com/tour12_img2.jpg', 'TOUR012'),
('IMG036', 1, NOW(), NULL, NOW(), 'Museum visit', 'http://example.com/tour12_img3.jpg', 'TOUR012'),

('IMG037', 1, NOW(), NULL, NOW(), 'Festival celebration', 'http://example.com/tour13_img1.jpg', 'TOUR013'),
('IMG038', 1, NOW(), NULL, NOW(), 'Street food festival', 'http://example.com/tour13_img2.jpg', 'TOUR013'),
('IMG039', 1, NOW(), NULL, NOW(), 'Fireworks show', 'http://example.com/tour13_img3.jpg', 'TOUR013'),

('IMG040', 1, NOW(), NULL, NOW(), 'Diving in coral reefs', 'http://example.com/tour14_img1.jpg', 'TOUR014'),
('IMG041', 1, NOW(), NULL, NOW(), 'Underwater photography', 'http://example.com/tour14_img2.jpg', 'TOUR014'),
('IMG042', 1, NOW(), NULL, NOW(), 'Deep-sea exploration', 'http://example.com/tour14_img3.jpg', 'TOUR014');

INSERT INTO payment (id, active, created_at, delete_at, updated_at, note, payment_method, status, total_amount, transaction_id) VALUES
('PM001', 1, NOW(), NULL, NOW(), 'Paid via credit card', 'Credit Card', 'Completed', 1240.00 , 'TXN001'),
('PM002', 1, NOW(), NULL, NOW(), 'Paid via PayPal', 'PayPal', 'Completed', 1230.00 , 'TXN002'),
('PM003', 1, NOW(), NULL, NOW(), 'Pending bank transfer', 'Bank Transfer', 'Pending', 1780.00 , 'TXN003'),
('PM004', 1, NOW(), NULL, NOW(), 'Successful payment', 'Credit Card', 'Completed', 2000.00 , 'TXN004'),
('PM005', 1, NOW(), NULL, NOW(), 'Cash payment', 'Cash', 'Completed', 2100.00 , 'TXN005'),
('PM006', 1, NOW(), NULL, NOW(), 'Refunded transaction', 'Credit Card', 'Refunded', 950.00, 'TXN006'),
('PM007', 1, NOW(), NULL, NOW(), 'Awaiting confirmation', 'PayPal', 'Pending', 1750.00, 'TXN007'),
('PM008', 1, NOW(), NULL, NOW(), 'Payment received', 'Bank Transfer', 'Completed', 1130.00, 'TXN008'),
('PM009', 1, NOW(), NULL, NOW(), 'Processing', 'Credit Card', 'Processing', 1950.00, 'TXN009'),
('PM010', 1, NOW(), NULL, NOW(), 'Paid via mobile wallet', 'Mobile Wallet', 'Completed', 1500.00, 'TXN010');

INSERT INTO invoice (id, discount, note, payment_status, subtotal, total_amount, vat, payment_id) VALUES
('INV001', 50.00, 'Early bird discount', 'Paid', 500.00, 450.00, 10.00, 'PM001'),
('INV002', 20.00, 'Limited-time offer', 'Paid', 270.00, 250.00, 5.00, 'PM002'),
('INV003', 0.00, 'Standard invoice', 'Pending', 700.00, 700.00, 15.00, 'PM003'),
('INV004', 30.00, 'Promotional discount applied', 'Paid', 330.00, 300.00, 8.00, 'PM004'),
('INV005', 10.00, 'In-store discount', 'Paid', 160.00, 150.00, 3.00, 'PM005'),
('INV006', 40.00, 'Refund issued', 'Refunded', 440.00, 400.00, 9.00, 'PM006'),
('INV007', 0.00, 'Awaiting bank confirmation', 'Pending', 600.00, 600.00, 12.00, 'PM007'),
('INV008', 20.00, 'Loyalty program discount', 'Paid', 470.00, 450.00, 11.00, 'PM008'),
('INV009', 15.00, 'Processing invoice', 'Processing', 365.00, 350.00, 7.00, 'PM009'),
('INV010', 25.00, 'Special deal', 'Paid', 575.00, 550.00, 14.00, 'PM010');

INSERT INTO voucher (id, active, created_at, delete_at, updated_at, code_voucher, discount, end_date, name_voucher, quantity, start_date, status, payment_id) VALUES
('VCH001', 1, NOW(), NULL, NOW(), 'DISCOUNT50', 50.00, '2025-12-31', 'Holiday Special', 100, '2025-01-01', 'active', 'PM001'),
('VCH002', 1, NOW(), NULL, NOW(), 'SAVE20', 20.00, '2025-11-30', 'Limited Offer', 50, '2025-02-01', 'active', 'PM002'),
('VCH003', 1, NOW(), NULL, NOW(), 'EARLYBIRD', 30.00, '2025-10-15', 'Early Bird Deal', 80, '2025-03-01', 'active', 'PM004'),
('VCH004', 1, NOW(), NULL, NOW(), 'CASHBACK10', 10.00, '2025-09-30', 'Cashback Bonus', 200, '2025-04-01', 'active', 'PM005'),
('VCH005', 1, NOW(), NULL, NOW(), 'REFUND40', 40.00, '2025-08-31', 'Refund Reward', 30, '2025-05-01', 'active', 'PM006'),
('VCH006', 1, NOW(), NULL, NOW(), 'LOYALTY25', 25.00, '2025-07-30', 'Loyalty Reward', 150, '2025-06-01', 'active', 'PM008'),
('VCH007', 1, NOW(), NULL, NOW(), 'WELCOME15', 15.00, '2025-06-30', 'Welcome Bonus', 250, '2025-07-01', 'active', 'PM009'),
('VCH008', 1, NOW(), NULL, NOW(), 'VIP100', 100.00, '2025-05-31', 'VIP Exclusive', 20, '2025-08-01', 'active', 'PM010'),
('VCH009', 1, NOW(), NULL, NOW(), 'SEASONAL5', 5.00, '2025-04-30', 'Seasonal Discount', 500, '2025-09-01', 'active', 'PM003'),
('VCH010', 1, NOW(), NULL, NOW(), 'FLASHSALE30', 30.00, '2025-03-31', 'Flash Sale Deal', 100, '2025-10-01', 'active', 'PM007');

INSERT INTO booking (id, active, created_at, delete_at, updated_at, adult_num, booking_status, children_num, payment_method, special_requests, total_amount, total_number, combo_id, payment_id, tour_detail_id, user_id) VALUES
-- USER001 với 2 booking
('BK001', 1, '2024-02-10', NULL, NOW(), 2, 'Confirmed', 1, 'Credit Card', 'Near window seat', 500.00, 3, 'COMBO001', 'PM001', 'TD001', 'USER001'),
('BK002', 1, '2024-06-15', NULL, NOW(), 1, 'Pending', 0, 'PayPal', NULL, 250.00, 1, 'COMBO002', 'PM002', 'TD002', 'USER001'),

-- USER002 với 1 booking
('BK003', 1, '2024-02-05', NULL, NOW(), 3, 'Completed', 2, 'Bank Transfer', 'Vegetarian meals', 800.00, 5, 'COMBO003', 'PM003', 'TD003', 'USER002'),

-- USER003 với 3 booking
('BK004', 1, '2024-09-20', NULL, NOW(), 2, 'Confirmed', 1, 'Cash', NULL, 450.00, 3, 'COMBO001', 'PM004', 'TD004', 'USER003'),
('BK005', 1, '2024-02-18', NULL, NOW(), 4, 'Pending', 2, 'Credit Card', 'Extra luggage', 1200.00, 6, 'COMBO004', 'PM005', 'TD005', 'USER003'),
('BK006', 1, '2024-11-12', NULL, NOW(), 1, 'Confirmed', 0, 'Mobile Wallet', NULL, 300.00, 1, 'COMBO002', 'PM006', 'TD006', 'USER003'),

-- USER004 với 2 booking
('BK007', 1, '2024-01-30', NULL, NOW(), 2, 'Completed', 2, 'Bank Transfer', 'Late check-in', 650.00, 4, 'COMBO005', 'PM007', 'TD007', 'USER004'),
('BK008', 1, '2024-12-20', NULL, NOW(), 3, 'Confirmed', 1, 'Credit Card', NULL, 900.00, 4, 'COMBO006', 'PM008', 'TD008', 'USER004'),

-- USER005 với 1 booking
('BK009', 1, '2024-07-05', NULL, NOW(), 1, 'Pending', 1, 'Cash', NULL, 350.00, 2, 'COMBO007', 'PM009', 'TD009', 'USER005'),

-- USER006 với 3 booking
('BK010', 1, '2024-02-22', NULL, NOW(), 2, 'Confirmed', 1, 'PayPal', 'Honeymoon package', 700.00, 3, 'COMBO008', 'PM010', 'TD010', 'USER006'),
('BK011', 1, '2024-09-25', NULL, NOW(), 4, 'Completed', 2, 'Bank Transfer', NULL, 1400.00, 6, 'COMBO009', 'PM001', 'TD011', 'USER006'),
('BK012', 1, '2024-11-18', NULL, NOW(), 3, 'Pending', 1, 'Credit Card', 'Extra bed', 950.00, 4, 'COMBO010', 'PM002', 'TD012', 'USER006'),

-- USER007 với 2 booking
('BK013', 1, '2024-04-10', NULL, NOW(), 1, 'Confirmed', 0, 'Mobile Wallet', NULL, 300.00, 1, 'COMBO001', 'PM003', 'TD013', 'USER007'),
('BK014', 1, '2024-02-08', NULL, NOW(), 2, 'Completed', 1, 'Credit Card', 'Vegan meals', 500.00, 3, 'COMBO002', 'PM004', 'TD014', 'USER007'),

-- USER008 với 1 booking
('BK015', 1, '2024-10-05', NULL, NOW(), 3, 'Pending', 2, 'PayPal', NULL, 800.00, 5, 'COMBO003', 'PM005', 'TD015', 'USER008'),

-- USER009 với 3 booking
('BK016', 1, '2024-08-20', NULL, NOW(), 2, 'Confirmed', 1, 'Cash', 'Sea view', 600.00, 3, 'COMBO004', 'PM006', 'TD016', 'USER009'),
('BK017', 1, '2024-02-12', NULL, NOW(), 4, 'Completed', 2, 'Credit Card', NULL, 1200.00, 6, 'COMBO005', 'PM007', 'TD017', 'USER009'),
('BK018', 1, '2024-05-07', NULL, NOW(), 1, 'Confirmed', 0, 'Bank Transfer', NULL, 350.00, 1, 'COMBO006', 'PM008', 'TD018', 'USER009'),

-- USER010 với 2 booking
('BK019', 1, '2024-07-25', NULL, NOW(), 3, 'Pending', 1, 'Credit Card', 'Extra night stay', 850.00, 4, 'COMBO007', 'PM009', 'TD019', 'USER010'),
('BK020', 1, '2024-02-28', NULL, NOW(), 2, 'Completed', 0, 'Mobile Wallet', NULL, 500.00, 2, 'COMBO008', 'PM010', 'TD020', 'USER010');
-- Insert sample data into category table
INSERT INTO category (ID, STATUS, TITLE, TYPE) VALUES
                                                   ('CAT001', 'active', 'Adventure', 'Outdoor'),
                                                   ('CAT002', 'active', 'Relaxation', 'Indoor'),
                                                   ('CAT003', 'active', 'Sports', 'Outdoor'),
                                                   ('CAT004', 'active', 'Technology', 'Indoor'),
                                                   ('CAT005', 'active', 'Music', 'Indoor'),
                                                   ('CAT006', 'active', 'Art', 'Indoor'),
                                                   ('CAT007', 'active', 'Fitness', 'Outdoor'),
                                                   ('CAT008', 'active', 'Cooking', 'Indoor'),
                                                   ('CAT009', 'active', 'Travel', 'Outdoor'),
                                                   ('CAT010', 'active', 'Photography', 'Outdoor');


-- Insert sample data into destination table
INSERT INTO destination (id, city, country, full_address, name, region) VALUES
                                                                            ('DEST001', 'Hanoi', 'Vietnam', '123 Street, Hanoi', 'Hanoi City', 'North'),
                                                                            ('DEST002', 'Ho Chi Minh City', 'Vietnam', '456 Avenue, HCMC', 'Saigon', 'South'),
                                                                            ('DEST003', 'Da Nang', 'Vietnam', '789 Road, Da Nang', 'Da Nang Beach', 'Central'),
                                                                            ('DEST004', 'Nha Trang', 'Vietnam', '101 Beach St, Nha Trang', 'Nha Trang Bay', 'Central'),
                                                                            ('DEST005', 'Phu Quoc', 'Vietnam', '202 Island Rd, Phu Quoc', 'Phu Quoc Island', 'South'),
                                                                            ('DEST006', 'Bangkok', 'Thailand', '303 Sukhumvit Rd, Bangkok', 'Bangkok City', 'Central'),
                                                                            ('DEST007', 'Singapore', 'Singapore', '404 Orchard Rd, Singapore', 'Singapore City', 'Southeast Asia'),
                                                                            ('DEST008', 'Tokyo', 'Japan', '505 Shibuya, Tokyo', 'Tokyo Metropolis', 'East Asia'),
                                                                            ('DEST009', 'Paris', 'France', '606 Champs-Élysées, Paris', 'Paris City', 'Europe'),
                                                                            ('DEST010', 'New York', 'USA', '707 Fifth Avenue, New York', 'New York City', 'North America');

-- Insert sample data into guide table
INSERT INTO guide (id, address, email, experience_years, first_name, last_name, phone) VALUES
                                                                                           ('GUIDE001', '789 Road, Hanoi', 'guide1@example.com', 5, 'John', 'Doe', '0123456789'),
                                                                                           ('GUIDE002', '101 Street, HCMC', 'guide2@example.com', 3, 'Jane', 'Smith', '0987654321'),
                                                                                           ('GUIDE003', '456 Avenue, Da Nang', 'guide3@example.com', 7, 'Alice', 'Nguyen', '0365478921'),
                                                                                           ('GUIDE004', '222 Beach St, Nha Trang', 'guide4@example.com', 6, 'David', 'Tran', '0978523641'),
                                                                                           ('GUIDE005', '333 Island Rd, Phu Quoc', 'guide5@example.com', 4, 'Emma', 'Le', '0832567894'),
                                                                                           ('GUIDE006', '101 Sukhumvit Rd, Bangkok', 'guide6@example.com', 8, 'Michael', 'Wong', '0654789321'),
                                                                                           ('GUIDE007', '202 Orchard Rd, Singapore', 'guide7@example.com', 9, 'Sophia', 'Tan', '0765893245'),
                                                                                           ('GUIDE008', '303 Shibuya, Tokyo', 'guide8@example.com', 10, 'Liam', 'Kobayashi', '0954236781'),
                                                                                           ('GUIDE009', '404 Champs-Élysées, Paris', 'guide9@example.com', 12, 'Olivia', 'Dupont', '0332145698'),
                                                                                           ('GUIDE010', '505 Fifth Avenue, New York', 'guide10@example.com', 15, 'Noah', 'Johnson', '0987456321');


-- Insert sample data into hotel table
INSERT INTO hotel (id, name, price, type_hotel) VALUES
                                                    ('HOTEL001', 'Luxury Hotel', 150.00, '5-star'),
                                                    ('HOTEL002', 'Budget Hotel', 50.00, '3-star'),
                                                    ('HOTEL003', 'Riverside Resort', 120.00, '4-star'),
                                                    ('HOTEL004', 'Mountain View Lodge', 90.00, '3-star'),
                                                    ('HOTEL005', 'Seaside Inn', 80.00, '3-star'),
                                                    ('HOTEL006', 'Grand Palace Hotel', 200.00, '5-star'),
                                                    ('HOTEL007', 'Business Suites', 110.00, '4-star'),
                                                    ('HOTEL008', 'Eco Friendly Hotel', 70.00, '3-star'),
                                                    ('HOTEL009', 'Boutique Stay', 130.00, '4-star'),
                                                    ('HOTEL010', 'Skyline Tower Hotel', 180.00, '5-star');


-- Insert sample data into payment table
INSERT INTO payment (id, active, created_at, delete_at, updated_at, note, payment_method, status, total_amount, transaction_id) VALUES
                                                                                                                                    ('PAY001', 1, '2023-01-01 10:00:00', NULL, '2023-01-01 10:00:00', 'Payment for tour', 'Credit Card', 'completed', 200.00, 'TRANS1'),
                                                                                                                                    ('PAY002', 1, '2023-01-02 11:00:00', NULL, '2023-01-02 11:00:00', 'Payment for tour', 'PayPal', 'pending', 150.00, 'TRANS2'),
                                                                                                                                    ('PAY003', 1, '2023-01-03 12:30:00', NULL, '2023-01-03 12:30:00', 'Flight booking', 'Bank Transfer', 'completed', 300.00, 'TRANS3'),
                                                                                                                                    ('PAY004', 1, '2023-01-04 09:45:00', NULL, '2023-01-04 09:45:00', 'Hotel reservation', 'Credit Card', 'failed', 180.00, 'TRANS4'),
                                                                                                                                    ('PAY005', 1, '2023-01-05 14:15:00', NULL, '2023-01-05 14:15:00', 'Payment for tour', 'Debit Card', 'completed', 220.00, 'TRANS5'),
                                                                                                                                    ('PAY006', 1, '2023-01-06 16:20:00', NULL, '2023-01-06 16:20:00', 'Car rental payment', 'PayPal', 'completed', 90.00, 'TRANS6'),
                                                                                                                                    ('PAY007', 1, '2023-01-07 08:10:00', NULL, '2023-01-07 08:10:00', 'Cruise booking', 'Bank Transfer', 'pending', 500.00, 'TRANS7'),
                                                                                                                                    ('PAY008', 1, '2023-01-08 19:25:00', NULL, '2023-01-08 19:25:00', 'Restaurant bill', 'Credit Card', 'completed', 75.00, 'TRANS8'),
                                                                                                                                    ('PAY009', 1, '2023-01-09 22:05:00', NULL, '2023-01-09 22:05:00', 'Tourist attraction ticket', 'Cash', 'completed', 50.00, 'TRANS9'),
                                                                                                                                    ('PAY010', 1, '2023-01-10 11:30:00', NULL, '2023-01-10 11:30:00', 'Bus ticket payment', 'Debit Card', 'refunded', 30.00, 'TRANS10');


-- Insert sample data into tour table
INSERT INTO tour (id, active, created_at, delete_at, updated_at, available, description, duration, status, thumbnail_url, title, category_id, destination_id, guide_id) VALUES
                                                                                                                                                                            ('TOUR001', 1, '2023-01-01 10:00:00', NULL, '2023-01-01 10:00:00', 1, 'Exciting adventure tour', 7, 'active', 'url1', 'Adventure Tour', 'CAT001', 'DEST001', 'GUIDE001'),
                                                                                                                                                                            ('TOUR002', 1, '2023-01-02 11:00:00', NULL, '2023-01-02 11:00:00', 1, 'Relaxing indoor tour', 3, 'active', 'url2', 'Relaxation Tour', 'CAT002', 'DEST002', 'GUIDE002'),
                                                                                                                                                                            ('TOUR003', 1, '2023-01-03 09:30:00', NULL, '2023-01-03 09:30:00', 1, 'Mountain hiking experience', 5, 'active', 'url3', 'Hiking Tour', 'CAT003', 'DEST003', 'GUIDE003'),
                                                                                                                                                                            ('TOUR004', 1, '2023-01-04 14:45:00', NULL, '2023-01-04 14:45:00', 1, 'Cultural exploration tour', 4, 'active', 'url4', 'Cultural Tour', 'CAT004', 'DEST004', 'GUIDE004'),
                                                                                                                                                                            ('TOUR005', 1, '2023-01-05 08:15:00', NULL, '2023-01-05 08:15:00', 1, 'Scenic island getaway', 6, 'active', 'url5', 'Island Tour', 'CAT005', 'DEST005', 'GUIDE005'),
                                                                                                                                                                            ('TOUR006', 1, '2023-01-06 10:20:00', NULL, '2023-01-06 10:20:00', 1, 'City sightseeing tour', 2, 'active', 'url6', 'City Tour', 'CAT006', 'DEST006', 'GUIDE006')
INSERT INTO review (id, active, created_at, delete_at, updated_at, comment, rating, booking_id, tour_id, user_id)
VALUES
('RV001', 1, NOW(), NULL, NOW(), 'Great experience, highly recommended!', 5, 'BK001', 'TOUR001', 'USER001'),
('RV002', 1, NOW(), NULL, NOW(), 'Good service but a bit expensive.', 4, 'BK002', 'TOUR002', 'USER002'),
('RV003', 1, NOW(), NULL, NOW(), 'Amazing tour, well-organized.', 5, 'BK003', 'TOUR003', 'USER003'),
('RV004', 1, NOW(), NULL, NOW(), 'Nice tour but could be improved.', 3, 'BK004', 'TOUR004', 'USER004'),
('RV005', 1, NOW(), NULL, NOW(), 'The guide was very knowledgeable!', 5, 'BK005', 'TOUR005', 'USER005'),
('RV006', 1, NOW(), NULL, NOW(), 'Good trip but hotel quality was average.', 4, 'BK006', 'TOUR006', 'USER006'),
('RV007', 1, NOW(), NULL, NOW(), 'Loved the scenery and activities.', 5, 'BK007', 'TOUR007', 'USER007'),
('RV008', 1, NOW(), NULL, NOW(), 'Food was excellent, very authentic.', 5, 'BK008', 'TOUR008', 'USER008'),
('RV009', 1, NOW(), NULL, NOW(), 'Could have been better planned.', 3, 'BK009', 'TOUR009', 'USER009'),
('RV010', 1, NOW(), NULL, NOW(), 'Worth every penny, unforgettable trip!', 5, 'BK010', 'TOUR010', 'USER010'),
('RV011', 1, NOW(), NULL, NOW(), 'Great value for money.', 4, 'BK011', 'TOUR011', 'USER001'),
('RV012', 1, NOW(), NULL, NOW(), 'Too crowded, not enjoyable.', 2, 'BK012', 'TOUR012', 'USER002'),
('RV013', 1, NOW(), NULL, NOW(), 'Excellent service from start to finish.', 5, 'BK013', 'TOUR013', 'USER003'),
('RV014', 1, NOW(), NULL, NOW(), 'Would book again!', 5, 'BK014', 'TOUR014', 'USER004'),
('RV015', 1, NOW(), NULL, NOW(), 'Tour schedule was too rushed.', 3, 'BK015', 'TOUR015', 'USER005'),
('RV016', 1, NOW(), NULL, NOW(), 'Loved the local culture experience.', 5, 'BK016', 'TOUR016', 'USER006'),
('RV017', 1, NOW(), NULL, NOW(), 'Boring itinerary, needs more activities.', 2, 'BK017', 'TOUR017', 'USER007'),
('RV018', 1, NOW(), NULL, NOW(), 'Amazing hospitality from the team.', 5, 'BK018', 'TOUR018', 'USER008'),
('RV019', 1, NOW(), NULL, NOW(), 'The weather was bad, but still a fun trip.', 4, 'BK019', 'TOUR019', 'USER009'),
('RV020', 1, NOW(), NULL, NOW(), 'Once in a lifetime experience!', 5, 'BK020', 'TOUR020', 'USER010');

INSERT INTO wishlist (id, tour_id, user_id)
VALUES
('WL001', 'TOUR001', 'USER001'),
('WL002', 'TOUR003', 'USER002'),
('WL003', 'TOUR005', 'USER003'),
('WL004', 'TOUR007', 'USER004'),
('WL005', 'TOUR009', 'USER005'),
('WL006', 'TOUR011', 'USER006'),
('WL007', 'TOUR013', 'USER007'),
('WL008', 'TOUR015', 'USER008'),
('WL009', 'TOUR017', 'USER009'),
('WL010', 'TOUR019', 'USER010'),
('WL011', 'TOUR002', 'USER001'),
('WL012', 'TOUR004', 'USER002'),
('WL013', 'TOUR006', 'USER003'),
('WL014', 'TOUR008', 'USER004'),
('WL015', 'TOUR010', 'USER005'),
('WL016', 'TOUR012', 'USER006'),
('WL017', 'TOUR014', 'USER007'),
('WL018', 'TOUR016', 'USER008'),
('WL019', 'TOUR018', 'USER009'),
('WL020', 'TOUR020', 'USER010');

--insert into otp (id, email, expires_at, otp_code)
--values ('1', 'user@example.com', DATEADD(HOUR, 1, NOW()), '123456');

SELECT 
    year(b.created_at) AS booking_year,
    SUM(p.total_amount) AS total_revenue
FROM booking b
JOIN payment p ON b.payment_id = p.id
GROUP BY year(b.created_at)
ORDER BY booking_year;

--UPDATE booking 
--SET created_at = DATEADD(DAY, -ABS(CHECKSUM(NEWID()) % 1095), NOW()) 
--WHERE id BETWEEN 'BK001' AND 'BK020';

--delete from booking

--select
--	td.id,
--	t.id,
--	t.title,
--	b.total_amount,
--	b.total_number,
--	FORMAT(b.created_at, 'yyyy-MM-dd') as created_at,
--	b.user_id
--from tour_details td
--left join booking b on td.id = b.tour_detail_id
--left join tour t on t.id = td.tour_id
--where (td.tour_id = '' OR '' = '')
--	OR (b.created_at IS NULL
--	OR '' = ''
--	OR (b.created_at BETWEEN '' AND ''))

--select 
--b.id,
--t.id,
--td.id,
--us.username,
--b.booking_status,
--b.total_amount,
--b.total_number,
--b.payment_method,
--FORMAT(b.created_at, 'yyyy-MM-dd') as created_at
--from booking b
--join users us on us.id = b.user_id
--join tour_details td on td.id = b.tour_detail_id
--join tour t on t.id = td.tour_id
--where (t.id = '' OR '' IS NULL OR '' = '')
--	or (LOWER(us.username) LIKE LOWER('') OR '' IS NULL OR '' = '');

select * 
from users us
join review rv on rv.u = us.