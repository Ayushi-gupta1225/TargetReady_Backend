package com.target.planogram.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int productRow;
    private int productSection;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "planogram_id")
    private Planogram planogram;
}
