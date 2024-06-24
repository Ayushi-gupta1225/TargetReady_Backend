package com.target.planogram.repository;

import com.target.planogram.entity.Location;
import com.target.planogram.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT l FROM Location l WHERE l.planogram.planogramId = :planogramId")
    List<Location> findByPlanogramId(@Param("planogramId") Long planogramId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Location l WHERE l.planogram.planogramId = :planogramId")
    void deleteByPlanogramId(@Param("planogramId") Long planogramId);

    @Query("SELECT DISTINCT l.product FROM Location l")
    List<Product> findProductsInUse();
}
