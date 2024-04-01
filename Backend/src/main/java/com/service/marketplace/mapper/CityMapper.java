package com.service.marketplace.mapper;

import com.service.marketplace.dto.request.CityRequest;
import com.service.marketplace.dto.response.CityResponse;
import com.service.marketplace.persistence.entity.City;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CityMapper {
    City cityRequestToCity(CityRequest request);

    CityResponse cityToCityResponse(City city);

    List<CityResponse> toCityResponseList(List<City> cities);
}
