package com.example.demo.controller;

import com.example.demo.dto.VolunteerDTO;
import com.example.demo.service.CertificateService;
import com.example.demo.service.VolunteerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/admin/volunteer")
public class CertificateController {

	@Autowired
	private CertificateService certificateService;

	@Autowired
	private VolunteerService volunteerService;

	@GetMapping("/issueCertificate/{id}")
	public ResponseEntity<String> issueCertificate(@PathVariable BigInteger id) {
		try {
			VolunteerDTO volunteer = volunteerService.getVolunteerById(id);
			if (volunteer == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Volunteer not found for ID: " + id);
			}
			LocalDate currentDate = LocalDate.now();

			// Format the date as "Ngày dd tháng MM năm yyyy"
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Ngày' dd 'tháng' MM 'năm' yyyy");
			String formattedDate = currentDate.format(formatter);

			certificateService.issueCertificate(volunteer, volunteer.getEmail());
			volunteerService.issueCertification(id);
			return ResponseEntity.ok("Certificate issued and emailed successfully.");
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error issuing certificate: " + e.getMessage());
		}
	}

}
