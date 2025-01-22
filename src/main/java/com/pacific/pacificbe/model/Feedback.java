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
@Table(name = "Feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer feedbackId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "tourId", nullable = false)
    private Tour tour;

    @Column(nullable = false)
    private Integer rating;

    private String comment;

    @Column(nullable = false)
    private java.sql.Date feedbackDate;

    @ManyToOne
    @JoinColumn(name = "statusId", nullable = false)
    private Status status;
}
