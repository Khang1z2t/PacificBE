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
//@Table(name = "TourGuides")
//public class TourGuide {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer tourGuideId;
//
//    @Column(nullable = false, length = 255)
//    private String fullName;
//
//    @Column(nullable = false, unique = true, length = 255)
//    private String email;
//
//    @Column(nullable = false, length = 20)
//    private String phoneNumber;
//
//    @Column(nullable = false, length = 20)
//    private String experienceYears;
//
//    @Lob
//    private String Description;
//
//    @ManyToOne
//    @JoinColumn(name = "statusId", nullable = false)
//    private Status status;
//}
