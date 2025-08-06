package com.ecommerce.snapshop.service;

import com.ecommerce.snapshop.payload.CategoryDTORequest;
import com.ecommerce.snapshop.payload.ProductDTORequest;
import com.ecommerce.snapshop.payload.ProductDTOResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface ProductService {
    ProductDTORequest addProduct(ProductDTORequest productDTORequest, Long categoryId);
    ProductDTOResponse getAllProduct(int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductDTORequest updateProductService(ProductDTORequest productDTORequest, Long productId);

    ProductDTORequest deleteProductService(Long productId);

    ProductDTORequest updateProductImageService(Long productId, MultipartFile image) throws IOException;

    ProductDTOResponse getProductById(Long categoryId, int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductDTOResponse getProductBykeyWord(String keyword, int pageNumber, int pageSize, String sortBy, String sortOrder);
}
