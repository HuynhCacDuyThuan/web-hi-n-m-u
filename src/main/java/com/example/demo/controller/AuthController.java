package com.example.demo.controller;

import com.example.demo.exception.AccountLockedException;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.request.LoginRequest;
import com.example.demo.request.LoginResponse;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse; // Correct import for Jakarta Servlet API

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	/**
	 * 
	 * @param loginRequest
	 * @param response
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		try {
			LoginResponse loginResponse = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

			Cookie tokenCookie = new Cookie("authToken", loginResponse.getToken());
			tokenCookie.setHttpOnly(true);
			tokenCookie.setMaxAge(60 * 60 * 24);
			tokenCookie.setPath("/");

			response.addCookie(tokenCookie);

			return ResponseEntity.ok(loginResponse);
		} catch (UserNotFoundException | InvalidPasswordException e) {
			// Return a 401 Unauthorized with a specific error message
			return ResponseEntity.status(401).body(e.getMessage());
		} catch (AccountLockedException e) {
			// Return a 403 Forbidden with a specific error message
			return ResponseEntity.status(403).body(e.getMessage());
		} catch (Exception e) {
			// Handle other unexpected exceptions
			return ResponseEntity.status(500).body(  e.getMessage());
		}
	}
}
