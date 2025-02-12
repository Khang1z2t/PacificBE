package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "review")
public class Review extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private com.pacific.pacificbe.model.User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private com.pacific.pacificbe.model.Tour tour;

    @Column(name = "rating")
    private Double rating;

    @Nationalized
    @Lob
    @Column(name = "content")
    private String content;

    @Size(max = 255)
    @Nationalized
    @Column(name = "status")
    private String status;

    @Column(name = "review_date")
    private Instant reviewDate;

    @Column(name = "column_name")
    private Integer columnName;

}