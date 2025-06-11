package org.practice._3d_prin_shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    @Column
    private int stock;

    @Column
    private String image_url;

    @Column
    private LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
