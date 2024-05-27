package com.example.logistic.repositories;

import com.example.logistic.entities.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    ConfirmationToken findConfirmationTokenByToken(String token);
}
