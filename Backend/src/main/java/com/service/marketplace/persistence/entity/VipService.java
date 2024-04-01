package com.service.marketplace.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "vip_services")
public class VipService extends BaseEntity {
    @JoinColumn(name = "service_id")
    @OneToOne
    private Service service;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "stripe_id", nullable = false)
    private String stripeId;


}
