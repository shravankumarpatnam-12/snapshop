package com.ecommerce.snapshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 3, message = "Product name should be at least 3 characters")
    private String name;
    @NotNull
    @Size(min = 5, message = "Product description should be at least 5 characters")
    private String description;
    private int quantity;
    private double productPrice;
    private double discount;
    private double specialPrice;
    private String image;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

}
