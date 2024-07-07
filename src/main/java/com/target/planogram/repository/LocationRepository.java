package com.target.planogram.repository;

import com.target.planogram.entity.Location;
import com.target.planogram.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT l FROM Location l WHERE l.planogram.planogramId = :planogramId")
    List<Location> findByPlanogramId(@Param("planogramId") Long planogramId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Location l WHERE l.planogram.planogramId = :planogramId")
    void deleteByPlanogramId(@Param("planogramId") Long planogramId);

    @Query("SELECT DISTINCT l.product FROM Location l")
    List<Product> findProductsInUse();

    @Query("SELECT l FROM Location l WHERE l.product.productId = :productId AND l.planogram.planogramId = :planogramId AND l.productRow = :productRow AND l.productSection = :productSection AND l.index = :index")
    Optional<Location> findSelectedProduct(@Param("productId") Long productId, @Param("planogramId") Long planogramId, @Param("productRow") int productRow, @Param("productSection") int productSection, @Param("index") int index);

    @Transactional
    @Modifying
    @Query("DELETE FROM Location l WHERE l.product.productId = :productId AND l.planogram.planogramId = :planogramId AND l.productRow = :productRow AND l.productSection = :productSection AND l.index = :index")
    void deleteSingleProduct(@Param("productId") Long productId, @Param("planogramId") Long planogramId, @Param("productRow") int productRow, @Param("productSection") int productSection, @Param("index") int index);

    @Query("SELECT MAX(l.index) FROM Location l WHERE l.planogram.planogramId = :planogramId AND l.productRow = :productRow AND l.productSection = :productSection")
    Integer maxIndexForSlot(@Param("planogramId") Long planogramId, @Param("productRow") int productRow, @Param("productSection") int productSection);

    @Query("SELECT COUNT(l) > 0 FROM Location l WHERE l.product.productId = :productId")
    boolean existsByProductId(@Param("productId") Long productId);

    @Query("SELECT l FROM Location l WHERE l.product.productId = :productId")
    List<Location> findByProduct(@Param("productId") Long productId);
}
