package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "system_wallet")
public class SystemWallet {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "balance", nullable = false, precision = 18, scale = 2)
    private BigDecimal balance;

    @NotNull
    @ColumnDefault("getdate()")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @ColumnDefault("getdate()")
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Nationalized
    @Lob
    @Column(name = "description")
    private String description;

}