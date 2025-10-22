package com.cafe_be.model;

import com.cafe_be.model.enums.TableStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cafe_tables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CafeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., "Table 1"

    @Column(nullable = false)
    private Integer capacity = 4; // Default capacity is 4

    @Column(nullable = false)
    private Boolean available = true;

    @Enumerated(EnumType.STRING)
    private TableStatus status = TableStatus.AVAILABLE;
}
