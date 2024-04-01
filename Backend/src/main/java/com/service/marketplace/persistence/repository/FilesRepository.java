package com.service.marketplace.persistence.repository;

import com.service.marketplace.persistence.entity.Files;
import com.service.marketplace.persistence.entity.Review;
import com.service.marketplace.persistence.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilesRepository extends JpaRepository<Files, Integer> {
    List<Files> findByService(Service service);

    List<Files> findByReview(Review review);
}
