package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Entity
@Table(name = "Tours")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tourId;

    @Column(nullable = false, length = 255)
    private String tourName;

    @Column(nullable = false, length = 255)
    private String destination;

    @Lob
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer capacity;

    private String imageUrl;

    @Column(nullable = false)
    private java.sql.Date startDate;

    @Column(nullable = false)
    private java.sql.Date endDate;

    @ManyToOne
    @JoinColumn(name = "statusId", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "regionId", nullable = false)
    private Region region;

    @ManyToOne
    @JoinColumn(name = "tourTypeId", nullable = false)
    private TourType tourType;
}
