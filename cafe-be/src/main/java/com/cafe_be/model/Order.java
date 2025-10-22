package com.cafe_be.model;

import com.cafe_be.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User customer; // nullable if walk-in without account

    @ManyToOne(optional = true)
    private CafeTable table;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items;

    private Double total;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.NEW;

    private LocalDateTime createdAt = LocalDateTime.now();
}
