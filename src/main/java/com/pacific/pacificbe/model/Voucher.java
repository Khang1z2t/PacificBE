//package com.pacific.pacificbe.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Data
//@Entity
//@Table(name = "Vouchers")
//public class Voucher {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer voucherId;
//
//    @Column(nullable = false, length = 50)
//    private String code;
//
//    @Column(nullable = false)
//    private Double discount;
//
//    @Column(nullable = false, length = 50)
//    private String discountType;
//
//    @Column(nullable = false)
//    private java.sql.Date startDate;
//
//    @Column(nullable = false)
//    private java.sql.Date endDate;
//
//    @Column(nullable = false)
//    private Double minSpend;
//
//    private Double maxDiscount;
//
//    @ManyToOne
//    @JoinColumn(name = "statusId", nullable = false)
//    private Status status;
//
//    @Lob
//    private String description;
//}
