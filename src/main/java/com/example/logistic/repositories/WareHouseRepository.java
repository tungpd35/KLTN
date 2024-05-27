package com.example.logistic.repositories;

import com.example.logistic.entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WareHouseRepository extends JpaRepository<Warehouse, Long> {
    Warehouse getWarehouseById(Long id);
}
