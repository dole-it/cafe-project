package com.cafe_be.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = true)
    private Double price;

    @Column(nullable = false)
    private String category;

    private String image;

    @Column(nullable = false)
    private Boolean available = true;
}
