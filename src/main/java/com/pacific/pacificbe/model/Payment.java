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
@Table(name = "payment")
public class Payment extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(name = "amount", precision = 19, scale = 4)
    private BigDecimal amount;

    @Size(max = 255)
    @Nationalized
    @Column(name = "payment_method")
    private String paymentMethod;

    @Size(max = 255)
    @Nationalized
    @Column(name = "payment_status")
    private String paymentStatus;

    @Size(max = 255)
    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "payment_date")
    private Instant paymentDate;

    @Nationalized
    @Lob
    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "payment")
    private Set<Invoice> invoices = new LinkedHashSet<>();

    @OneToMany(mappedBy = "payment")
    private Set<Voucher> vouchers = new LinkedHashSet<>();

}