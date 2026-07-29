package com.ritish.ai_calling_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ritish.ai_calling_system.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByPhone(String phone);
}
