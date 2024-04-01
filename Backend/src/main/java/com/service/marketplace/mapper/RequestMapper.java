package com.service.marketplace.mapper;

import com.service.marketplace.dto.request.RequestToCreateDto;
import com.service.marketplace.dto.response.RequestResponse;
import com.service.marketplace.persistence.entity.Request;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "service", source = "service")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Request requestRequestToRequest(RequestToCreateDto request, User customer, Service service);

    @Mapping(target = "customerId", source = "request.customer.id")
    @Mapping(target = "serviceId", source = "request.service.id")
    RequestResponse requestToRequestResponse(Request request);

    List<RequestResponse> toRequestResponseList(List<Request> requests);

    void requestFromRequest(RequestToCreateDto requestRequest, @MappingTarget Request request);

}