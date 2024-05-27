package com.example.logistic.services;

import com.example.logistic.dto.InvoiceDetailDto;
import com.example.logistic.dto.OrderDto;
import com.example.logistic.entities.*;
import com.example.logistic.repositories.OrderRepository;
import com.example.logistic.repositories.ProductRepository;
import com.example.logistic.repositories.StatusRepository;
import com.example.logistic.repositories.WareHouseRepository;
import jakarta.transaction.Transactional;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class OrderService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WareHouseRepository wareHouseRepository;
    @Autowired
    private StatusRepository statusRepository;
    public OrderProduct mapToOrderAndSave(OrderDto orderDto, Customer customer){
        OrderProduct orderProduct = new OrderProduct(new Date(),statusRepository.findByGroupId(orderDto.getStatus()),
                orderDto.getNote(),orderDto.getCod(),orderDto.getWarranty(), orderDto.getPayment_type_id(),
                orderDto.getRequired_note(),orderDto.getWeight(), orderDto.getTotal_price_product(), orderDto.isPayment_status(),
                orderDto.getTxnRef(), orderDto.getTransportFee(), orderDto.getWarrantyFee());
        Invoice invoice = new Invoice();
        int totalPrice = 0;
        for( InvoiceDetailDto invoiceDetailDto : orderDto.getInvoiceDetailDtoList()) {
            totalPrice += invoiceDetailDto.getPrice() * invoiceDetailDto.getQuantity();
            if(invoice.getInvoiceDetailList() == null) invoice.setInvoiceDetailList(new ArrayList<>());
            invoice.getInvoiceDetailList().add( new InvoiceDetail(invoice,productRepository.getProductById(invoiceDetailDto.getProductId()),
                    invoiceDetailDto.getQuantity(),invoiceDetailDto.getPrice()));
        }
        orderProduct.setTotal_price_product(totalPrice);
        ReceiverInfo receiverInfo = new ReceiverInfo(orderDto.getFullName(),orderDto.getAddress(),
                orderDto.getCity(), orderDto.getDistrict(), orderDto.getWard(),orderDto.getPhoneNumber());
        invoice.setOrderProduct(orderProduct);
        invoice.setCustomer(customer);
        invoice.setInvoiceDate(new Date());

        orderProduct.setFrom_name(customer.getFullName());
        orderProduct.setFrom_phone(customer.getPhoneNumber());
        orderProduct.setInvoice(invoice);
        orderProduct.setReceiverInfo(receiverInfo);

        customer.getOrderProducts().add(orderProduct);
        orderProduct.setCustomers(customer);
        orderProduct.setWarehouse(wareHouseRepository.getWarehouseById(orderDto.getWarehouse()));
        return orderRepository.save(orderProduct);
    }
}
