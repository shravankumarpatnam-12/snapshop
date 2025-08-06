package com.ecommerce.snapshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTORequest {
    private Long productId;
    private String productName;
    private String productDescription;
    private double productPrice;
    private double discount;
    private int quantity;
    private double specialPrice;
    private String image;
}
