package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tour_details")
public class TourDetail {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @NotNull
    @Nationalized
    @Lob
    @Column(name = "itinerary", nullable = false)
    private String itinerary;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @OneToMany(mappedBy = "tourDetails")
    private Set<Combo> combos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tourDetails")
    private Set<Image> images = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tourDetails")
    private Set<Review> reviews = new LinkedHashSet<>();

}