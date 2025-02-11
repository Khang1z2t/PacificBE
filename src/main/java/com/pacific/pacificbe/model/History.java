package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "history")
public class History {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Size(max = 255)
    @Column(name = "action_type")
    private String actionType;

    @Size(max = 255)
    @Column(name = "target_type")
    private String targetType;

    @Size(max = 255)
    @Column(name = "id_type")
    private String idType;

    @Size(max = 255)
    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "create_at")
    private Instant createAt;

}