package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Size(max = 255)
    @Nationalized
    @Column(name = "hotel_type")
    private String hotelType;

    @Column(name = "rating")
    private Double rating;

    @Nationalized
    @Lob
    @Column(name = "description")
    private String description;

}