package com.cafe_be.controller;

import com.cafe_be.model.Order;
import com.cafe_be.repository.OrderRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final OrderRepository orderRepository;

    public AdminController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/stats/daily")
    public Map<String, Object> dailyStats(@RequestParam(required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) date = LocalDate.now();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<Order> orders = orderRepository.findAll().stream()
                .filter(o -> o.getCreatedAt().isAfter(start.minusSeconds(1)) && o.getCreatedAt().isBefore(end))
                .toList();

        double revenue = orders.stream().mapToDouble(o -> o.getTotal() == null ? 0.0 : o.getTotal()).sum();
        long guests = orders.stream().filter(o -> o.getCustomer() != null).count();

        Map<String, Object> res = new HashMap<>();
        res.put("date", date.toString());
        res.put("totalRevenue", revenue);
        res.put("guestCount", guests);
        res.put("orderCount", orders.size());
        return res;
    }
}
