package com.service.marketplace.persistence.repository;

import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.entity.City;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.enums.ServiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
    List<Service> findByCategory(Category category);

    Page<Service> findByServiceStatus(ServiceStatus serviceStatus, Pageable pageable);

    List<Service> findByProvider(User user);

    @Query("SELECT s FROM Service s JOIN s.cities c WHERE c IN :cities")
    List<Service> findByCitiesUsingQuery(@Param("cities") List<City> cities);

    @Query("SELECT s FROM Service s " +
            "WHERE s.serviceStatus = 'ACTIVE' " +
            "AND (:minPrice IS NULL OR s.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR s.price <= :maxPrice) " +
            "AND (:categoryIds IS NULL OR s.category.id IN :categoryIds) " +
            "AND (:cityIds IS NULL OR EXISTS (SELECT c FROM s.cities c WHERE c.id IN :cityIds))")
    Page<Service> filterServices(@Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice,
                                 @Param("categoryIds") List<Integer> categoryIds,
                                 @Param("cityIds") List<Integer> cityIds,
                                 Pageable pageable);

}
