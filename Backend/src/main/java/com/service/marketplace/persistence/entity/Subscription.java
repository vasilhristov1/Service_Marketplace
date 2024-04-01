package com.service.marketplace.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "subscription")
public class Subscription extends BaseEntity {

    @JoinColumn(name = "user_id")
    @OneToOne
    private User user;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "stripe_id", nullable = false)
    private String stripeId;

    @Column(name = "is_cancelled")
    private boolean isCancelled = false;
}
