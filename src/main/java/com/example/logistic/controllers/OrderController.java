package com.example.logistic.controllers;

import com.example.logistic.dto.OrderDto;
import com.example.logistic.dto.StatusCount;
import com.example.logistic.entities.OrderProduct;
import com.example.logistic.entities.Role;
import com.example.logistic.entities.StatusOrder;
import com.example.logistic.entities.User;
import com.example.logistic.payload.Response;
import com.example.logistic.repositories.OrderRepository;
import com.example.logistic.repositories.StatusRepository;
import com.example.logistic.services.OrderService;
import com.example.logistic.services.UserService;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@CrossOrigin
public class OrderController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StatusRepository statusRepository;
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String token, @RequestBody OrderDto orderDto) {
        User user = userService.getUserFromToken(token);
        OrderProduct orderProduct = orderService.mapToOrderAndSave(orderDto,user.getCustomer());
        return ResponseEntity.ok(new Response(200,true,orderProduct));
    }
    @GetMapping("/list")
    public Page<OrderProduct> getOrder(@RequestHeader("Authorization") String token,
                                       @RequestParam int page, @RequestParam int page_size,
                                       @RequestParam String keyword, @RequestParam int status) {
        PageRequest pageRequest = PageRequest.of(page,page_size);
        User user = userService.getUserFromToken(token);
        if(user.getRole() == Role.STAFF) {
            if(status != 0) {
                return orderRepository.getOrderProductByStatusAndWarehouseId(statusRepository.findByGroupId(status),
                        user.getStaff().getWarehouse().getId(),pageRequest);
            }
            return orderRepository.getOrderByWarehouse(keyword,user.getStaff().getWarehouse().getId(),pageRequest);
        }
        if(user.getRole() == Role.CUSTOMER) {
            if(status != 0) {
                return orderRepository.getOrderProductByStatusAndCustomers(statusRepository.findByGroupId(status),user.getCustomer(),pageRequest);
            }
            return  orderRepository.getOrder(keyword,user.getCustomer().getId(),pageRequest);
        }
        return orderRepository.getOrder(keyword,user.getCustomer().getId(),pageRequest);
    }
    @GetMapping("/getOrder")
    public ResponseEntity<?> getOrderByID(@RequestHeader("Authorization") String token, @RequestParam Long id) {
        User user = userService.getUserFromToken(token);
        if(user.getRole() == Role.STAFF) {
            return ResponseEntity.ok(new Response(200,true,orderRepository.getOrderProductById(id)));
        } else {
            return ResponseEntity.ok(new Response(200,true,orderRepository.getOrderProductByIdAndCustomers(id, user.getCustomer())));
        }
    }
    @PutMapping("/preparing")
    public ResponseEntity<?> editStatus(@RequestParam Long id, @RequestParam int status, @RequestParam String orderCode) {
        System.out.println("Ok");
        OrderProduct order = orderRepository.getOrderProductById(id);
        order.setStatus(statusRepository.findByGroupId(status));
        order.setOrderCode(orderCode);
        orderRepository.save(order);
        return ResponseEntity.ok(new Response(200,true,order));
    }
    @PutMapping("/deny")
    public ResponseEntity<?> denyOrder(@RequestParam Long id, @RequestParam int status) {
        OrderProduct order = orderRepository.getOrderProductById(id);
        order.setStatus(statusRepository.findByGroupId(status));
        orderRepository.save(order);
        return ResponseEntity.ok(new Response(200,true,order));
    }
    @PutMapping("/picking")
    public ResponseEntity<?> pick(@RequestParam Long id, @RequestParam int status) {
        System.out.println("Ok");
        OrderProduct order = orderRepository.getOrderProductById(id);
        order.setStatus(statusRepository.findByGroupId(status));
        orderRepository.save(order);
        return ResponseEntity.ok(new Response(200,true,order));
    }
    @GetMapping("/countStatus")
    public ResponseEntity<?> countStatus(@RequestHeader("Authorization") String token) {
        User user = userService.getUserFromToken(token);
        if(user.getRole() == Role.CUSTOMER) {
            List<StatusCount> statusCountList = orderRepository.countStatus(user.getCustomer().getId());
            return ResponseEntity.ok(new Response(200,true,statusCountList));
        } else {
            List<StatusCount> statusCountList = orderRepository.countStatusByWarehouse(user.getStaff().getWarehouse().getId());
            return ResponseEntity.ok(new Response(200,true,statusCountList));
        }

    }
    @GetMapping("/payment/success")
    public ResponseEntity<?> success(@RequestParam String txnRef) {
        OrderProduct orderProduct = orderRepository.findOrderProductByTxnRef(txnRef);
        orderProduct.setPayment_status(true);
        orderProduct.setStatus(statusRepository.findByGroupId(1));
        orderRepository.save(orderProduct);
        return ResponseEntity.ok(new Response(200,true,orderProduct));
    }
}
