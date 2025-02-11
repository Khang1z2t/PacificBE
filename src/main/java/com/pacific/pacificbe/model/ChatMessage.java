package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "chat_message")
public class ChatMessage extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Size(max = 255)
    @Column(name = "sender_id")
    private String senderId;

    @Size(max = 255)
    @Nationalized
    @Column(name = "message")
    private String message;

    @Column(name = "is_read")
    private Boolean isRead;

}