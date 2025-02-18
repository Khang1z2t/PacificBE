package com.pacific.pacificbe.model;

import java.math.BigDecimal;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invoice")
public class Invoice {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "subtotal", precision = 18, scale = 2)
    private BigDecimal subtotal;

    @ColumnDefault("0")
    @Column(name = "discount", precision = 5, scale = 2)
    private BigDecimal discount;

    @ColumnDefault("10")
    @Column(name = "VAT", precision = 5, scale = 2)
    private BigDecimal vat;

    @Column(name = "total_amount", precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Size(max = 50)
    @Nationalized
    @Column(name = "payment_status", length = 50)
    private String paymentStatus;

    @Size(max = 500)
    @Nationalized
    @Column(name = "note", length = 500)
    private String note;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "tour_id", nullable = false)
//    private Tour tour;

//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "booking_id", nullable = false)
//    private Booking booking;
}