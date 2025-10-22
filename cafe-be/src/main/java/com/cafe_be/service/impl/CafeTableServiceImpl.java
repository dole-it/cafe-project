package com.cafe_be.service.impl;

import com.cafe_be.model.CafeTable;
import com.cafe_be.repository.CafeTableRepository;
import com.cafe_be.service.CafeTableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CafeTableServiceImpl implements CafeTableService {

    private final CafeTableRepository repo;

    public CafeTableServiceImpl(CafeTableRepository repo) {
        this.repo = repo;
    }

    @Override
    public CafeTable create(CafeTable t) {
        return repo.save(t);
    }

    @Override
    public CafeTable update(Long id, CafeTable t) {
        CafeTable existing = repo.findById(id).orElseThrow();
        existing.setName(t.getName());
        existing.setStatus(t.getStatus());
        return repo.save(existing);
    }

    @Override
    public List<CafeTable> listAll() {
        return repo.findAll();
    }

    @Override
    public CafeTable findById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public CafeTable updateStatus(Long id, com.cafe_be.model.enums.TableStatus status) {
        CafeTable table = repo.findById(id).orElseThrow(() -> new RuntimeException("Table not found"));
        table.setStatus(status);
        return repo.save(table);
    }
}
