package com.pacific.pacificbe.model;

import java.math.BigDecimal;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "tour_id")
//    private Tour tour;

    @Size(max = 50)
    @Nationalized
    @Column(name = "type_hotel", length = 50)
    private String typeHotel;

    @Size(max = 255)
    @Nationalized
    @Column(name = "name")
    private String name;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

//    @OneToMany(mappedBy = "hotel")
//    private Set<Combo> combos = new LinkedHashSet<>();

}