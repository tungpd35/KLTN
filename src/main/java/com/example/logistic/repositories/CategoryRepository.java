package com.example.logistic.repositories;

import com.example.logistic.entities.Category;
import com.example.logistic.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoriesByName(String name);
    @Query(value = "SELECT * FROM category as c where c.name like %:keyword% and c.customer_id = :customer_id",nativeQuery = true)
    Page<Category> searchCategories(String keyword, Long customer_id, PageRequest pageRequest);
}
