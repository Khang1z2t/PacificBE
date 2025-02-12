package com.pacific.pacificbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cate_id")
    private Category cate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(
            name = "tour_guide",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "guide_id")
    )
    private Set<Guide> guides = new LinkedHashSet<>();

    @Size(max = 255)
    @Nationalized
    @Column(name = "title")
    private String title;

    @Size(max = 255)
    @Nationalized
    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "base_price", precision = 19, scale = 4)
    private BigDecimal basePrice;

    @Column(name = "children_price", precision = 19, scale = 4)
    private BigDecimal childrenPrice;

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
    @Column(name = "metting_point")
    private String mettingPoint;

    @Lob
    @Column(name = "theme_url")
    private String themeUrl;

    @Size(max = 255)
    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "tour")
    private Set<Booking> bookings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tour")
    private Set<Hotel> hotels = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tour")
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tour")
    private Set<Schedule> schedules = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tour")
    private Set<TourImage> tourImages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tour")
    private Set<Transport> transports = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tour")
    private Set<Wishlist> wishlists = new LinkedHashSet<>();

}