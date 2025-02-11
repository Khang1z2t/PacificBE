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
//@Table(name = "Services")
//public class Service {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer serviceId;
//
//    @Column(nullable = false, length = 255)
//    private String serviceName;
//
//    @Column(nullable = false)
//    private Double price;
//
//    @Lob
//    private String description;
//
//    @ManyToOne
//    @JoinColumn(name = "statusId", nullable = false)
//    private Status status;
//}
