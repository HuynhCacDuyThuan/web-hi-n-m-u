package com.example.demo.controller;


import com.example.demo.dto.BloodDonationDTO;
import com.example.demo.service.BloodDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/blood-donations")
public class BloodDonationController {

    private final BloodDonationService bloodDonationService;

    @Autowired
    public BloodDonationController(BloodDonationService bloodDonationService) {
        this.bloodDonationService = bloodDonationService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BloodDonationDTO>> getAllBloodDonations() {
        try {
            // Retrieve the list of blood donations from the service
            List<BloodDonationDTO> bloodDonations = bloodDonationService.getAllBloodDonations();
            return ResponseEntity.ok(bloodDonations);
        } catch (Exception e) {
            // Log the error and return a 500 Internal Server Error
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
