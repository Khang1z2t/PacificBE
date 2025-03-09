package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "address")
    private String address;

    @Size(max = 255)
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "birthday")
    private LocalDate birthday;

    @ColumnDefault("0")
    @Column(name = "deposit", precision = 18, scale = 2)
    private BigDecimal deposit;

    @Size(max = 100)
    @Nationalized
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Size(max = 50)
    @Nationalized
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Nationalized
    @Column(name = "gender", length = 50)
    private String gender;

    @Size(max = 50)
    @Nationalized
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 20)
    @Nationalized
    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "phone_verified")
    private Boolean phoneVerified;

    @Size(max = 20)
    @Nationalized
    @Column(name = "role", length = 20)
    private String role;

    @Size(max = 50)
    @Nationalized
    @ColumnDefault("'active'")
    @Column(name = "status", length = 50)
    private String status;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @OneToMany(mappedBy = "user")
    private Set<Blog> blogs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Booking> bookings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Support> supports = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<com.pacific.pacificbe.model.Wishlist> wishlists = new LinkedHashSet<>();

}