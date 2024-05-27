package com.example.logistic.repositories;

import com.example.logistic.entities.Notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifyRepository extends JpaRepository<Notification, Long> {
   Page<Notification> getNotificationsByUserIdOrderByCreateTimeDesc(Long id, PageRequest pageRequest);
   List<Notification> getAllByUserId(Long id);
}
