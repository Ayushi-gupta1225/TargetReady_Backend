package com.target.planogram.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Planogram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planogramId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "num_sections")
    private int numSections;

    @Column(nullable = false, name = "num_shelves")
    private int numShelves;

    @Column(nullable = false, name = "slot_height")
    private int slotHeight;

    @Column(nullable = false, name = "slot_width")
    private int slotWidth;
}
