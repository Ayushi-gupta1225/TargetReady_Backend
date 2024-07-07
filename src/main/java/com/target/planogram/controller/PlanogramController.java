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
        System.out.println(STR."Received request to create planogram: \{planogram}");
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
        System.out.println(STR."Received product data: \{product}");
        System.out.println(STR."Received planogramId: \{planogramId}");

        String result = planogramService.placeProduct(product, productRow, productSection, quantity, planogramId);
        if (result.equals("Product placed successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/planogram/{planogramId}/products")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<List<Product>> getProductsByPlanogram(@PathVariable Long planogramId) {
        List<Product> products = planogramService.getProductsByPlanogram(planogramId);
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/planogram/{planogramId}/product/{productId}/slot")
    public ResponseEntity<String> deleteProductFromSlot(
            @PathVariable Long productId,
            @RequestParam int productRow,
            @RequestParam int productSection,
            @RequestParam int index,
            @PathVariable Long planogramId) {
        planogramService.deleteProductFromSlot(productId, productRow, productSection, planogramId, index);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product updatedProduct) {
        try {
            if (updatedProduct.getName() == null || updatedProduct.getHeight() <= 0 || updatedProduct.getBreadth() <= 0) {
                throw new IllegalArgumentException("Invalid product data");
            }
            Product product = planogramService.updateProduct(productId, updatedProduct);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            System.out.println("Bad Request");
            return ResponseEntity.badRequest().build();
        }
    }
}
