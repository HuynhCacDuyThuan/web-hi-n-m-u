package com.example.demo.controller;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.example.demo.model.Users;
import com.example.demo.service.BlockchainService;
import com.example.demo.service.UserService;

@Controller
public class ProfileController {
	@Autowired
	BlockchainService blockchainService;
	@Autowired
	UserService userService;

	@GetMapping("/profile")
	public String profile(@SessionAttribute(value = "username", required = false) String username,
	                      @SessionAttribute(value = "email", required = false) String email,
	                      @SessionAttribute(value = "address", required = false) String address,
	                      @SessionAttribute(value = "cccd", required = false) String cccd,
	                      @SessionAttribute(value = "bloodGroup", required = false) String bloodGroup,
	                      @SessionAttribute(value = "gender", required = false) String gender, Model model) {

	    // Kiểm tra xem người dùng có đăng nhập hay không
	    if (username == null || email == null) {
	        return "redirect:/login"; // Nếu chưa đăng nhập, chuyển hướng tới trang login
	    }

	    Users users = userService.findByEmail(email);

	    // Thêm các thông tin vào model
	    model.addAttribute("isLoggedIn", true);
	    model.addAttribute("username", username);
	    model.addAttribute("email", email);
	    model.addAttribute("address", address);
	    model.addAttribute("cccd", cccd);
	    model.addAttribute("bloodGroup", bloodGroup);
	    model.addAttribute("gender", gender);
	    model.addAttribute("image", users.getImg());

	    return "user/profile"; // Trả về trang profile
	}

	@GetMapping("/delete-user")
	public ResponseEntity<String> deleteUser(@SessionAttribute(value = "id", required = false) String id,
			@SessionAttribute(value = "email", required = false) String email) {
		if (id == null) {
			System.out.println("id" + id);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: User ID ");
		}
		if (email == null) {
			System.out.println("id" + email);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: User email is missing in session.");
		}

		try {
			// Convert the id from String to BigInteger
			BigInteger userId = new BigInteger(id);
			String response = blockchainService.deleteUser(userId);

			if (response.startsWith("Error")) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Blockchain deletion failed: " + response);
			}

			Users user = userService.findByEmail(email);
			userService.deleteUserById(user.getId());

			return ResponseEntity.ok("User deleted successfully");

		} catch (NumberFormatException e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Invalid user ID format.");
		}
	}

}
