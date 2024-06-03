package com.target.planogram.repository;

import com.target.planogram.entity.ShelfOccupancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelfOccupancyRepository extends JpaRepository<ShelfOccupancy, Integer> {
}

