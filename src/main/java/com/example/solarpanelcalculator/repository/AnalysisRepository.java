package com.example.solarpanelcalculator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.solarpanelcalculator.model.Analysis;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    Page<Analysis> findByUserId(Long userId, Pageable pageable);
    List<Analysis> findByUserId(Long userId, Sort sort);
}
