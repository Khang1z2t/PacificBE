package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "blog_categories", indexes = {
        @Index(name = "idx_blog_category_slug", columnList = "slug")
})
public class BlogCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @NotNull
    @Size(max = 100)
    @Nationalized
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Size(max = 255)
    @Column(name = "slug", unique = true, nullable = false)
    private String slug;

    @Size(max = 255)
    @Column(name = "meta_title")
    private String metaTitle;

    @Size(max = 500)
    @Column(name = "meta_description")
    private String metaDescription;
}
