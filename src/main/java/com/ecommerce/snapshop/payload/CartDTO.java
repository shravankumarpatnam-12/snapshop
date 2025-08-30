package com.ecommerce.snapshop.payload;

import com.ecommerce.snapshop.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDTO {

    private Long cartId;
    private Double totalPrice=0.0;
    private List<ProductDTORequest> productDTORequestList;
}
