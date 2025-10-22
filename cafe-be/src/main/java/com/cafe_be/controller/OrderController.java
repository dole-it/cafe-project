package com.cafe_be.controller;

import com.cafe_be.model.Order;
import com.cafe_be.model.OrderItem;
import com.cafe_be.model.Product;
import com.cafe_be.model.enums.OrderStatus;
import com.cafe_be.repository.OrderRepository;
import com.cafe_be.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    // DTOs for creating an order from frontend
    public static class CreateOrderRequest {
        public Long tableId; // optional
        public List<CreateItem> items;
        public String deliveryTime; // optional string
    }

    public static class CreateItem {
        public Long productId;
        public Integer quantity;
        public Double price; // snapshot price from client (optional)
    }

    @PostMapping
    public Order create(@RequestBody CreateOrderRequest req) {
        Order o = new Order();
        // map items
        List<OrderItem> items = req.items.stream().map(it -> {
            OrderItem oi = new OrderItem();
            Optional<Product> p = productRepository.findById(it.productId);
            p.ifPresent(prod -> oi.setProduct(prod));
            oi.setQuantity(it.quantity == null ? 1 : it.quantity);
            oi.setPrice(it.price == null && p.isPresent() ? p.get().getPrice() : it.price);
            return oi;
        }).collect(Collectors.toList());

        double total = items.stream().mapToDouble(i -> (i.getPrice() == null ? 0.0 : i.getPrice()) * i.getQuantity()).sum();
        o.setItems(items);
        o.setTotal(total);
        o.setStatus(OrderStatus.NEW);
        o.setCreatedAt(LocalDateTime.now());

        return orderRepository.save(o);
    }

    @GetMapping
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @GetMapping("/recent")
    public List<Order> recent() {
        return orderRepository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(10)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestBody StatusChange req) {
        Optional<Order> o = orderRepository.findById(id);
        if (o.isPresent()) {
            Order ord = o.get();
            ord.setStatus(OrderStatus.valueOf(req.status));
            return orderRepository.save(ord);
        }
        throw new RuntimeException("Order not found");
    }

    public static class StatusChange { public String status; }
}
