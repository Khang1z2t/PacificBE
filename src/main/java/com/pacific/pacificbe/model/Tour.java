package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tour")
public class Tour extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ColumnDefault("1")
    @Column(name = "available")
    private Boolean available;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "price_adults", precision = 10, scale = 2)
    private BigDecimal priceAdults;

    @Column(name = "price_children", precision = 10, scale = 2)
    private BigDecimal priceChildren;

    @Column(name = "quantity_max")
    private Integer quantityMax;

    @Column(name = "rating_avg")
    private Double ratingAvg;

    @Size(max = 50)
    @Nationalized
    @Column(name = "status", length = 50)
    private String status;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "title", nullable = false)
    private String title;

    @Nationalized
    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id")
    private Guide guide;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @OneToMany(mappedBy = "tour")
    private Set<Image> images = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tour")
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tour")
    private Set<com.pacific.pacificbe.model.TourDetail> tourDetails = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tour")
    private Set<Wishlist> wishlists = new LinkedHashSet<>();


}