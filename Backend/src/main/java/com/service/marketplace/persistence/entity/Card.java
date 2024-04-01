package com.service.marketplace.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "cards")
public class Card extends BaseEntity {
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @Column(name = "card_token", nullable = false)
    private String cardToken;

    @Column(name = "isDeleted", nullable = false)
    private boolean isDeleted = false;
}
