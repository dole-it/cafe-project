package com.cafe_be.controller;

import com.cafe_be.model.Order;
import com.cafe_be.model.OrderItem;
import com.cafe_be.model.Product;
import com.cafe_be.model.enums.OrderStatus;
import com.cafe_be.repository.CafeTableRepository;
import com.cafe_be.repository.OrderRepository;
import com.cafe_be.repository.ProductRepository;
import com.cafe_be.repository.UserRepository;

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
    private final CafeTableRepository tableRepository;
    private final UserRepository userRepository;

    public OrderController(OrderRepository orderRepository, ProductRepository productRepository,
                           CafeTableRepository tableRepository,
                           UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.tableRepository = tableRepository;
        this.userRepository = userRepository;
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
        try {
        Order o = new Order();

        // map items if present; if no items but tableId provided it's a reservation
        List<OrderItem> items = List.of();
        if (req.items == null || req.items.isEmpty()) {
            if (req.tableId == null) {
                throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Order must contain at least one item or be a table reservation");
            }
            // reservation: leave items empty
        } else {
            items = req.items.stream().map(it -> {
                OrderItem oi = new OrderItem();
                Optional<Product> p = productRepository.findById(it.productId);
                if (p.isEmpty()) {
                    throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Product not found: " + it.productId);
                }
                var prod = p.get();
                oi.setProduct(prod);
                oi.setQuantity(it.quantity == null ? 1 : it.quantity);
                oi.setPrice(it.price == null ? prod.getPrice() : it.price);
                return oi;
            }).collect(Collectors.toList());
        }

        double total = items.stream().mapToDouble(i -> (i.getPrice() == null ? 0.0 : i.getPrice()) * i.getQuantity()).sum();
        o.setItems(items);
        o.setTotal(total);
        // RESERVED for table reservation with no items, otherwise NEW
        if ((items == null || items.isEmpty()) && req.tableId != null) {
            o.setStatus(OrderStatus.RESERVED);
        } else {
            o.setStatus(OrderStatus.NEW);
        }
        o.setCreatedAt(LocalDateTime.now());

        // attach table if provided
        if (req.tableId != null) {
            var table = tableRepository.findById(req.tableId);
            if (table.isPresent()) {
                o.setTable(table.get());
                // mark occupied
                var t = table.get();
                t.setStatus(com.cafe_be.model.enums.TableStatus.OCCUPIED);
                tableRepository.save(t);
            }
        }

        // if authenticated, attach customer
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getName() != null) {
            var user = userRepository.findByUsername(auth.getName());
            user.ifPresent(u -> o.setCustomer(u));
        }

        return orderRepository.save(o);
        } catch (org.springframework.web.server.ResponseStatusException rse) {
            throw rse;
        } catch (Exception ex) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create order: " + ex.getMessage(), ex);
        }
    }

    @GetMapping("/my-orders")
    public List<Order> myOrders() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED);
        }
        var userOpt = userRepository.findByUsername(auth.getName());
        if (userOpt.isEmpty()) return List.of();
        var user = userOpt.get();
        return orderRepository.findAll().stream().filter(o -> o.getCustomer() != null && o.getCustomer().getId().equals(user.getId())).collect(Collectors.toList());
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

    @PatchMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestBody StatusChange req) {
        Optional<Order> o = orderRepository.findById(id);
        if (o.isPresent()) {
            Order ord = o.get();
            try {
                ord.setStatus(OrderStatus.valueOf(req.status));
            } catch (IllegalArgumentException iae) {
                throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid status: " + req.status);
            }
            return orderRepository.save(ord);
        }
        throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Order not found");
    }

    public static class StatusChange { public String status; }
}
