create table category
(
    id     varchar(255)  not null
        primary key,
    status nvarchar(50),
    title  nvarchar(255) not null,
    type   nvarchar(50)
)
    go

create table combo
(
    id          varchar(255) not null
        primary key,
    price_combo numeric(10, 2)
)
    go

create table destination
(
    id           varchar(255)  not null
        primary key,
    city         nvarchar(255) not null,
    country      nvarchar(255) not null,
    full_address nvarchar(500) not null,
    name         nvarchar(255) not null,
    region       nvarchar(255)
)
    go

create table guide
(
    id               varchar(255)  not null
        primary key,
    address          nvarchar(255) not null,
    email            nvarchar(100) not null,
    experience_years int,
    first_name       nvarchar(50)  not null,
    last_name        nvarchar(50)  not null,
    phone            nvarchar(20)  not null
)
    go

create table hotel
(
    id         varchar(255) not null
        primary key,
    name       nvarchar(255),
    price      numeric(10, 2),
    type_hotel nvarchar(50)
)
    go

create table otp
(
    id         varchar(255) not null
        constraint OTP_pk
            primary key,
    email      varchar(255),
    otp_code   varchar(10),
    expires_at datetimeoffset(6)
)
    go

create table payment
(
    id             varchar(255) not null
        primary key,
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
(
    id             varchar(255) not null
        primary key,
    discount       numeric(5, 2) default 0,
    note           nvarchar(500),
    payment_status nvarchar(50),
    subtotal       numeric(18, 2),
    total_amount   numeric(18, 2),
    vat            numeric(5, 2) default 10,
    payment_id     varchar(255)
        constraint FKbaxa82hce5x7dqj0sotnc1cxf
            references payment
)
    go

create unique index UK5vvlr4mmb6jbwiu4dyqwevd0d
    on invoice (payment_id)
    where [payment_id] IS NOT NULL
go

create table promotion
(
    id             varchar(255)  not null
        primary key,
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
    duration       int,
    price_adults   numeric(10, 2),
    price_children numeric(10, 2),
    quantity_max   int,
    rating_avg     float,
    status         nvarchar(50),
    title          nvarchar(255) not null,
    category_id    varchar(255)
        constraint FK77r62epm647fkrvbv9a7824dx
            references category,
    guide_id       varchar(255)
        constraint FKbhtkr75hypdnlymbgcommv13j
            references guide,
    promotion_id   varchar(255)
        constraint FKebpp50j3f0ycjcjacu4qvavm6
            references promotion,
    description    nvarchar(max)
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
    id            varchar(255)  not null
        primary key,
    duration      int           not null,
    end_date      date          not null,
    itinerary     nvarchar(max) not null,
    start_date    date          not null,
    combo_id      varchar(255)  not null
        constraint FKjekgh9qlrlh9tnrbchetsa90r
            references combo
            on delete cascade,
    destinaton_id varchar(255)  not null
        constraint FK63ufq97rptrjg03mcq81f63pa
            references destination
            on delete cascade,
    hotel_id      varchar(255)  not null
        constraint FKkfwi1rkf52lro3ly2accpvhh6
            references hotel
            on delete cascade,
    tour_id       varchar(255)  not null
        constraint FK27g89or55p5ovep89frmbyva2
            references tour
            on delete cascade,
    transport_id  varchar(255)  not null
        constraint FKdehvthno0rf0ulsi7k1rtagkc
            references transport
            on delete cascade,
    active        bit,
    created_at    datetime2(6),
    delete_at     datetime2(6),
    updated_at    datetime2(6),
    quantity      int           not null,
    status        varchar(50)
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
    first_name     nvarchar(50),
    gender         nvarchar(50),
    last_name      nvarchar(50),
    password       nvarchar(255) not null,
    phone          nvarchar(20),
    role           nvarchar(20),
    status         nvarchar(50)   default 'active',
    username       nvarchar(50)  not null,
    email_verified bit,
    phone_verified bit
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
        constraint booking_tour_details_id_fk
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

