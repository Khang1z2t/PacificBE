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

insert into blogs (id, active, created_at, delete_at, updated_at, content, status, title, user_id)
values ('1', 1, GETDATE(), NULL, GETDATE(), 'Sample blog content', 'draft', 'Sample Blog Title', '1');

insert into booking (id, active, created_at, delete_at, updated_at, adult_num, booking_status, children_num, payment_method, special_requests, total_amount, total_number, combo_id, payment_id, tour_detail_id, user_id)
values ('1', 1, GETDATE(), NULL, GETDATE(), 2, 'pending', 1, 'Credit Card', 'None', 100.00, 3, '1', '1', '1', '1');

insert into dbo.category (id, status, title, type)
values ('1', 'active', 'Adventure', 'Outdoor');

insert into combo (id, combo_type, price_combo)
values ('1', 'Family', 150.00);

insert into destination (id, city, country, full_address, name, region)
values ('1', 'Hanoi', 'Vietnam', '123 Street, Hanoi', 'Hanoi City', 'North');

insert into guide (id, address, email, experience_years, first_name, last_name, phone)
values ('1', '123 Street, Hanoi', 'guide@example.com', 5, 'John', 'Doe', '123456789');

insert into hotel (id, name, price, type_hotel)
values ('1', 'Sample Hotel', 200.00, 'Luxury');

insert into image (id, active, created_at, delete_at, updated_at, description, image_url, tour_id)
values ('1', 1, GETDATE(), NULL, GETDATE(), 'Sample Image', 'http://example.com/image.jpg', '1');

insert into invoice (id, discount, note, payment_status, subtotal, total_amount, vat, payment_id)
values ('1', 10.00, 'Sample Note', 'Paid', 100.00, 110.00, 10.00, '1');

insert into itinerary (id, active, created_at, delete_at, updated_at, day_detail, day_number, notes, tour_detail_id)
values ('1', 1, GETDATE(), NULL, GETDATE(), 'Day 1 details', 1, 'Sample notes', '1');

insert into otp (id, email, expires_at, otp_code)
values ('1', 'user@example.com', DATEADD(HOUR, 1, GETDATE()), '123456');

insert into payment (id, active, created_at, delete_at, updated_at, note, payment_method, status, total_amount, transaction_id)
values ('1', 1, GETDATE(), NULL, GETDATE(), 'Sample Payment', 'Credit Card', 'Completed', 100.00, 'TX123');

insert into promotion (id, active, created_at, delete_at, updated_at, discount, end_date, name_promotion, quantity, start_date, status)
values ('1', 1, GETDATE(), NULL, GETDATE(), 20.00, '2023-12-31', 'New Year Promo', 100, '2023-01-01', 'active');

insert into review (id, active, created_at, delete_at, updated_at, comment, rating, booking_id, tour_id, user_id)
values ('1', 1, GETDATE(), NULL, GETDATE(), 'Great tour!', 4.5, '1', '1', '1');

insert into support (id, active, created_at, delete_at, updated_at, message, status, subject, user_id)
values ('1', 1, GETDATE(), NULL, GETDATE(), 'Need help with booking', 'pending', 'Booking Issue', '1');

insert into tour (id, active, created_at, delete_at, updated_at, available, description, status, title, category_id, destination_id, guide_id, promotion_id)
values ('1', 1, GETDATE(), NULL, GETDATE(), 1, 'Sample Tour Description', 'active', 'Sample Tour', '1', '1', '1', '1');

insert into tour_details (id, active, created_at, delete_at, updated_at, duration, end_date, price_adults, price_children, quantity, rating_avg, start_date, status, combo_id, hotel_id, tour_id, transport_id)
values ('1', 1, GETDATE(), NULL, GETDATE(), 7, '2023-12-31', 500.00, 300.00, 20, 4.5, '2023-01-01', 'active', '1', '1', '1', '1');

insert into transport (id, active, created_at, delete_at, updated_at, name, price, type_transport)
values ('1', 1, GETDATE(), NULL, GETDATE(), 'Bus', 50.00, 'Land');

insert into users (id, active, created_at, delete_at, updated_at, address, avatar_url, birthday, deposit, email, email_verified, first_name, gender, last_name, password, phone, phone_verified, role, status, username)
values ('1', 1, GETDATE(), NULL, GETDATE(), '123 Street, Hanoi', 'http://example.com/avatar.jpg', '1990-01-01', 0.00, 'user@example.com', 1, 'John', 'Male', 'Doe', 'password', '123456789', 1, 'user', 'active', 'johndoe');

insert into voucher (id, active, created_at, delete_at, updated_at, code_voucher, discount, end_date, name_voucher, quantity, start_date, status, payment_id)
values ('1', 1, GETDATE(), NULL, GETDATE(), 'NEWYEAR2023', 15.00, '2023-12-31', 'New Year Voucher', 100, '2023-01-01', 'active', '1');

insert into wishlist (id, tour_id, user_id)
values ('1', '1', '1');