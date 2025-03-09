package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "invoice")
public class Invoice {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @ColumnDefault("0")
    @Column(name = "discount", precision = 5, scale = 2)
    private BigDecimal discount;

    @Size(max = 500)
    @Nationalized
    @Column(name = "note", length = 500)
    private String note;

    @Size(max = 50)
    @Nationalized
    @Column(name = "payment_status", length = 50)
    private String paymentStatus;

    @Column(name = "subtotal", precision = 18, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "total_amount", precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @ColumnDefault("10")
    @Column(name = "vat", precision = 5, scale = 2)
    private BigDecimal vat;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

}