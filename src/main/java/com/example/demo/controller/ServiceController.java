package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import com.example.demo.service.EventService;

import org.springframework.data.domain.Pageable;

import com.example.demo.model.Event;

@Controller
public class ServiceController {

	@Autowired
	private EventService eventService;

	@GetMapping("/service")
	public String service(@SessionAttribute(value = "username", required = false) String username,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "6") int size, Model model) {

		// Check if the user is logged in
		boolean isLoggedIn = username != null;
		model.addAttribute("isLoggedIn", isLoggedIn);

		// Fetch events with pagination
		Pageable pageable = PageRequest.of(page, size);
		Page<Event> eventPage = eventService.getAllEvents(pageable);

		// Add the event page and other pagination details to the model
		model.addAttribute("events", eventPage.getContent()); // Events on the current page
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", eventPage.getTotalPages());
		model.addAttribute("totalItems", eventPage.getTotalElements());

		// Add username if logged in
		if (isLoggedIn) {
			model.addAttribute("username", username);
		}

		return "user/service"; // Return the service view
	}

}
