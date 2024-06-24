package com.target.planogram.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShelfOccupancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long occupancyId;

    private int shelfId;

    @ManyToOne
    @JoinColumn(name = "planogram_id")
    private Planogram planogram;

    private int occupancy;
}
