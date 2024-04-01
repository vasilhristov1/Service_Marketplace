package com.service.marketplace.controller;

import com.service.marketplace.dto.request.CityRequest;
import com.service.marketplace.dto.response.CityResponse;
import com.service.marketplace.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cities")
public class CityController {
    private final CityService cityService;

    @GetMapping("/all")
    public ResponseEntity<List<CityResponse>> getAllCities() {
        List<CityResponse> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<CityResponse> getCityById(@PathVariable("cityId") Integer cityId) {
        CityResponse city = cityService.getCityById(cityId);

        if (city != null) {
            return ResponseEntity.ok(city);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<CityResponse> createCity(@RequestBody CityRequest cityToCreate) {
        CityResponse newCity = cityService.createCity(cityToCreate);
        return ResponseEntity.ok(newCity);
    }

    @PutMapping("/update/{cityId}")
    public ResponseEntity<CityResponse> updateCity(@PathVariable("cityId") Integer cityId, @RequestBody CityRequest cityToUpdate) {
        CityResponse updatedCity = cityService.updateCity(cityId, cityToUpdate);

        if (updatedCity != null) {
            return ResponseEntity.ok(updatedCity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{cityId}")
    public ResponseEntity<Void> deleteCity(@PathVariable("cityId") Integer cityId) {
        cityService.deleteCityById(cityId);
        return ResponseEntity.ok().build();
    }
}
