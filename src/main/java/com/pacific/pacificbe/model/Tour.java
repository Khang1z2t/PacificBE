package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

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

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Nationalized
    @Lob
    @Column(name = "description")
    private String description;

    @Size(max = 50)
    @Nationalized
    @Column(name = "status", length = 50)
    private String status;

    @Size(max = 255)
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    private Destination destination;

    @OneToMany(mappedBy = "tour")
    private Set<Image> images = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tour")
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TourDetail> tourDetails = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tour")
    private Set<Wishlist> wishlists = new LinkedHashSet<>();

    @OneToMany
    private Set<Voucher> vouchers = new LinkedHashSet<>();

}