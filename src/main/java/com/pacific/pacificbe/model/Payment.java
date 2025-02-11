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
//@Table(name = "Payments")
//public class Payment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer paymentId;
//
//    @ManyToOne
//    @JoinColumn(name = "bookingId", nullable = false)
//    private Booking booking;
//
//    @Column(nullable = false)
//    private java.sql.Date paymentDate;
//
//    @Column(nullable = false)
//    private Double amount;
//
//    @Column(nullable = false, length = 50)
//    private String paymentMethod;
//
//    @ManyToOne
//    @JoinColumn(name = "statusId", nullable = false)
//    private Status status;
//}
//
