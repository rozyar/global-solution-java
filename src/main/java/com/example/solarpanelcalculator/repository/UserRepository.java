package com.example.solarpanelcalculator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solarpanelcalculator.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
