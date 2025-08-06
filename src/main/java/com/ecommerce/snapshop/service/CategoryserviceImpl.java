package com.ecommerce.snapshop.service;

import com.ecommerce.snapshop.exceptions.APIException;
import com.ecommerce.snapshop.exceptions.ResourceNotFoundException;
import com.ecommerce.snapshop.model.Category;
import com.ecommerce.snapshop.payload.CategoryDTORequest;
import com.ecommerce.snapshop.payload.CategoryDTOResponse;
import com.ecommerce.snapshop.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryserviceImpl implements CategoryService {
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    @Override
    public CategoryDTOResponse getAllCategories(int pageNumber, int pageSize,String sortBy, String sortOrder) {
        Sort sortOrderBy = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize,sortOrderBy);
        Page<Category> pageCategories=categoryRepository.findAll(pageDetails);
        List<Category> categories = pageCategories.getContent();
        if(categories.isEmpty()){
            throw new APIException("No categories found");
        }
        List<CategoryDTORequest> categoriesDTORes = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTORequest.class))
                .toList();
        CategoryDTOResponse categoryDTOResponse = new CategoryDTOResponse();
        categoryDTOResponse.setCategories(categoriesDTORes);
        categoryDTOResponse.setPageNumber(pageDetails.getPageNumber());
        categoryDTOResponse.setPageSize(pageDetails.getPageSize());
        categoryDTOResponse.setTotalElements(pageCategories.getTotalElements());
        categoryDTOResponse.setTotalPages(pageCategories.getTotalPages());
        categoryDTOResponse.setLastPage(pageCategories.isLast());
        return categoryDTOResponse;
    }

    @Override
    public CategoryDTORequest createCategory(CategoryDTORequest categoryDTORequest) {
        Category category = modelMapper.map(categoryDTORequest, Category.class);
        Category categoryName=categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryName!=null){
            throw new APIException(categoryDTORequest.getCategoryName() +" already exists!!!");
        }
        Category savedCategory=categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTORequest.class);
    }

    @Override
    public CategoryDTORequest deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTORequest.class);
    }

    @Override
    public CategoryDTORequest updateCategory(CategoryDTORequest categoryDTORequest, Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if(!optionalCategory.isPresent() || !optionalCategory.get().getCategoryName().equals(categoryDTORequest.getCategoryName())) {
            throw new ResourceNotFoundException("Category", "categoryName", categoryDTORequest.getCategoryName());
        }
        Category categoryToUpdate = optionalCategory.get();
        categoryToUpdate.setCategoryName(categoryDTORequest.getCategoryName());
        Category updatedCategory=categoryRepository.save(categoryToUpdate);
        return modelMapper.map(updatedCategory, CategoryDTORequest.class);
    }
}
