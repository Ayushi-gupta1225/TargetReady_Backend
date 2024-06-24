//package com.target.planogram.controller;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.target.planogram.entity.Product;
//import com.target.planogram.service.PlanogramService;
//import lombok.extern.java.Log;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//    @RunWith(SpringRunner.class)
//    @ActiveProfiles("test")
//    @SpringBootTest
//    @Log
//    public class PlanogramControllerTest {
//        @InjectMocks
//        public PlanogramController planogramController;
//
//        @Mock
//        private PlanogramService planogramService;
//
//        @Test
//        public void testPlaceProduct_Success() {
//            // Arrange
//            Product product = new Product();
//            product.setProductId(3L);
//            product.setBreadth(10);
//            product.setHeight(10);
//            int productRow = 1;
//            int productSection = 2;
//            int quantity = 3;
//            String expectedResult = "Product placed successfully";
//
//            when(planogramService.placeProduct(product, productRow, productSection, quantity)).thenReturn(expectedResult);
//
//            // Act
//            ResponseEntity<String> response = planogramController.placeProduct(product, productRow, productSection, quantity);
//            log.info(response.getBody().toString());
//            // Assert
//            assertEquals(ResponseEntity.ok(expectedResult), response);
//        }
//
//        @Test
//        public void testPlaceProduct_Failure() {
//            // Arrange
//            Product product = new Product();
//            product.setProductId(3L);
//            product.setBreadth(10);
//            product.setHeight(10);
//            int productRow = 1;
//            int productSection = 2;
//            int quantity = 3;
//            String expectedResult = "Failed to place product";
//
//           when(planogramService.placeProduct(product, productRow, productSection, quantity)).thenReturn(expectedResult);
//
//            // Act
//            ResponseEntity<String> response = planogramController.placeProduct(product, productRow, productSection, quantity);
//            log.info(response.getBody().toString());
//            // Assert
//            assertEquals(ResponseEntity.badRequest().body(expectedResult), response);
//        }
//
//        @Test
//        public void testGetData() {
//            // Arrange
//            Map<String, Object> expectedData = new HashMap<>();
//            expectedData.put("locations", planogramService.getAllLocations());
//            expectedData.put("products", planogramService.getAllProducts());
//
//            // Act
//            ResponseEntity<Map<String, Object>> response = planogramController.getData();
//            log.info(response.getBody().toString());
//
//            // Assert
//            assertEquals(ResponseEntity.ok(expectedData), response);
//        }
//
//
//}