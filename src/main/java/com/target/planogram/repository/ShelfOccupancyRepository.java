package com.target.planogram.repository;

import com.target.planogram.entity.ShelfOccupancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

public interface ShelfOccupancyRepository extends JpaRepository<ShelfOccupancy, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ShelfOccupancy s WHERE s.planogram.planogramId = :planogramId")
    void deleteByPlanogramId(@Param("planogramId") Long planogramId);
}
