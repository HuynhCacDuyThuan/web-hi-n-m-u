package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.example.demo.model.JwtUtil;
import com.example.demo.model.BloodDonationHistory;
import com.example.demo.model.Contract.BloodDonation;
import com.example.demo.model.Event;
import com.example.demo.service.BlockchainService;
import com.example.demo.service.EventService;
import com.example.demo.service.UserService;

@Controller
public class HistorybloodController {
	private final BlockchainService blockchainService;
	private final UserService userService;

	@Autowired
	private EventService eventService;

	public HistorybloodController(BlockchainService blockchainService, UserService userService) {
		this.blockchainService = blockchainService;
		this.userService = userService;
	}

	@GetMapping("/history-blood")
	public String history(@SessionAttribute(value = "username", required = false) String username, Model model) {

		// Kiểm tra xem người dùng có đăng nhập hay không
		boolean isLoggedIn = username != null;
		List<BloodDonationHistory> bloodDonationHistories = new ArrayList<>();

		try {
			// Retrieve donations directly as BloodDonation objects from the blockchain
			// service
			List<BloodDonation> donations = blockchainService.getDonationsByName(username);

			// Process each donation to retrieve event details and create a history record
			for (BloodDonation donation : donations) {
				Long id_blood = donation.getIdBlood().longValue();
				LocalDate registrationDate = LocalDate.parse(donation.getRegisteredDate());

				// Fetch events associated with the specific donation
				List<Event> events = eventService.getEventsById(id_blood);

				for (Event event : events) {
					BloodDonationHistory history = convertToBloodDonationHistory(donation, event);
					bloodDonationHistories.add(history);
				}
			}

			// Add donation histories to model
			model.addAttribute("bloodDonationHistories", bloodDonationHistories);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Add login status and username to model
		model.addAttribute("isLoggedIn", username != null);
		model.addAttribute("username", username);

		return "user/history-blood"; // View for history page
	}

	// Updated method to accept BloodDonation
	private BloodDonationHistory convertToBloodDonationHistory(BloodDonation donation, Event event) {
		BloodDonationHistory history = new BloodDonationHistory();
		history.setRegistrationDate(LocalDate.parse(donation.getRegisteredDate())); // Use BloodDonation's
																					// registeredDate
		history.setLocation(donation.getLocation());
		history.setStartTime(event.getStartTime());
		history.setEndTime(event.getEndTime());
		history.setStatus(donation.getStatus()); // Use BloodDonation's status
		history.setMedicalstatus(donation.medicalstatus);

		return history;
	}

}
