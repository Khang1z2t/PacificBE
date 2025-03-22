package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "hotel")
public class Hotel {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "name")
    private String name;

    @Size(max = 255)
    @Nationalized
    @Column(name = "rating")
    private String rating;

    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ImageURL")
    private String ImageURL;

    @Size(max = 50)
    @Nationalized
    @Column(name = "type_hotel", length = 50)
    private String typeHotel;

    @OneToMany(mappedBy = "hotel")
    private Set<TourDetail> tourDetails = new LinkedHashSet<>();

}