package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.CityRequest;
import com.service.marketplace.dto.response.CityResponse;
import com.service.marketplace.mapper.CityMapper;
import com.service.marketplace.persistence.entity.City;
import com.service.marketplace.persistence.repository.CityRepository;
import com.service.marketplace.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public List<CityResponse> getAllCities() {
        List<City> cities = cityRepository.findAll();

        return cityMapper.toCityResponseList(cities);
    }

    @Override
    public CityResponse getCityById(Integer cityId) {
        City city = cityRepository.findById(cityId).orElse(null);

        return (city != null) ? cityMapper.cityToCityResponse(city) : null;
    }

    @Override
    public CityResponse createCity(CityRequest cityToCreate) {
        City newCity = cityMapper.cityRequestToCity(cityToCreate);

        return cityMapper.cityToCityResponse(cityRepository.save(newCity));
    }

    @Override
    public CityResponse updateCity(Integer cityId, CityRequest cityToUpdate) {
        City existingCity = cityRepository.findById(cityId).orElse(null);

        City updatedCity = cityMapper.cityRequestToCity(cityToUpdate);

        if (existingCity != null) {
            existingCity.setName(updatedCity.getName());
            existingCity.setZipCode(updatedCity.getZipCode());
            existingCity.setAddress(updatedCity.getAddress());

            return cityMapper.cityToCityResponse(cityRepository.save(existingCity));
        } else {
            return null;
        }
    }

    @Override
    public void deleteCityById(Integer cityId) {
        cityRepository.deleteById(cityId);
    }
}
