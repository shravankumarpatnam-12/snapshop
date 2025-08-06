package com.ecommerce.snapshop.controller;

import com.ecommerce.snapshop.config.AppConstants;
import com.ecommerce.snapshop.payload.CategoryDTORequest;
import com.ecommerce.snapshop.payload.CategoryDTOResponse;
import com.ecommerce.snapshop.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    //@RequestMapping(value = "/api/public/categories", method = RequestMethod.GET)
    public ResponseEntity<CategoryDTOResponse> getCategoryList(
            @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
            @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE) int pageSize,
            @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_ORDER) String sortOrder) {
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTORequest> createCategory(@Valid @RequestBody CategoryDTORequest categoryDTORequest) {
        return new ResponseEntity<>(categoryService.createCategory(categoryDTORequest),HttpStatus.CREATED);
    }

    @DeleteMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTORequest> deleteCategory(@PathVariable Long categoryId) {
        return new ResponseEntity<CategoryDTORequest>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
    }
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTORequest> updateCategory(@RequestBody CategoryDTORequest categoryDTORequest, @PathVariable Long categoryId) {
            return new ResponseEntity<>(categoryService.updateCategory(categoryDTORequest,categoryId), HttpStatus.OK);
    }
}
