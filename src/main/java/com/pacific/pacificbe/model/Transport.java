package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transport")
@SQLDelete(sql = "UPDATE transport SET delete_at = NOW() WHERE id = ?")
@Where(clause = "delete_at IS NULL")
public class Transport extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost;

    @Size(max = 255)
    @Nationalized
    @Column(name = "imageurl")
    private String imageURL;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "type_transport", nullable = false, length = 50)
    private String typeTransport;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "delete_at")
    private LocalDateTime deleteAt;

    @OneToMany(mappedBy = "transport", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TourDetail> tourDetails = new LinkedHashSet<>();

    @OneToMany(mappedBy = "transport", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Image> images = new LinkedHashSet<>();
}