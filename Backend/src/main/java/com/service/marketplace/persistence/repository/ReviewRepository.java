package com.service.marketplace.persistence.repository;

import com.service.marketplace.persistence.entity.Review;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByService(Service service);

    List<Review> findByCustomer(User user);
}
