package com.cafe_be.controller;

import com.cafe_be.model.CafeTable;
import com.cafe_be.service.CafeTableService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class CafeTableController {

    private final CafeTableService service;

    public CafeTableController(CafeTableService service) {
        this.service = service;
    }

    @GetMapping
    public List<CafeTable> list() { return service.listAll(); }

    @PostMapping
    public CafeTable create(@RequestBody CafeTable t) { return service.create(t); }
}
