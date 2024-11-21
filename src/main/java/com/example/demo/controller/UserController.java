package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BloodDonationService;
import com.example.demo.service.UserService;

import io.jsonwebtoken.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Value("${upload.path}")
	private String uploadDir;
	@Autowired
	private UserService userService;
	@Autowired
	BloodDonationService bloodDonationService;
	@Autowired
	private UserRepository userRepository;

	@PutMapping("/lock/{id}")
	public ResponseEntity<String> toggleUserLockStatus(@PathVariable Long id) {
		try {
			boolean newLockStatus = userService.toggleUserLockStatus(id);
			String statusMessage = newLockStatus ? "User is now locked." : "User is now unlocked.";
			return ResponseEntity.ok(statusMessage);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error: " + e.getMessage());
		}
	}

	@PostMapping("/avatar")
	public ResponseEntity<String> uploadAvatar(@RequestParam("image") MultipartFile file,
			@SessionAttribute(value = "email", required = false) String email) throws java.io.IOException {
		if (email == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
		}

		try {
			// Ensure the upload directory exists
			Files.createDirectories(Paths.get(uploadDir));

			// Fetch user based on email
			Users user = userService.findByEmail(email);
			if (user == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
			}

			// Generate a unique filename using user ID and original file name
			String filename = user.getId() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
			Path filepath = Paths.get(uploadDir, filename);
			Files.write(filepath, file.getBytes());

			// Update the user's img field in the database
			user.setImg(filename); // Store only the filename, not the full path
			userRepository.save(user);

			return ResponseEntity.ok("Avatar updated successfully!");

		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload avatar");
		}
	}

}
