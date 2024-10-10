package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.NewsItem;
import com.example.demo.model.XMLParser;

@Controller

public class XMLController {
	@GetMapping("/tin-tuc")
	public String tintuc(Model model) {
		XMLParser parser = new XMLParser();
		List<NewsItem> list = parser.parseXML(); // Trả về danh sách các đối tượng NewsItem
		model.addAttribute("listTinTuc", list);
		return "user/news"; 
	}
}