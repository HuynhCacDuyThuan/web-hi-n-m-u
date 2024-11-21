package com.example.demo.service;

import com.example.demo.model.BloodGroup;
import com.example.demo.repository.BloodGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BloodGroupService {

    @Autowired
    private BloodGroupRepository bloodGroupRepository;

    public List<BloodGroup> getAllBloodGroups() {
        return bloodGroupRepository.findAll();
    }
}
