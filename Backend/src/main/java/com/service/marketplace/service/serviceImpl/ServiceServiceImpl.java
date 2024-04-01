package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.FilesRequest;
import com.service.marketplace.dto.request.ServiceFilterRequest;
import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
import com.service.marketplace.mapper.ServiceMapper;
import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.entity.City;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.enums.ServiceStatus;
import com.service.marketplace.persistence.repository.CategoryRepository;
import com.service.marketplace.persistence.repository.CityRepository;
import com.service.marketplace.persistence.repository.ServiceRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.FilesService;
import com.service.marketplace.service.ServiceService;
import com.service.marketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final FilesService filesService;

    @Override
    public List<ServiceResponse> getAllServices() {
        List<com.service.marketplace.persistence.entity.Service> services = serviceRepository.findAll();

        return serviceMapper.toServiceResponseList(services);
    }

    @Override
    public ServiceResponse getServiceById(Integer serviceId) {
        com.service.marketplace.persistence.entity.Service service = serviceRepository.findById(serviceId).orElse(null);

        if (service != null) {
            return serviceMapper.serviceToServiceResponse(service);
        } else {
            return null;
        }
    }

    @Override
    public ServiceResponse createService(ServiceRequest serviceToCreate, MultipartFile[] files) {
        List<City> cities = cityRepository.findAllById(serviceToCreate.getCityIds());
        User provider = userRepository.findById(serviceToCreate.getProviderId()).orElse(null);
        Category category = categoryRepository.findById(serviceToCreate.getCategoryId()).orElse(null);

        com.service.marketplace.persistence.entity.Service newService = serviceMapper.serviceRequestToService(serviceToCreate, provider, category, cities);

        ServiceResponse serviceResponse = serviceMapper.serviceToServiceResponse(serviceRepository.save(newService));

        if (files != null) {
            for (MultipartFile multipartFile : files) {
                FilesRequest filesRequest = new FilesRequest(multipartFile, serviceResponse.getId(), null);
                filesService.createFile(filesRequest);
            }
        }

        return serviceResponse;
    }

    @Override
    public ServiceResponse updateService(Integer serviceId, ServiceRequest serviceToUpdate) {
        com.service.marketplace.persistence.entity.Service existingService = serviceRepository.findById(serviceId).orElse(null);
        Category category = categoryRepository.findById(serviceToUpdate.getCategoryId()).orElse(null);
        List<City> cities = cityRepository.findAllById(serviceToUpdate.getCityIds());

        com.service.marketplace.persistence.entity.Service updatedService = serviceMapper.serviceRequestToService(serviceToUpdate, category, cities);

        if (existingService != null) {
            existingService.setTitle(updatedService.getTitle());
            existingService.setDescription(updatedService.getDescription());
            existingService.setServiceStatus(updatedService.getServiceStatus());
            existingService.setPrice(updatedService.getPrice());
            existingService.setCategory(updatedService.getCategory());
            existingService.setCities(updatedService.getCities());
            existingService.setUpdatedAt(updatedService.getUpdatedAt());

            return serviceMapper.serviceToServiceResponse(serviceRepository.save(existingService));
        } else {
            return null;
        }
    }

    @Override
    public void deleteServiceById(Integer serviceId) {
        serviceRepository.deleteById(serviceId);
    }

    @Override
    public Page<ServiceResponse> fetchServices(Integer page, Integer pageSize, String sortingField, String sortingDirection) {
        Sort sort = Sort.by(Sort.Direction.valueOf(sortingDirection), sortingField);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        return serviceMapper.toServiceResponsePage(serviceRepository.findByServiceStatus(ServiceStatus.ACTIVE, pageable));
    }

    @Override
    public Page<ServiceResponse> filterServices(ServiceFilterRequest serviceFilterRequest) {
        Pageable pageable = PageRequest.of(serviceFilterRequest.getPage(), serviceFilterRequest.getPageSize(), Sort.by(Sort.Direction.fromString(serviceFilterRequest.getSortingDirection()), serviceFilterRequest.getSortingField()));
        List<Integer> categoryIds = serviceFilterRequest.getCategoryIds();
        List<Integer> cityIds = serviceFilterRequest.getCityIds();

        Page<com.service.marketplace.persistence.entity.Service> filteredServices = serviceRepository.filterServices(
                serviceFilterRequest.getMinPrice(),
                serviceFilterRequest.getMaxPrice(),
                categoryIds.isEmpty() ? null : categoryIds,
                cityIds.isEmpty() ? null : cityIds,
                pageable
        );

        return serviceMapper.toServiceResponsePage(filteredServices);
    }


    @Override
    public List<ServiceResponse> getServicesByUserId(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            List<com.service.marketplace.persistence.entity.Service> userServices = serviceRepository.findByProvider(user);

            return serviceMapper.toServiceResponseList(userServices);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<ServiceResponse> getServicesByCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);

        if (category != null) {
            List<com.service.marketplace.persistence.entity.Service> categoryServices = serviceRepository.findByCategory(category);

            return serviceMapper.toServiceResponseList(categoryServices);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<ServiceResponse> getServicesByCurrentUser() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Collections.emptyList();
        }

        List<com.service.marketplace.persistence.entity.Service> userServices = serviceRepository.findByProvider(currentUser);
        return serviceMapper.toServiceResponseList(userServices);
    }

    @Override
    public void makeServicesInactive(Integer providerId) {
        User user = userRepository.findById(providerId).orElseThrow();
        List<com.service.marketplace.persistence.entity.Service> services = serviceRepository.findByProvider(user);

        for (com.service.marketplace.persistence.entity.Service service : services) {
            service.setServiceStatus(ServiceStatus.INACTIVE);
        }

        serviceRepository.saveAll(services);
    }

    @Override
    public void makeServicesActive(Integer providerId) {
        User user = userRepository.findById(providerId).orElseThrow();
        List<com.service.marketplace.persistence.entity.Service> services = serviceRepository.findByProvider(user);

        for (com.service.marketplace.persistence.entity.Service service : services) {
            service.setServiceStatus(ServiceStatus.ACTIVE);
        }

        serviceRepository.saveAll(services);
    }
}
