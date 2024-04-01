package com.service.marketplace.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "city")
public class City extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "zip_code", nullable = false)
    private int zipCode;

    @Column(name = "address", nullable = false)
    private String address;

}
