package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Entity
@Table(name = "Schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scheduleId;

    @ManyToOne
    @JoinColumn(name = "tourId", nullable = false)
    private Tour tour;

    @Column(nullable = false)
    private Integer dayNumber;

    @Column(nullable = false, length = 5)
    private String startTime;

    @Column(nullable = false, length = 5)
    private String endTime;

    @Column(nullable = false, length = 255)
    private String activity;

    private String description;

    @ManyToOne
    @JoinColumn(name = "statusId", nullable = false)
    private Status status;
}
