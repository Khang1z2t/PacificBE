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
@Table(name = "Bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "tourId", nullable = false)
    private Tour tour;

    @Column(nullable = false)
    private java.sql.Date bookingDate;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private Integer totalPersons;

    @ManyToOne
    @JoinColumn(name = "statusId", nullable = false)
    private Status status;
}
