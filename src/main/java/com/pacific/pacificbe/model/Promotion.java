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
@Table(name = "promotions")
public class Promotion extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 225)
    @Nationalized
    @Column(name = "description", length = 225)
    private String description;

    @Column(name = "discount", precision = 19, scale = 4)
    private BigDecimal discount;

    @Column(name = "date_issued")
    private Instant dateIssued;

    @Column(name = "due_date")
    private Instant dueDate;

    @OneToMany(mappedBy = "promotion")
    private Set<Booking> bookings = new LinkedHashSet<>();

}