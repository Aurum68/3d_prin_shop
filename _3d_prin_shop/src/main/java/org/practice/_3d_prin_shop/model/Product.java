package org.practice._3d_prin_shop.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    @Column
    private int stock_quantity;

    @Column
    private String image;

    @Column
    private LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;
}
