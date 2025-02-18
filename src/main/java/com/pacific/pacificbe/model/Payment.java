package com.pacific.pacificbe.model;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "total_amount", precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Size(max = 50)
    @Nationalized
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Size(max = 50)
    @Nationalized
    @Column(name = "transaction_id", length = 50)
    private String transactionId;

    @Size(max = 20)
    @Nationalized
    @ColumnDefault("'pending'")
    @Column(name = "status", length = 20)
    private String status;

    @Size(max = 500)
    @Nationalized
    @Column(name = "note", length = 500)
    private String note;

    @OneToMany(mappedBy = "payment")
    private Set<Booking> bookings = new LinkedHashSet<>();;

    @OneToMany(mappedBy = "payment")
    private Set<Voucher> vouchers = new LinkedHashSet<>();
    
    @OneToOne(mappedBy = "payment")
    private Invoice invoice;
    
//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

}