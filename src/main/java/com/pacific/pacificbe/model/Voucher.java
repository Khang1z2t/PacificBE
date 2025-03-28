package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "voucher")
public class Voucher extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "code_voucher", nullable = false)
    private String codeVoucher;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "quantity")
    private Integer quantity;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Size(max = 50)
    @Nationalized
    @ColumnDefault("'pending'")
    @Column(name = "status", length = 50)
    private String status;

    @OneToMany(mappedBy = "voucher")
    private Set<Booking> bookings = new LinkedHashSet<>();

    @Column(name = "discount_value", precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Size(max = 255)
    @Nationalized
    @Column(name = "title")
    private String title;

    @Column(name = "min_order_value", precision = 10, scale = 2)
    private BigDecimal minOrderValue;

    @Column(name = "user_limit")
    private Integer userLimit;

    @Size(max = 20)
    @Column(name = "apply_to", length = 20)
    private String applyTo;

    @Size(max = 225)
    @Column(name = "tour_id", length = 225)
    private String tourId;

    @Size(max = 225)
    @Column(name = "category_id", length = 225)
    private String categoryId;

}