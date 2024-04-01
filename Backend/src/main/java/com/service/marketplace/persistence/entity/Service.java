package com.service.marketplace.persistence.entity;

import com.service.marketplace.persistence.enums.ServiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service")
public class Service extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "service_status")
    @Enumerated(EnumType.STRING)
    private ServiceStatus serviceStatus;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @JoinColumn(name = "provider_id")
    @ManyToOne
    private User provider;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @JoinColumn(name = "category_id")
    @ManyToOne
    private Category category;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "service_city",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "city_id")
    )
    private List<City> cities;

    @Column(name = "picture")
    private String picture;

    @Column(name = "is_vip")
    private boolean isVip;

}
