package com.example.demo.service;


import com.example.demo.model.MedicalCondition;
import com.example.demo.repository.MedicalConditionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalConditionService {

    private final MedicalConditionRepository repository;

    public MedicalConditionService(MedicalConditionRepository repository) {
        this.repository = repository;
    }

    /**
     * Get all medical conditions from the database.
     *
     * @return List of all medical conditions
     */
    public List<MedicalCondition> getAllMedicalConditions() {
        return repository.findAll();
    }
}
