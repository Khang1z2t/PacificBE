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

    @Column(name = "adult_num")
    private Integer adult_nums;

    @Column(name = "children_num")
    private Integer childrennums;

//    @ColumnDefault("[adult_num]+[children_num]")
    @Column(name = "total_number")
    private Integer totalNumber;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Size(max = 50)
    @Nationalized
    @ColumnDefault("'pending'")
    @Column(name = "booking_status", length = 50)
    private String bookingStatus;

    @Size(max = 50)
    @Nationalized
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Size(max = 255)
    @Nationalized
    @Column(name = "special_requests")
    private String specialRequests;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "combo_id")
    private Combo combo;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToMany(mappedBy = "booking")
    private Set<Review> reviews = new LinkedHashSet<>();


//    @OneToMany(mappedBy = "booking")
//    private Set<Voucher> vouchers = new LinkedHashSet<>();

//    @OneToMany(mappedBy = "booking")
//    private Set<Invoice> invoices = new LinkedHashSet<>();
    
//  @OneToMany(mappedBy = "booking")
//  private Set<Promotion> promotions = new LinkedHashSet<>();
}