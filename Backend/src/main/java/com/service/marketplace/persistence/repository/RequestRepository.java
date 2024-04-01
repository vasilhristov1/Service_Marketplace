package com.service.marketplace.persistence.repository;

import com.service.marketplace.persistence.entity.Request;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByService(Service service);

    List<Request> findByCustomer(User user);
}