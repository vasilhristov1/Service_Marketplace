package com.service.marketplace.persistence.repository;

import com.service.marketplace.persistence.entity.Offer;
import com.service.marketplace.persistence.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {
    List<Offer> findByRequest(Request request);

}
