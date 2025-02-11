package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "guide")
public class Guide {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "first_name")
    private String firstName;

    @Size(max = 255)
    @Nationalized
    @Column(name = "last_name")
    private String lastName;

    @Size(max = 20)
    @Nationalized
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 100)
    @Nationalized
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "experience_year")
    private Double experienceYear;

}