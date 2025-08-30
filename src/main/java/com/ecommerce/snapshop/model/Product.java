package com.ecommerce.snapshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    private Long id;
    @NotNull
    @Size(min = 3, message = "Product name should be at least 3 characters")
    @Column(name="product_name")
    private String name;
    @NotNull
    @Size(min = 5, message = "Product description should be at least 5 characters")
    @Column(name="product_description")
    private String description;
    private int quantity;
    private double productPrice;
    private double discount;
    private double specialPrice;
    private String image;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="seller_id")
    private User user;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
    private List<CartItem> products;

}
