package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "guide")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Guide {

    @Id
    private String id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String email;

    private Integer experience_years;

    @Column(nullable = false)
    private String first_name;

    @Column(nullable = false)
    private String last_name;

    @Column(nullable = false)
    private String phone;
}
