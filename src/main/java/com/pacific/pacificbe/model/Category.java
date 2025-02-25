package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    private String id;

    private String status;

    @Column(nullable = false)
    private String title;

    private String type;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<Tour> tours;
}
