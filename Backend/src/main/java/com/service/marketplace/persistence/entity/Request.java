package com.service.marketplace.persistence.entity;

import com.service.marketplace.persistence.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "request")
public class Request extends BaseEntity {

    @Column(name = "description", nullable = false)
    private String description;

    @JoinColumn(name = "customer_id", nullable = true)
    @ManyToOne
    private User customer;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @Column(name = "request_status")
    @Enumerated(EnumType.STRING)
    private OfferStatus requestStatus;

    @JoinColumn(name = "service_id", nullable = false)
    @ManyToOne
    private Service service;
}
