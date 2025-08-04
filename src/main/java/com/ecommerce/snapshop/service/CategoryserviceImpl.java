package com.ecommerce.snapshop.service;

import com.ecommerce.snapshop.exceptions.APIException;
import com.ecommerce.snapshop.exceptions.CategoryNotFoundException;
import com.ecommerce.snapshop.exceptions.ResourceNotFoundException;
import com.ecommerce.snapshop.model.Category;
import com.ecommerce.snapshop.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryserviceImpl implements CategoryService {
    //private List<Category> categoryList=new ArrayList<>();
    private CategoryRepository categoryRepository;

    public CategoryserviceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new APIException("No categories found");
        }
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(Category category) {
        Category categoryName=categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryName!=null){
            throw new APIException(category.getCategoryName() +" already exists!!!");
        }
        categoryRepository.save(category);
        return category;
    }

    @Override
    public String deleteCategory(Long categoryId) {
//        List<Category> categories = categoryRepository.findAll();
//        Category category=categories.stream().filter(c->c.getCategoryId().equals(categoryId)).
//                findFirst().orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource Not Found"));
//        categoryRepository.delete(category);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
        categoryRepository.delete(category);
        return category.getCategoryName()+" Successfully deleted";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if(optionalCategory.isPresent()){
            Category categoryToUpdate = optionalCategory.get();
            categoryToUpdate.setCategoryName(category.getCategoryName());
            categoryRepository.save(categoryToUpdate);
        }else{
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category Not Found");
            throw new ResourceNotFoundException("Category","categoryName",category.getCategoryName());
        }

//        Optional<Category> optionalCategory = categoryRepository.findAll().
//                stream().filter(c -> c.getCategoryId().equals(categoryId))
//                .findFirst();
//        if (optionalCategory.isPresent()) {
//            Category categoryToUpdate = optionalCategory.get();
//            categoryToUpdate.setCategoryName(category.getCategoryName());
//            categoryRepository.save(categoryToUpdate);
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found");
//        }
        return category;
    }
}
