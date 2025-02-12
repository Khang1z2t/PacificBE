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
@Table(name = "voucher")
public class Voucher {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Size(max = 255)
    @Nationalized
    @Column(name = "voucher_name")
    private String voucherName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "date_issued")
    private Instant dateIssued;

    @Column(name = "due_date")
    private Instant dueDate;

}