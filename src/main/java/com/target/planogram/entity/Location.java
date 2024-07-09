package com.target.planogram.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "product_row", nullable = false)
    private int productRow;

    @Column(name = "product_section", nullable = false)
    private int productSection;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "planogram_id", nullable = false)
    private Planogram planogram;

    @Column(name = "slot_index", nullable = false)
    private int index;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}


