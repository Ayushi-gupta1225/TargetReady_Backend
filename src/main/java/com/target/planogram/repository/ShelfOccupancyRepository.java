package com.target.planogram.repository;

import com.target.planogram.entity.ShelfOccupancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShelfOccupancyRepository extends JpaRepository<ShelfOccupancy, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM ShelfOccupancy s WHERE s.planogram.planogramId = :planogramId")
    void deleteByPlanogramId(@Param("planogramId") Long planogramId);

    @Query("SELECT s FROM ShelfOccupancy s WHERE s.planogram.planogramId = :planogramId AND s.shelfId = :shelfId")
    Optional<ShelfOccupancy> productSlotOccupancy(@Param("planogramId") Long planogramId, @Param("shelfId") int shelfId);

    @Query("SELECT s FROM ShelfOccupancy s WHERE s.planogram.planogramId = :planogramId AND s.shelfId = :shelfId")
    List<ShelfOccupancy> findByPlanogramAndShelfId(@Param("planogramId") Long planogramId, @Param("shelfId") int shelfId);
}

