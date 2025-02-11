package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "itinerary")
public class Itinerary extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 255)
    @Column(name = "tour_id")
    private String tourId;

    @Column(name = "day_number")
    private Integer dayNumber;

    @Size(max = 255)
    @Nationalized
    @Column(name = "title")
    private String title;

    @Size(max = 255)
    @Nationalized
    @Column(name = "description")
    private String description;

    @Size(max = 255)
    @Nationalized
    @Column(name = "activities")
    private String activities;

    @Size(max = 255)
    @Nationalized
    @Column(name = "notes")
    private String notes;

}