package com.service.marketplace.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "review")
public class Review extends BaseEntity {

    @Column(name = "description")
    private String description;

    @Column(name = "rating", nullable = false)
    private double rating;

    @JoinColumn(name = "customer_id", nullable = false)
    @ManyToOne
    private User customer;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_Active")
    private boolean isActive;

    @JoinColumn(name = "service_id", nullable = false)
    @ManyToOne
    private Service service;
}
