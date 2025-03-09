package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "guide")
public class Guide {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "address", nullable = false)
    private String address;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Size(max = 20)
    @NotNull
    @Nationalized
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @OneToMany(mappedBy = "guide")
    private Set<com.pacific.pacificbe.model.Tour> tours = new LinkedHashSet<>();

}