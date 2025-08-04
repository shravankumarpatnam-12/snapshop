package com.ecommerce.snapshop.controller;

import com.ecommerce.snapshop.model.Category;
import com.ecommerce.snapshop.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    //@RequestMapping(value = "/api/public/categories", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getCategoryList() {
        return new ResponseEntity<>(categoryService.getAllCategories(),HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>(category,HttpStatus.CREATED);
    }

    @DeleteMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
//        try{
//             return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
//        }catch (ResponseStatusException e){
//            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
//        }
    }
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Long categoryId) {
        try{
            categoryService.updateCategory(category,categoryId);
            return new ResponseEntity<>(category.getCategoryName() +" update successfully", HttpStatus.OK);
        }catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
