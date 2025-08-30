package com.ecommerce.snapshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private CartDTO cartDTO;
    private ProductDTORequest productDTO;
    private int quantity;
    private double productPrice;
    private double discount;

}
