package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "booking")
public class Booking extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Column(name = "booking_date")
    private Instant bookingDate;

    @Column(name = "total_price", precision = 19, scale = 4)
    private BigDecimal totalPrice;

    @Size(max = 255)
    @Nationalized
    @Column(name = "status")
    private String status;

    @Size(max = 255)
    @Nationalized
    @Column(name = "cancel_reason")
    private String cancelReason;

    @Size(max = 255)
    @Nationalized
    @Column(name = "booking_status")
    private String bookingStatus;

    @Size(max = 255)
    @Nationalized
    @Column(name = "special_request")
    private String specialRequest;

    @Column(name = "adult_quantity")
    private Long adultQuantity;

    @Column(name = "children_quantity")
    private Long childrenQuantity;

    @OneToMany(mappedBy = "booking")
    private Set<Payment> payments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "booking")
    private Set<Review> reviews = new LinkedHashSet<>();

}