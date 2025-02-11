package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "transport")
public class Transport {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 255)
    @Column(name = "booking_id")
    private String bookingId;

    @Size(max = 255)
    @Nationalized
    @Column(name = "type")
    private String type;

    @Size(max = 255)
    @Nationalized
    @Column(name = "destination")
    private String destination;

    @Size(max = 255)
    @Nationalized
    @Column(name = "package")
    private String packageField;

    @Size(max = 255)
    @Nationalized
    @Column(name = "notes")
    private String notes;

}