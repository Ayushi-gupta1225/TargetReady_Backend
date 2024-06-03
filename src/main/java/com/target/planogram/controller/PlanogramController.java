package com.target.planogram.controller;

import com.target.planogram.entity.Product;
import com.target.planogram.service.PlanogramService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

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

    @GetMapping("/place")
    public ResponseEntity<String> getPlaceEndpoint() {
        return ResponseEntity.ok("This is a POST endpoint. Please use a POST request.");
    }
}
