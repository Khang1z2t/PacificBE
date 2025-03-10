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

@Getter
@Setter
@Entity
@Table(name = "support")
public class Support extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Nationalized
    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @Size(max = 50)
    @Nationalized
    @ColumnDefault("'pending'")
    @Column(name = "status", length = 50)
    private String status;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "subject", nullable = false)
    private String subject;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private com.pacific.pacificbe.model.User user;

}