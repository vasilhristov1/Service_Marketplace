package com.service.marketplace.service;

import com.service.marketplace.dto.request.CityRequest;
import com.service.marketplace.dto.response.CityResponse;

import java.util.List;

public interface CityService {
    List<CityResponse> getAllCities();

    CityResponse getCityById(Integer cityId);

    CityResponse createCity(CityRequest cityToCreate);

    CityResponse updateCity(Integer cityId, CityRequest cityToUpdate);

    void deleteCityById(Integer cityId);
}
