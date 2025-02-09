//package com.pacific.pacificbe.model;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Lob;
//import jakarta.persistence.Table;
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
//@Table(name = "Roles")
//public class Role {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer roleId;
//
//    @Column(nullable = false, length = 100)
//    private String roleName;
//
//    @Lob
//    private String description;
//}
