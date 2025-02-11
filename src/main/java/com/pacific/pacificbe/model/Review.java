package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Column(name = "rating")
    private Double rating;

    @Size(max = 255)
    @Column(name = "content")
    private String content;

    @Size(max = 255)
    @Column(name = "status")
    private String status;

    @Column(name = "review_date")
    private Instant reviewDate;

}