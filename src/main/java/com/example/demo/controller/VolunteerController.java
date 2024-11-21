package com.example.demo.controller;

import com.example.demo.dto.VolunteerDTO;
import com.example.demo.dto.VolunteerResponseDTO;
import com.example.demo.service.BloodDonationService;
import com.example.demo.service.VolunteerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {

	private final BloodDonationService bloodDonationService;
	@Autowired
	private VolunteerService volunteerService;

	@Autowired
	public VolunteerController(BloodDonationService bloodDonationService) {
		this.bloodDonationService = bloodDonationService;
	}

	// Endpoint to get all volunteers
	@GetMapping
	public ResponseEntity<List<VolunteerDTO>> getAllVolunteers() {
		try {
			// Call the service method to get the list of volunteers
			List<VolunteerDTO> volunteers = bloodDonationService.volunteerDTOs();

			// Return the list as the response entity
			return ResponseEntity.ok(volunteers);
		} catch (Exception e) {
			// Log the error (optional) and return a server error response
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/{donationId}")
	public ResponseEntity<VolunteerResponseDTO> getVolunteerByDonationId(@PathVariable BigInteger donationId) {
	    LocalDate currentDate = LocalDate.now();

	    // Format the date as "Ngày dd tháng MM năm yyyy"
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Ngày' dd 'tháng' MM 'năm' yyyy");
	    String formattedDate = currentDate.format(formatter);

	    try {
	        VolunteerDTO volunteerDTO = volunteerService.getVolunteerById(donationId);

	        if (volunteerDTO == null) {
	            // If no volunteer found, return 404 status
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }

	        // Wrap VolunteerDTO and formattedDate in VolunteerResponseDTO
	        VolunteerResponseDTO responseDTO = new VolunteerResponseDTO(volunteerDTO, formattedDate);

	        // Return the responseDTO wrapped in a ResponseEntity with 200 status
	        return ResponseEntity.ok(responseDTO);
	    } catch (Exception e) {
	        // Log exception details and return a 500 error for unexpected cases
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}

}
