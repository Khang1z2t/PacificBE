package com.pacific.pacificbe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Entity
@Table(name = "TourTypes")
public class TourType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tourTypeId;

    @Column(nullable = false, length = 100)
    private String tourTypeName;
}

