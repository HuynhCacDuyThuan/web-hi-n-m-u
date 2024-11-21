package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BloodDonationStatisticalDTO;
import com.example.demo.dto.BloodDonationStatisticsDTO;
import com.example.demo.dto.MonthlyDonationStatisticsDto;
import com.example.demo.service.BlockchainService;

@RestController
@RequestMapping("/api/statistics")
public class DonationStatisticsController {

    private final BlockchainService blockchainService;

    public DonationStatisticsController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @GetMapping("/monthly-donations")
    public ResponseEntity<MonthlyDonationStatisticsDto> getMonthlyDonationStatistics() {
        try {
            MonthlyDonationStatisticsDto statistics = blockchainService.getMonthlyDonationStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
 // Endpoint to get blood donation statistics
    @GetMapping("/blood-donations") // Endpoint URL
    public ResponseEntity<List<BloodDonationStatisticalDTO>> getBloodDonationStatistics() {
        List<BloodDonationStatisticalDTO> statistics = blockchainService.getBloodDonationStatisticalDTOs();
        return ResponseEntity.ok(statistics); // Return the list of statistics with HTTP status 200
    }
  
}
