package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ContacController {
	@GetMapping("/contact")
	public String contact() {
		return "user/contact";
	}
}
