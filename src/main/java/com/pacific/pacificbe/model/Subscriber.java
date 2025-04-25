package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "subscribers")
@EntityListeners(AuditingEntityListener.class)
public class Subscriber {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 200)
    @Column(name = "email", length = 200)
    private String email;

    @Size(max = 255)
    @Nationalized
    @Column(name = "name")
    private String name;

    @Column(name = "subscribed_at", updatable = false)
    @CreatedDate
    private LocalDateTime subscribedAt;

    @Column(name = "active")
    private Boolean active;

    @Size(max = 255)
    @Column(name = "unsubscribe_token")
    private String unsubscribeToken;

}