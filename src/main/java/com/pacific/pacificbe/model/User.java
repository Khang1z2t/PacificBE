package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class User extends BaseEntity implements UserDetails {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 50)
    @Nationalized
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Nationalized
    @Column(name = "last_name", length = 50)
    private String lastName;

    @ColumnDefault("0")
    @Column(name = "deposit", precision = 18, scale = 2)
    private BigDecimal deposit;

    @Size(max = 50)
    @Nationalized
    @Column(name = "gender", length = 50)
    private String gender;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Size(max = 100)
    @Nationalized
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 20)
    @Nationalized
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 255)
    @Nationalized
    @Column(name = "address")
    private String address;

    @Size(max = 50)
    @Nationalized
    @ColumnDefault("'active'")
    @Column(name = "status", length = 50)
    private String status;

    @Size(max = 20)
    @Nationalized
    @Column(name = "role", length = 20)
    private String role;

    @Size(max = 255)
    @Column(name = "avatar_url")
    private String avatarUrl;

    @OneToMany(mappedBy = "user")
    private Set<Blog> blogs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Booking> bookings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Support> supports = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Wishlist> wishlists = new LinkedHashSet<>();

//  @OneToMany(mappedBy = "user")
//  private Set<Invoice> invoices = new LinkedHashSet<>();

//  @OneToMany(mappedBy = "user")
//  private Set<Payment> payments = new LinkedHashSet<>();

//  @OneToMany(mappedBy = "user")
//  private Set<Promotion> promotions = new LinkedHashSet<>();

//  @OneToMany(mappedBy = "user")
//  private Set<Voucher> vouchers = new LinkedHashSet<>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}