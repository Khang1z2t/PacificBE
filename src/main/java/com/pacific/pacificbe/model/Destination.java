package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import com.fasterxml.jackson.annotation.JsonIgnore;


import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "destination")
public class Destination {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "city", nullable = false)
    private String city;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "country", nullable = false)
    private String country;

    @Size(max = 500)
    @NotNull
    @Nationalized
    @Column(name = "full_address", nullable = false, length = 500)
    private String fullAddress;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Nationalized
    @Column(name = "region")
    private String region;

    @OneToMany(mappedBy = "destination")
    @JsonIgnore
    private Set<Tour> tours = new LinkedHashSet<>();

}