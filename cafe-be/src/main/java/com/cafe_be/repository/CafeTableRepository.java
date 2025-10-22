package com.cafe_be.repository;

import com.cafe_be.model.CafeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeTableRepository extends JpaRepository<CafeTable, Long> {
}
