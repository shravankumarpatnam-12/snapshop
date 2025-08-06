package com.ecommerce.snapshop.service;

import com.ecommerce.snapshop.model.Category;
import com.ecommerce.snapshop.payload.CategoryDTORequest;
import com.ecommerce.snapshop.payload.CategoryDTOResponse;

import java.util.List;

public interface CategoryService {
    CategoryDTOResponse getAllCategories(int pageNumber, int pageSize,String sortBy, String sortOrder);
    CategoryDTORequest createCategory(CategoryDTORequest categoryDTORequest);
    CategoryDTORequest deleteCategory(Long categoryId);
    CategoryDTORequest updateCategory(CategoryDTORequest categoryDTORequest, Long categoryId);

}
