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
    private String id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "code_voucher", nullable = false)
    private String codeVoucher;

    @Column(name = "discount", precision = 5, scale = 2)
    private BigDecimal discount;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Size(max = 255)
    @Nationalized
    @Column(name = "name_voucher")
    private String nameVoucher;

    @Column(name = "quantity")
    private Integer quantity;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Size(max = 50)
    @Nationalized
    @ColumnDefault("'pending'")
    @Column(name = "status", length = 50)
    private String status;

    @OneToMany(mappedBy = "voucher")
    private Set<Booking> bookings = new LinkedHashSet<>();

}