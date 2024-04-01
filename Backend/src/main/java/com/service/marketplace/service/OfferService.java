package com.service.marketplace.service;

import com.service.marketplace.dto.request.OfferRequest;
import com.service.marketplace.dto.response.OfferResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OfferService {

    List<OfferResponse> getAllOffer();

    OfferResponse getOfferById(Integer offerId);

    OfferResponse createOffer(OfferRequest offerToCreate);

    void deleteOfferById(Integer offerId);

    List<OfferResponse> getOfferByCustomer();

    ResponseEntity<String> cancelOffer(OfferRequest offerRequest, Integer offerId);

    OfferResponse updateOffer(Integer offerId, OfferRequest offerToUpdate);
}
