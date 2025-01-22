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
@Table(name = "TourGuideAssignments")
public class TourGuideAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assignmentId;

    @ManyToOne
    @JoinColumn(name = "tourId", nullable = false)
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "tourGuideId", nullable = false)
    private TourGuide tourGuide;

    @Column(nullable = false)
    private java.sql.Date assignmentDate;
}
