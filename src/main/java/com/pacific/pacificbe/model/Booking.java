package com.pacific.pacificbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "booking")
@EntityListeners(AuditingEntityListener.class)
public class Booking extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "booker_full_name")
    private String bookerFullName;

    @Size(max = 20)
    @Column(name = "booker_phone_number", length = 20)
    private String bookerPhoneNumber;

    @Size(max = 255)
//    @Email(message = "Email should be valid")
    @Column(name = "booker_email")
    private String bookerEmail;

    @Size(max = 500)
    @Nationalized
    @Column(name = "booker_address", length = 500)
    private String bookerAddress;

    @Column(name = "adult_num")
    private Integer adultNum;

    @Column(name = "children_num")
    private Integer childrenNum;

    @Size(max = 50)
    @Nationalized
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Size(max = 255)
    @Nationalized
    @Column(name = "special_requests")
    private String specialRequests;

    @Nationalized
    @Lob
    @Column(name = "notes")
    private String notes;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "total_number")
    private Integer totalNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tour_detail_id", nullable = false)
    private TourDetail tourDetail;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @OneToMany(mappedBy = "booking")
    private Set<Invoice> invoices = new LinkedHashSet<>();

    @OneToOne(mappedBy = "booking")
    private Review review;

    @OneToMany(mappedBy = "booking")
    private List<BookingDetail> bookingDetails = new ArrayList<>();

    @Size(max = 225)
    @Column(name = "booking_no", length = 225)
    private String bookingNo;

    @Size(max = 100)
    @Column(name = "status", length = 100)
    private String status;

}