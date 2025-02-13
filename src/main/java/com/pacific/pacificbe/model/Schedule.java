package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String  id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    // Liên kết đến Tour (giả sử mỗi lịch trình thuộc về 1 tour)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;


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