package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.MedicalCondition;

public interface MedicalConditionRepository extends JpaRepository<MedicalCondition, Long> {
}
