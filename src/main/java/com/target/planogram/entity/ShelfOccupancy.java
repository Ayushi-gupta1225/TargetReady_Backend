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
    private int shelfId;
    private int occupancy;
}
