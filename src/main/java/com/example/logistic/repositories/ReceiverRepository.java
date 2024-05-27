package com.example.logistic.repositories;

import com.example.logistic.entities.Customer;
import com.example.logistic.entities.Product;
import com.example.logistic.entities.Receiver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiverRepository extends JpaRepository<Receiver, Long> {
    Receiver findByPhoneNumberAndCustomer(String phoneNumber, Customer customer);
    @Query(value = "Select * from Receiver as r Where r.name like %:keyword% and r.customer_id = :customer_id",nativeQuery = true)
    Page<Receiver> getReceivers(String keyword, Long customer_id, PageRequest pageRequest);
}
