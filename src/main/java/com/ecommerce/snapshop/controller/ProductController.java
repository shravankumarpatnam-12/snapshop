package com.ecommerce.snapshop.controller;

import com.ecommerce.snapshop.config.AppConstants;
import com.ecommerce.snapshop.payload.ProductDTORequest;
import com.ecommerce.snapshop.payload.ProductDTOResponse;
import com.ecommerce.snapshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTORequest> addProduct(@Valid @RequestBody ProductDTORequest productDTORequest,
                                                        @PathVariable Long categoryId) {
        ProductDTORequest productDTOAddRequest= productService.addProduct(productDTORequest,categoryId);
        return new ResponseEntity<ProductDTORequest>(productDTOAddRequest,HttpStatus.CREATED);
    }
    @GetMapping("/public/products")
    public ResponseEntity<ProductDTOResponse> getAllProducts(
            @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
            @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE) int pageSize,
            @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_ORDER) String sortOrder
    ) {
        ProductDTOResponse productDTOResponse= productService.getAllProduct(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<ProductDTOResponse>(productDTOResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductDTOResponse> getProductById(@PathVariable Long categoryId,
                 @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
                 @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE) int pageSize,
                 @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
                 @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_ORDER) String sortOrder) {
        ProductDTOResponse productDTOResponse= productService.getProductById(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<ProductDTOResponse>(productDTOResponse, HttpStatus.OK);
    }

    @GetMapping("public/products/keyword/{keyword}")
    public ResponseEntity<ProductDTOResponse> getProductById(@PathVariable String keyword,
     @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
     @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE) int pageSize,
     @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
     @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_ORDER) String sortOrder) {
        ProductDTOResponse productDTOResponse= productService.getProductBykeyWord(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<ProductDTOResponse>(productDTOResponse, HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTORequest> updateProduct(@Valid @RequestBody ProductDTORequest productDTORequest,
                                                           @PathVariable Long productId) {
        ProductDTORequest productResFromService= productService.updateProductService(productDTORequest,productId);
        return new ResponseEntity<ProductDTORequest>(productResFromService,HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTORequest> deleteProduct(@PathVariable Long productId) {
        ProductDTORequest productResFromService= productService.deleteProductService(productId);
        return new ResponseEntity<ProductDTORequest>(productResFromService,HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTORequest> updateProductImage(@PathVariable Long productId,
                                                                @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTORequest productResFromService= productService.updateProductImageService(productId,image);
        return new ResponseEntity<ProductDTORequest>(productResFromService,HttpStatus.OK);
    }
}
