package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tour_image")
public class TourImage {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "create_at")
    private Instant createAt;

}