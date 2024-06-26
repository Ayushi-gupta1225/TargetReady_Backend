package com.target.planogram.service;

import com.target.planogram.entity.Location;
import com.target.planogram.entity.Planogram;
import com.target.planogram.entity.Product;
import com.target.planogram.entity.ShelfOccupancy;
import com.target.planogram.repository.LocationRepository;
import com.target.planogram.repository.PlanogramRepository;
import com.target.planogram.repository.ProductRepository;
import com.target.planogram.repository.ShelfOccupancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlanogramService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private ShelfOccupancyRepository shelfOccupancyRepository;
    @Autowired
    private PlanogramRepository planogramRepository;

    @Transactional
    public String placeProduct(Product product, int productRow, int productSection, int quantity, Long planogramId) {
        Planogram planogram = planogramRepository.findById(planogramId).orElse(null);
        if (planogram == null) {
            return "Planogram not found";
        }

        int SHELF_CAPACITY = planogram.getSlotWidth();
        int SHELF_MAX_HEIGHT = planogram.getSlotHeight();

        Optional<Product> productOpt = productRepository.findByName(product.getName());

        if (productOpt.isPresent()) {
            product = productOpt.get();
        } else {
            int productHeight = product.getHeight();
            int totalProductBreadth = product.getBreadth() * quantity;

            if (totalProductBreadth > SHELF_CAPACITY) {
                return "Shelf capacity exceeded";
            }
            if (productHeight > SHELF_MAX_HEIGHT) {
                return "Shelf height exceeded";
            }

            product = productRepository.save(product); // Save the product if it does not exist
        }

        int shelfId = (productRow - 1) * planogram.getNumSections() + productSection;
        ShelfOccupancy shelfOccupancy = shelfOccupancyRepository.findById((long) shelfId)
                .orElse(new ShelfOccupancy(null, shelfId, planogram, 0));
        int currentOccupancy = shelfOccupancy.getOccupancy();
        int productHeight = product.getHeight();
        int totalProductBreadth = product.getBreadth() * quantity;

        if (currentOccupancy + totalProductBreadth > SHELF_CAPACITY) {
            return "Shelf capacity exceeded";
        }
        if (productHeight > SHELF_MAX_HEIGHT) {
            return "Shelf height exceeded";
        }

        shelfOccupancy.setOccupancy(currentOccupancy + totalProductBreadth);
        shelfOccupancyRepository.save(shelfOccupancy);

        Location location = new Location();
        location.setProduct(product);
        location.setProductRow(productRow);
        location.setProductSection(productSection);
        location.setQuantity(quantity);
        location.setPlanogram(planogram);
        locationRepository.save(location);

        return "Product placed successfully";
    }

    @Transactional
    public Planogram createPlanogram(Planogram planogram) {
        System.out.println("Creating planogram with data: " + planogram);
        return planogramRepository.save(planogram);
    }

    @Transactional
    public void deletePlanogram(Long planogramId) {
        locationRepository.deleteByPlanogramId(planogramId);
        shelfOccupancyRepository.deleteByPlanogramId(planogramId);
        List<Product> productsInUse = locationRepository.findProductsInUse();
        List<Product> allProducts = productRepository.findAll();
        allProducts.removeAll(productsInUse);
        productRepository.deleteAll(allProducts);
        planogramRepository.deleteById(planogramId);
    }

    public List<Location> getAllLocations(Long planogramId) {
        return locationRepository.findByPlanogramId(planogramId);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Planogram> getAllPlanograms() {
        return planogramRepository.findAll();
    }

    public List<Product> getProductsByPlanogram(Long planogramId) {
        List<Location> locations = locationRepository.findByPlanogramId(planogramId);
        List<Product> products = locations.stream()
                .map(Location::getProduct)
                .distinct()
                .collect(Collectors.toList());
        return products;
    }
}
