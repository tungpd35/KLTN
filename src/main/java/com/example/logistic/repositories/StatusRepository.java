package com.example.logistic.repositories;

import com.example.logistic.entities.StatusOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<StatusOrder, Long> {
    StatusOrder findByGroupId(int groupId);
}
