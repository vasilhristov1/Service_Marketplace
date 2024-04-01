package com.service.marketplace.persistence.repository;

import com.service.marketplace.persistence.entity.VipService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VipServiceRepository extends JpaRepository<VipService, Integer> {
}