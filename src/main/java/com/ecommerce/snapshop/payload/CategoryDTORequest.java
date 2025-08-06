package com.ecommerce.snapshop.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTORequest {
    private Long categoryId;
    private String categoryName;
}
