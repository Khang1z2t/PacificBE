package com.pacific.pacificbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pacific.pacificbe.utils.enums.BlogStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "blogs", indexes = {
        @Index(name = "idx_blog_slug", columnList = "slug"),
        @Index(name = "idx_blog_status", columnList = "status")
})
public class Blog extends BaseEntity {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Nationalized
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private BlogStatus status;

    @Size(max = 255)
    @Column(name = "slug", unique = true)
    private String slug;

    @Size(max = 255)
    @Nationalized
    @Column(name = "meta_title")
    private String metaTitle;

    @Size(max = 500)
    @Nationalized
    @Column(name = "meta_description")
    private String metaDescription;

    @Column(name = "view_count", columnDefinition = "INT DEFAULT 0")
    private int viewCount;

    @Column(name = "like_count", columnDefinition = "INT DEFAULT 0")
    private int likeCount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private BlogCategory category;

    @ManyToMany
    @JoinTable(
            name = "blog_tour",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "tour_id")
    )
    private List<Tour> tours = new ArrayList<>();

}
