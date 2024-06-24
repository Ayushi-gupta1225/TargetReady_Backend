package com.target.planogram.controller;

import com.target.planogram.entity.Planogram;
import com.target.planogram.entity.Product;
import com.target.planogram.service.PlanogramService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlanogramController {
    private final PlanogramService planogramService;

    @PostMapping("/admin/planogram")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Planogram> createPlanogram(@RequestBody Planogram planogram) {
        System.out.println("Received request to create planogram: " + planogram);
        Planogram createdPlanogram = planogramService.createPlanogram(planogram);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlanogram);
    }

    @DeleteMapping("/admin/planogram/{planogramId}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Void> deletePlanogram(@PathVariable Long planogramId) {
        planogramService.deletePlanogram(planogramId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/planograms")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<List<Planogram>> getAllPlanograms() {
        List<Planogram> planograms = planogramService.getAllPlanograms();
        return ResponseEntity.ok(planograms);
    }

    @GetMapping("/planogram/{planogramId}/data")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Map<String, Object>> getPlanogramData(@PathVariable Long planogramId) {
        Map<String, Object> data = new HashMap<>();
        data.put("locations", planogramService.getAllLocations(planogramId));
        data.put("products", planogramService.getAllProducts());
        return ResponseEntity.ok(data);
    }

    @PostMapping("/planogram/{planogramId}/place")
    public ResponseEntity<String> placeProduct(
            @RequestBody Product product,
            @PathVariable Long planogramId,
            @RequestParam int productRow,
            @RequestParam int productSection,
            @RequestParam int quantity) {
        System.out.println("Received product data: " + product);
        System.out.println("Received planogramId: " + planogramId);

        String result = planogramService.placeProduct(product, productRow, productSection, quantity, planogramId);
        if (result.equals("Product placed successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}
