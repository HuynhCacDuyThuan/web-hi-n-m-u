package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.BloodGroup;
import com.example.demo.service.BloodGroupService;

@Controller
public class RegisterController {
	@Autowired
	private BloodGroupService bloodGroupService;

	@GetMapping("/register")
	public String register(Model model) {
		List<BloodGroup> bloodGroups = bloodGroupService.getAllBloodGroups();
		model.addAttribute("bloodGroups", bloodGroups);
		return "user/register";
	}
}
