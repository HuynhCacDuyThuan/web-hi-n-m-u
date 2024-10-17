package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HistoryFlood {
	@GetMapping("/history-blood")
	public String history() {
		return "user/history-blood";
	}
}
