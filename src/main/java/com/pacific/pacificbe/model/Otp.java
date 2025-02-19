package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "otp")
public class Otp {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @Size(max = 10)
    @Column(name = "otp_code", length = 10)
    private String otpCode;

    @Column(name = "expires_at")
    private Instant expiresAt;

}