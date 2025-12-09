package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Nationalized
    @Column(name = "day_detail", columnDefinition = "TEXT")
    private String dayDetail;

    @Column(name = "day_number")
    private Integer dayNumber;

    @Nationalized
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Size(max = 255)
    @Nationalized
    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "tour_id")
    private Tour tour;

}