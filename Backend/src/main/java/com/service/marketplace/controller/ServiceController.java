package com.service.marketplace.controller;

import com.service.marketplace.dto.request.ServiceFilterRequest;
import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
import com.service.marketplace.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/services")
public class ServiceController {
    private final ServiceService serviceService;

    @GetMapping("/all")
    public ResponseEntity<List<ServiceResponse>> getAllServices() {
        List<ServiceResponse> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable("serviceId") Integer serviceId) {
        ServiceResponse service = serviceService.getServiceById(serviceId);
        if (service != null) {
            return ResponseEntity.ok(service);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ServiceResponse>> getServicesByUserId(@PathVariable("userId") Integer userId) {
        List<ServiceResponse> userServices = serviceService.getServicesByUserId(userId);
        return ResponseEntity.ok(userServices);
    }

    @GetMapping("/user/current")
    public ResponseEntity<List<ServiceResponse>> getServicesByCurrentUser() {
        List<ServiceResponse> userServices = serviceService.getServicesByCurrentUser();
        return ResponseEntity.ok(userServices);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ServiceResponse>> getServicesByCategory(@PathVariable("categoryId") Integer categoryId) {
        List<ServiceResponse> categoryServices = serviceService.getServicesByCategory(categoryId);
        return ResponseEntity.ok(categoryServices);
    }

    @PostMapping("/create")
    public ResponseEntity<ServiceResponse> createService(@ModelAttribute ServiceRequest serviceToCreate, @RequestParam(value = "files", required = false) MultipartFile[] files) {
        ServiceResponse newService = serviceService.createService(serviceToCreate, files);
        return ResponseEntity.ok(newService);
    }

    @PutMapping("/update/{serviceId}")
    public ResponseEntity<ServiceResponse> updateService(@PathVariable("serviceId") Integer serviceId, @RequestBody ServiceRequest serviceToUpdate) {
        ServiceResponse updatedService = serviceService.updateService(serviceId, serviceToUpdate);
        if (updatedService != null) {
            return ResponseEntity.ok(updatedService);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{serviceId}")
    public ResponseEntity<Void> deleteService(@PathVariable("serviceId") Integer serviceId) {
        serviceService.deleteServiceById(serviceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public Page<ServiceResponse> getFilteredServices(ServiceFilterRequest serviceFilterRequest) {
        return serviceService.filterServices(serviceFilterRequest);
    }

    @GetMapping("/getPaginationServices/{page}/{pageSize}/{sortingField}/{sortingDirection}")
    public Page<ServiceResponse> getServices(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize,
                                             @PathVariable("sortingField") String sortingField, @PathVariable("sortingDirection") String sortingDirection) {
        return serviceService.fetchServices(page, pageSize, sortingField, sortingDirection);
    }
}
