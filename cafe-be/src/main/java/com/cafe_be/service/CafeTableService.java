package com.cafe_be.service;

import com.cafe_be.model.CafeTable;
import java.util.List;

public interface CafeTableService {
    CafeTable create(CafeTable t);
    CafeTable update(Long id, CafeTable t);
    List<CafeTable> listAll();
    CafeTable findById(Long id);
}
