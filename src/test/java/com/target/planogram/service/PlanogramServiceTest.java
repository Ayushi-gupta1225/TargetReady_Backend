package com.target.planogram.service;

import static org.junit.jupiter.api.Assertions.*;

import com.target.planogram.entity.Location;
import com.target.planogram.entity.Product;
import com.target.planogram.entity.ShelfOccupancy;
import com.target.planogram.repository.LocationRepository;
import com.target.planogram.repository.ProductRepository;
import com.target.planogram.repository.ShelfOccupancyRepository;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Log
public class PlanogramServiceTest {
    @InjectMocks
    public PlanogramService planogramService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private ShelfOccupancyRepository shelfOccupancyRepository;


    @Test
    public void testPlaceProduct_ShelfCapacityExceeded() {
        // Arrange
        Product product = new Product();
        product.setProductId(3L);
        product.setBreadth(10);
        product.setHeight(10);
        int productRow = 1;
        int productSection = 1;
        int quantity = 10;

        log.info(product.toString());
        ShelfOccupancy shelfOccupancy = new ShelfOccupancy(1, 80);
        when(shelfOccupancyRepository.findById(1)).thenReturn(Optional.of(shelfOccupancy));

        // Act
        String result = planogramService.placeProduct(product, productRow, productSection, quantity);

        // Assert
        assertEquals("Shelf capacity exceeded", result);
    }

    @Test
    public void testPlaceProduct_ShelfHeightExceeded() {
        // Arrange
        Product product = new Product();
        product.setBreadth(10);
        product.setHeight(46);
        int productRow = 1;
        int productSection = 1;
        int quantity = 1;

        ShelfOccupancy shelfOccupancy = new ShelfOccupancy(1, 0);
        when(shelfOccupancyRepository.findById(1)).thenReturn(Optional.of(shelfOccupancy));

        // Act
        String result = planogramService.placeProduct(product, productRow, productSection, quantity);
        log.info(result);
        // Assert
        assertEquals("Shelf height exceeded", result);
    }

    @Test
    public void testPlaceProduct_Success() {
        // Arrange
        Product product = new Product();
        product.setBreadth(10);
        product.setHeight(10);
        int productRow = 1;
        int productSection = 1;
        int quantity = 1;

        ShelfOccupancy shelfOccupancy = new ShelfOccupancy(1, 0);
        when(shelfOccupancyRepository.findById(1)).thenReturn(Optional.of(shelfOccupancy));

        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));

        // Act
        String result = planogramService.placeProduct(product, productRow, productSection, quantity);
        log.info(result);
        // Assert
        assertEquals("Product placed successfully", result);
    }

    @Test
    public void testGetAllLocations() {
        // Arrange
        Product product1 = new Product();
        product1.setBreadth(10);
        product1.setHeight(10);
        Product product2 = new Product();
        product2.setBreadth(5);
        product2.setHeight(5);
        List<Location> expectedLocations = Arrays.asList(
                new Location(1L, product1, 1, 1, 1),
                new Location(2L, product2, 2, 1, 1)
        );
        log.info("expectedLocations" + expectedLocations.toString());
        when(locationRepository.findAll()).thenReturn(expectedLocations);

        // Act
        List<Location> result = planogramService.getAllLocations();

        // Assert
        assertEquals(expectedLocations, result);
    }


      @Test
    public void testGetAllProducts() {
          // Arrange
          List<Product> expectedProducts = Arrays.asList(
                  new Product(1L, "Product 1", 10, 10),
                  new Product(2L, "Product 2", 20, 20)
          );


          when(productRepository.findAll()).thenReturn(expectedProducts);

          // Act
          List<Product> result = planogramService.getAllProducts();

          // Assert
          assertEquals(expectedProducts, result);
      }
}