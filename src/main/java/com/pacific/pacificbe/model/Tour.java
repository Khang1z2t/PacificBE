package com.pacific.pacificbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tours")
public class Tour extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "title")
    private String title;

    @Size(max = 255)
    @Nationalized
    @Column(name = "description")
    private String description;

    @Size(max = 255)
    @Column(name = "theme_url")
    private String themeUrl;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "base_price", precision = 19, scale = 4)
    private BigDecimal basePrice;

    @Column(name = "childrent_price", precision = 19, scale = 4)
    private BigDecimal childrentPrice;

    @Size(max = 255)
    @Nationalized
    @Column(name = "duration")
    private String duration;

    @Size(max = 255)
    @Nationalized
    @Column(name = "destination")
    private String destination;

    @Size(max = 255)
    @Nationalized
    @Column(name = "meeting_point")
    private String meetingPoint;

    @Column(name = "status")
    private Instant status;

    @Size(max = 255)
    @Column(name = "guide_id")
    private String guideId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cate_id")
    private Category cate;

    @OneToMany(mappedBy = "tour")
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tour", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<TourImage> tourImages = new LinkedHashSet<>();

}