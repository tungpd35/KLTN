package com.example.logistic.repositories;

import com.example.logistic.dto.StatusCount;
import com.example.logistic.entities.Customer;
import com.example.logistic.entities.OrderProduct;
import com.example.logistic.entities.StatusOrder;
import org.hibernate.query.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderProduct, Long> {
    OrderProduct findOrderProductByTxnRef(String txnRef);
    OrderProduct getOrderProductById(Long id);
    OrderProduct getOrderProductByIdAndCustomers(Long id, Customer customer);
    Page<OrderProduct> getOrderProductByStatusAndCustomers(StatusOrder status, Customer customers, Pageable pageable);
    Page<OrderProduct> getOrderProductByStatusAndWarehouseId(StatusOrder status, Long warehouse_id, Pageable pageable);
    @Query(value = "Select * from order_product as o Where o.id like %:keyword% and o.customers_id = :customer_id",nativeQuery = true)
    Page<OrderProduct> getOrder(String keyword, Long customer_id, PageRequest pageRequest);
    @Query(value = "Select * from order_product as o Where o.id like %:keyword% and o.warehouse_id = :warehouse_id",nativeQuery = true)
    Page<OrderProduct> getOrderByWarehouse(String keyword, Long warehouse_id, PageRequest pageRequest);
    @Query(value = "SELECT status_order.id as id, status_order.group_id as groupId, status_order.name as name, count(status_order.group_id) as count FROM status_order LEFT JOIN " +
            "order_product ON status_order.id = order_product.status_id WHERE order_product.customers_id = :customer_id GROUP BY status_order.id, status_order.group_id", nativeQuery = true)
    List<StatusCount> countStatus(Long customer_id);
    @Query(value = "SELECT status_order.id as id, status_order.group_id as groupId, status_order.name as name, count(status_order.group_id) as count FROM status_order LEFT JOIN " +
            "order_product ON status_order.id = order_product.status_id WHERE order_product.warehouse_id = :warehouse_id GROUP BY status_order.id, status_order.group_id", nativeQuery = true)
    List<StatusCount> countStatusByWarehouse(Long warehouse_id);

}
