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
@Table(name = "BookingVouchers")
public class BookingVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingVoucherId;

    @ManyToOne
    @JoinColumn(name = "bookingId", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "voucherId", nullable = false)
    private Voucher voucher;

    @Column(nullable = false)
    private java.sql.Date appliedDate;

    @Column(nullable = false)
    private Double discountAmount;
}
