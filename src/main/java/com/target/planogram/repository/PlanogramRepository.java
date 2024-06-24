package com.target.planogram.repository;

import com.target.planogram.entity.Planogram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanogramRepository extends JpaRepository<Planogram, Long> {}
