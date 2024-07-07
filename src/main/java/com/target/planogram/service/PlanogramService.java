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
            int totalProductBreadth = product.getBreadth() * quantity;

            if (totalProductBreadth > SHELF_CAPACITY) {
                return "Shelf capacity exceeded";
            }
            if (product.getHeight() > SHELF_MAX_HEIGHT) {
                return "Shelf height exceeded";
            }
            product = productRepository.save(product);
        }

        int shelfId = (productRow - 1) * planogram.getNumSections() + productSection;
        ShelfOccupancy shelfOccupancy = shelfOccupancyRepository.productSlotOccupancy(planogramId, shelfId)
                .orElse(new ShelfOccupancy(null, shelfId, planogram, 0));
        int currentOccupancy = shelfOccupancy.getOccupancy();

        int totalProductBreadth = product.getBreadth() * quantity;
        if (currentOccupancy + totalProductBreadth > SHELF_CAPACITY) {
            return "Shelf capacity exceeded";
        }
        if (product.getHeight() > SHELF_MAX_HEIGHT) {
            return "Shelf height exceeded";
        }

        shelfOccupancy.setOccupancy(currentOccupancy + totalProductBreadth);
        shelfOccupancyRepository.save(shelfOccupancy);

        Integer maxIndex = locationRepository.maxIndexForSlot(planogramId, productRow, productSection);
        int newIndex = (maxIndex != null) ? maxIndex + 1 : 1;

        Location location = new Location();
        location.setProduct(product);
        location.setProductRow(productRow);
        location.setProductSection(productSection);
        location.setQuantity(quantity);
        location.setPlanogram(planogram);
        location.setIndex(newIndex); // Set the new index here
        locationRepository.save(location);

        return "Product placed successfully";
    }

    @Transactional
    public Planogram createPlanogram(Planogram planogram) {
        System.out.println(STR."Creating planogram with data: \{planogram}");
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
        return locations.stream().map(Location::getProduct).distinct().collect(Collectors.toList());
    }

    @Transactional
    public void deleteProductFromSlot(Long productId, int productRow, int productSection, Long planogramId, int index) {
        Optional<Location> locationOpt = locationRepository.findSelectedProduct(productId, planogramId, productRow, productSection, index);
        if (locationOpt.isPresent()) {
            Location location = locationOpt.get();
            int newQuantity = location.getQuantity() - 1;
            if (newQuantity > 0) {
                location.setQuantity(newQuantity);
                locationRepository.save(location);
            } else {
                locationRepository.deleteSingleProduct(productId, planogramId, productRow, productSection, index);
            }

            int shelfId = (productRow - 1) * planogramRepository.findById(planogramId).get().getNumSections() + productSection;
            ShelfOccupancy shelfOccupancy = shelfOccupancyRepository.productSlotOccupancy(planogramId, shelfId)
                    .orElseThrow(() -> new RuntimeException("ShelfOccupancy not found"));
            int productBreadth = location.getProduct().getBreadth();
            int updatedOccupancy = shelfOccupancy.getOccupancy() - productBreadth;
            shelfOccupancy.setOccupancy(updatedOccupancy);
            shelfOccupancyRepository.save(shelfOccupancy);

            boolean productExistsElsewhere = locationRepository.existsByProductId(productId);
            if (!productExistsElsewhere) {
                productRepository.deleteById(productId);
            }
        }
    }

    @Transactional
    public Product updateProduct(Long productId, Product updatedProduct) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Check if dimensions have changed
        boolean dimensionsChanged = existingProduct.getHeight() != updatedProduct.getHeight() ||
                existingProduct.getBreadth() != updatedProduct.getBreadth();

        if (dimensionsChanged) {
            // Validate new dimensions
            List<Location> locations = locationRepository.findByProduct(productId);

            for (Location location : locations) {
                Planogram planogram = location.getPlanogram();
                int SHELF_CAPACITY = planogram.getSlotWidth();
                int SHELF_MAX_HEIGHT = planogram.getSlotHeight();
                int totalProductBreadth = updatedProduct.getBreadth() * location.getQuantity();

                if (totalProductBreadth > SHELF_CAPACITY) {
                    throw new IllegalArgumentException(STR."Shelf capacity exceeded for location: \{location.getLocationId()}");
                }
                if (updatedProduct.getHeight() > SHELF_MAX_HEIGHT) {
                    throw new IllegalArgumentException(STR."Shelf height exceeded for location: \{location.getLocationId()}");
                }

                // Check if the new dimensions fit in the current shelf occupancy
                int shelfId = (location.getProductRow() - 1) * planogram.getNumSections() + location.getProductSection();
                ShelfOccupancy shelfOccupancy = shelfOccupancyRepository.productSlotOccupancy(planogram.getPlanogramId(), shelfId)
                        .orElseThrow(() -> new IllegalArgumentException(STR."Shelf occupancy not found for location: \{location.getLocationId()}"));

                int currentOccupancy = shelfOccupancy.getOccupancy();
                int existingProductBreadth = existingProduct.getBreadth() * location.getQuantity();
                int newOccupancy = currentOccupancy - existingProductBreadth + totalProductBreadth;

                if (newOccupancy > SHELF_CAPACITY) {
                    throw new IllegalArgumentException(STR."Updated product exceeds shelf capacity for location: \{location.getLocationId()}");
                }

                // Update the shelf occupancy
                shelfOccupancy.setOccupancy(newOccupancy);
                shelfOccupancyRepository.save(shelfOccupancy);
            }
        }

        // Update the fields of the existing product
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setHeight(updatedProduct.getHeight());
        existingProduct.setBreadth(updatedProduct.getBreadth());

        // Save the updated product
        return productRepository.save(existingProduct);
    }


    private void updateShelfOccupancyTable(Product product, Location location) {
        int calculateShelf = (location.getProductRow()-1)*(location.getPlanogram().getNumSections())+location.getProductSection();

        List<ShelfOccupancy> occupancies = shelfOccupancyRepository.findByPlanogramAndShelfId(location.getPlanogram().getPlanogramId(), calculateShelf);

        for (ShelfOccupancy occupancy : occupancies) {
            int newOccupancy = calculateNewOccupancy(product, location);
            occupancy.setOccupancy(newOccupancy);
            shelfOccupancyRepository.save(occupancy);
        }
    }

    private int calculateNewOccupancy(Product product, Location location) {
        return location.getQuantity() * product.getBreadth();
    }
}


