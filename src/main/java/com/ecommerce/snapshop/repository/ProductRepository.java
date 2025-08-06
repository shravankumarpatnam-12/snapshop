package com.ecommerce.snapshop.repository;

import com.ecommerce.snapshop.model.Category;
import com.ecommerce.snapshop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    Page<Product> findByCategory(Optional<Category> optionCategory, Pageable pageDetails);

    Page<Product> findByNameLikeIgnoreCase(String s, Pageable pageDetails);
}
