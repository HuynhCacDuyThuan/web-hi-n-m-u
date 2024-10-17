package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
	@GetMapping("/admin/index")
	public String getIndexAdmin() {
		return "admin/index";
	}

	@GetMapping("/admin/users")
	public String getUserAdmin() {
		return "admin/users";
	}

	@GetMapping("/admin/blood")
	public String getFloodAdmin() {
		return "admin/flood";
	}

	@GetMapping("/admin/event-blood")
	public String getEventFloodAdmin() {
		return "admin/EventFlood";
	}

	@GetMapping("/admin/statistics")
	public String getStatisticsAdmin() {
		return "admin/statistics";
	}

	@GetMapping("/admin/notifications")
	public String getNotificationsAdmin() {
		return "admin/notifications";
	}

	@GetMapping("/admin/volunteer")
	public String getVolunteerAdmin() {
		return "admin/volunteer";
	}

}
