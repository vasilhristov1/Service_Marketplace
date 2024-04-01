package com.service.marketplace.controller;

import com.service.marketplace.dto.request.OfferRequest;
import com.service.marketplace.dto.response.OfferResponse;
import com.service.marketplace.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/offer")
public class OfferController {
    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<OfferResponse>> getAllOffers() {
        List<OfferResponse> offers = offerService.getAllOffer();
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<OfferResponse> getOfferById(@PathVariable("offerId") Integer offerId) {
        OfferResponse offerResponse = offerService.getOfferById(offerId);

        if (offerResponse != null) {
            return ResponseEntity.ok(offerResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<OfferResponse> createOffer(@RequestBody OfferRequest offerToCreate) {
        OfferResponse offerResponse = offerService.createOffer(offerToCreate);
        return ResponseEntity.ok(offerResponse);
    }

    @DeleteMapping("/{offerId}")
    public ResponseEntity<Void> deleteRequest(@PathVariable("offerId") Integer offerId) {
        offerService.deleteOfferById(offerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/currentUser")
    public ResponseEntity<List<OfferResponse>> getOfferByCustomer() {
        List<OfferResponse> offers = offerService.getOfferByCustomer();
        return ResponseEntity.ok(offers);
    }

    @PostMapping("/cancel/{offerId}")
    public ResponseEntity<String> cancelOffer(@RequestBody OfferRequest offerRequest, @PathVariable("offerId") Integer offerId) {
        return offerService.cancelOffer(offerRequest, offerId);
    }
}
