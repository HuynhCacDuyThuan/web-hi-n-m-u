package com.example.demo.controller;

import java.math.BigInteger;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BloodDonationDTO;
import com.example.demo.dto.BloodGroupStatisticsDto;
import com.example.demo.model.Contract.BloodDonation;
import com.example.demo.service.BlockchainService;

@RestController
@RequestMapping("/api/statistics")
public class BloodGroupStatisticsController {

	private final BlockchainService blockchainService;
	
	public BloodGroupStatisticsController(BlockchainService blockchainService) {
		this.blockchainService = blockchainService;
	}

	@GetMapping("/blood-groups")
	public ResponseEntity<BloodGroupStatisticsDto> getBloodGroupStatistics() {
		try {
			// Gọi service để lấy dữ liệu thống kê nhóm máu
			BloodGroupStatisticsDto statistics = blockchainService.getBloodGroupStatistics();
			return ResponseEntity.ok(statistics); // Trả về dữ liệu với mã trạng thái 200 OK
		} catch (Exception e) {
			e.printStackTrace();
			// Nếu có lỗi, trả về mã trạng thái 500 Internal Server Error
			return ResponseEntity.status(500).body(null);
		}
	}
	

	@GetMapping("/details/{donationId}")
    public ResponseEntity<BloodDonationDTO> getBloodDonation(@PathVariable BigInteger donationId) {
        BloodDonationDTO donation = blockchainService.getBloodDonation(donationId);

        if (donation != null) {
            return ResponseEntity.ok(donation); // Trả về chi tiết của lần hiến máu
        } else {
            return ResponseEntity.notFound().build(); // Nếu không tìm thấy, trả về 404
        }
    }
}
