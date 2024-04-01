package com.service.marketplace.mapper;

import com.service.marketplace.dto.request.OfferRequest;
import com.service.marketplace.dto.response.OfferResponse;
import com.service.marketplace.persistence.entity.Offer;
import com.service.marketplace.persistence.entity.Request;
import com.service.marketplace.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfferMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", source = "requestOffer.description")
    @Mapping(target = "offerPrice", source = "requestOffer.price")
    @Mapping(target = "provider", source = "provider")
    @Mapping(target = "request", source = "requests")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Offer OfferRequestToOffer(OfferRequest requestOffer, User provider, Request requests);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", source = "requestOffer.description")
    @Mapping(target = "offerPrice", source = "requestOffer.price")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Offer OfferRequestToOffer(OfferRequest requestOffer);

    @Mapping(target = "providerId", source = "offers.provider.id")
    @Mapping(target = "requestId", source = "offers.request.id")
    @Mapping(target = "price", source = "offers.offerPrice")
    OfferResponse offerToOfferResponse(Offer offers);

    List<OfferResponse> toOfferResponseList(List<Offer> offers);


}

