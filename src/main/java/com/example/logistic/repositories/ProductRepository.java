package com.example.logistic.repositories;

import com.example.logistic.entities.Customer;
import com.example.logistic.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getAllByCustomer(Customer customer, PageRequest pageRequest);
    Product getProductById(Long id);
    @Query(value = "Select * from Product as p Where p.name like %:keyword% and p.customer_id = :customer_id",nativeQuery = true)
    Page<Product> getProducts(String keyword, Long customer_id, PageRequest pageRequest);
}
