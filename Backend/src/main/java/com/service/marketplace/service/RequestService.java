package com.service.marketplace.service;

import com.service.marketplace.dto.request.RequestToCreateDto;
import com.service.marketplace.dto.response.RequestResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RequestService {
    List<RequestResponse> getAllRequests();

    RequestResponse getRequestById(Integer requestId);

    RequestResponse createRequest(RequestToCreateDto requestToCreate);

    RequestResponse updateRequest(Integer requestId, RequestToCreateDto requestToUpdate);

    void deleteRequestById(Integer requestId);

    List<RequestResponse> getRequestsByProvider();

    ResponseEntity<String> cancelRequest(RequestToCreateDto requestDto, Integer requestId);
}