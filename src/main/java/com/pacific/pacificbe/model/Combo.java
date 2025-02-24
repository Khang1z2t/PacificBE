package com.pacific.pacificbe.model;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "combo")
public class Combo {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "price_combo", precision = 10, scale = 2)
    private BigDecimal priceCombo;

    @OneToMany(mappedBy = "combo")
    private Set<Booking> bookings = new LinkedHashSet<>();
    
    @OneToMany(mappedBy = "combo")
    private Set<TourDetail> tourDetail = new LinkedHashSet<>();

    @Size(max = 100)
    @Nationalized
    @Column(name = "combo_type", length = 100)
    private String comboType;

//  @NotNull
//  @ManyToOne(fetch = FetchType.LAZY, optional = false)
//  @JoinColumn(name = "hotel_id", nullable = false)
//  private Hotel hotel;
//
//  @NotNull
//  @ManyToOne(fetch = FetchType.LAZY, optional = false)
//  @JoinColumn(name = "transport_id", nullable = false)
//  private Transport transport;

//  @NotNull
//  @ManyToOne(fetch = FetchType.LAZY, optional = false)
//  @JoinColumn(name = "tour_details_id", nullable = false)
//  private TourDetail tourDetails;
    
}