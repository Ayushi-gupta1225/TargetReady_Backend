package com.target.planogram.service;

import com.target.planogram.entity.Location;
import com.target.planogram.entity.Product;
import com.target.planogram.entity.ShelfOccupancy;
import com.target.planogram.repository.LocationRepository;
import com.target.planogram.repository.ProductRepository;
import com.target.planogram.repository.ShelfOccupancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class PlanogramService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ShelfOccupancyRepository shelfOccupancyRepository;

    private static final int SHELF_CAPACITY = 50;

    @Transactional
    public String placeProduct(Product product, int productRow, int productSection, int quantity) {
        Optional<Product> productOpt = productRepository.findById(product.getProductId());
        if (productOpt.isEmpty()) {
            productRepository.save(product);
        } else {
            product = productOpt.get();
        }

        int shelfId = (productRow - 1) * 3 + productSection;

        ShelfOccupancy shelfOccupancy = shelfOccupancyRepository.findById(shelfId).orElse(new ShelfOccupancy(shelfId, 0));
        int currentOccupancy = shelfOccupancy.getOccupancy();

        int totalProductBreadth = product.getBreadth() * quantity;
        if (currentOccupancy + totalProductBreadth > SHELF_CAPACITY) {
            return "Shelf capacity exceeded";
        }

        shelfOccupancy.setOccupancy(currentOccupancy + totalProductBreadth);
        shelfOccupancyRepository.save(shelfOccupancy);

        Location location = new Location();
        location.setProduct(product);
        location.setProductRow(productRow);
        location.setProductSection(productSection);
        location.setQuantity(quantity);
        locationRepository.save(location);

        return "Product placed successfully";
    }
}
