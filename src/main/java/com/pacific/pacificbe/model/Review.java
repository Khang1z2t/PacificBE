package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

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

    @Nationalized
    @Lob
    @Column(name = "comment")
    private String comment;

    @Column(name = "rating", precision = 2, scale = 1)
    private BigDecimal rating;

    @Size(max = 50)
    @Nationalized
    @ColumnDefault("'active'")
    @Column(name = "status", length = 50)
    private String status;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "price_rating", precision = 2, scale = 1)
    private BigDecimal priceRating;

    @Column(name = "service_rating", precision = 2, scale = 1)
    private BigDecimal serviceRating;

    @Column(name = "facility_rating", precision = 2, scale = 1)
    private BigDecimal facilityRating;

    @Column(name = "food_rating", precision = 2, scale = 1)
    private BigDecimal foodRating;

    @Column(name = "accommodation_rating", precision = 2, scale = 1)
    private BigDecimal accommodationRating;

}