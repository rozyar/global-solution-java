package com.example.solarpanelcalculator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.solarpanelcalculator.model.Appliance;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ApplianceRepository extends JpaRepository<Appliance, Long> {
    Page<Appliance> findByUserId(Long userId, Pageable pageable);
    List<Appliance> findByUserId(Long userId);

}
