package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Users;
import com.example.demo.exception.AccountLockedException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.JwtUtil;
import com.example.demo.model.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.LoginResponse;
import com.example.demo.request.UserDetailsResponse;
import com.example.demo.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.identityconnectors.framework.common.exceptions.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BlockchainService blockchainService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	public Users findByEmail(String email) {
		Optional<Users> optionalUser = userRepository.findByEmail(email);
		return optionalUser.orElse(null); // Returns null if user is not found
	}

	// Method to delete a user by ID
	public void deleteUserById(Long userId) {
		userRepository.deleteById(userId);
	}

	// Method to retrieve a user by ID (single user)
	public Optional<Users> getUserById(Long id) {
		return userRepository.findById(id);
	}

	public boolean toggleUserLockStatus(Long userId) {
		Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		// Toggle the lock status
		user.setLocked(!user.isLocked());

		// Save the updated user
		userRepository.save(user);

		// Return the new lock status
		return user.isLocked();
	}

	public LoginResponse login(String email, String password) {
		Optional<Users> optionalUser = userRepository.findByEmail(email);

		// Check if the user exists
		Users user = optionalUser.orElseThrow(() -> {
			logger.warn("Invalid login attempt for email: {}", email);
			return new UserNotFoundException("Email không tồn tại");
		});

		// Check if the user's account is locked
		if (user.isLocked()) {
			logger.warn("Login attempt for locked account: {}", email);
			throw new AccountLockedException("Tài khoản của bạn đã bị khoá");
		}

		// Check if the password matches
		if (!passwordEncoder.matches(password, user.getPassword())) {
			logger.warn("Invalid login attempt for email: {}", email);
			throw new InvalidPasswordException("Mật khẩu không dúng.");
		}

		// Get the user's role
		String roleName = user.getRole().getRolename();

		// Fetch additional user info from the blockchain
		UserDetailsResponse userDetails = blockchainService.getUserByEmail_Response(email);
		if (userDetails == null) {
			logger.error("Failed to retrieve user details from blockchain for email: {}", email);
			throw new RuntimeException("Failed to retrieve user details from blockchain");
		}

		// Generate the JWT
		String token = jwtUtil.generateToken(userDetails);

		// Return a response containing both the token and full user details
		return new LoginResponse(token, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(),
				userDetails.getCccd(), userDetails.getGender(), userDetails.getDateOfBirth(),
				userDetails.getBloodGroup(), roleName);
	}

}
