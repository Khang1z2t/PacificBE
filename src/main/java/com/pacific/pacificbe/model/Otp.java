package com.pacific.pacificbe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "otp")
public class Otp {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Size(max = 10)
    @Column(name = "otp_code", length = 10)
    private String otpCode;

}