package com.target.planogram.controller;

import com.target.planogram.entity.Product;
import com.target.planogram.service.PlanogramService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlanogramController {
    private final PlanogramService planogramService;

    @PostMapping("/place")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<String> placeProduct(
            @RequestBody Product product,
            @RequestParam int productRow,
            @RequestParam int productSection,
            @RequestParam int quantity
    ) {
        String result = planogramService.placeProduct(product, productRow, productSection, quantity);
        if (result.equals("Product placed successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/data")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Map<String, Object>> getData() {
        Map<String, Object> data = new HashMap<>();
        data.put("locations", planogramService.getAllLocations());
        data.put("products", planogramService.getAllProducts());
        return ResponseEntity.ok(data);
    }
}
