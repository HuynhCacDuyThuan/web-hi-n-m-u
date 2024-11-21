package com.example.demo.controller;

import com.example.demo.service.BlockchainService;
import com.example.demo.service.EventService;
import com.example.demo.service.NotificationService;
import com.example.demo.service.UserService;
import com.example.demo.model.Contract.BloodDonation;
import com.example.demo.dto.UserDto;
import com.example.demo.model.BloodDonationHistory;
import com.example.demo.model.Event;
import com.example.demo.model.JwtUtil;
import com.example.demo.model.Notification;
import com.example.demo.model.Users;
import com.example.demo.request.UserRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/blockchain")
public class BlockchainController {
	private final BlockchainService blockchainService;
	private final UserService userService;

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private EventService eventService;
	@Autowired
	NotificationService notificationService;
	private static final Logger logger = LoggerFactory.getLogger(BlockchainController.class);

	public BlockchainController(BlockchainService blockchainService, UserService userService) {
		this.blockchainService = blockchainService;
		this.userService = userService;
	}

	@GetMapping("/notifications/all")
	public ResponseEntity<List> getAllNotifications() {
		List notifications = blockchainService.fetchAllNotifications();

		if (notifications != null) {
			// Return the notifications list with a 200 OK status
			return ResponseEntity.ok(notifications);
		} else {
			// Return a 500 Internal Server Error if an error occurred
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/history-blood-json/{name}")
	public ResponseEntity<List<BloodDonationHistory>> historyJson(@PathVariable("name") String name) {
		List<BloodDonationHistory> bloodDonationHistories = new ArrayList<>();

		try {
			// Retrieve donations directly as BloodDonation objects from the blockchain
			// service
			List<BloodDonation> donations = blockchainService.getDonationsByName(name);

			// Process each donation to retrieve event details and create a history record
			for (BloodDonation donation : donations) {
				Long id_blood = donation.getIdBlood().longValue();
				LocalDate registrationDate = LocalDate.parse(donation.getRegisteredDate());

				// Fetch events associated with the specific donation
				List<Event> events = eventService.getEventsById(id_blood);

				for (Event event : events) {
					BloodDonationHistory history = convertToBloodDonationHistory(donation, event);
					bloodDonationHistories.add(history);
				}
			}

			return ResponseEntity.ok(bloodDonationHistories);

		} catch (Exception e) {
			logger.error("Error retrieving donation history by name: {}", name, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// Updated method to accept BloodDonation
	private BloodDonationHistory convertToBloodDonationHistory(BloodDonation donation, Event event) {
		BloodDonationHistory history = new BloodDonationHistory();
		history.setRegistrationDate(LocalDate.parse(donation.getRegisteredDate())); // Use BloodDonation's
																					// registeredDate
		history.setLocation(event.getLocation());
		history.setStartTime(event.getStartTime());
		history.setEndTime(event.getEndTime());
		history.setStatus(donation.getStatus()); // Use BloodDonation's status
		return history;
	}

	// Endpoint to fetch all blood donations
	@GetMapping("/all")
	public ResponseEntity<List<BloodDonation>> getAllBloodDonations() {
		try {
			// Fetch all blood donations from the blockchain
			List<BloodDonation> bloodDonations = blockchainService.getAllBloodDonations();

			// Return the list with an HTTP 200 OK status
			return ResponseEntity.ok(bloodDonations);
		} catch (Exception e) {
			// Handle any potential errors by returning a 500 Internal Server Error response
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// Endpoint to add a new blood donation
	@PostMapping("/addBloodDonation/{eventId}")
	public ResponseEntity<String> addBloodDonation(
			@CookieValue(value = "authToken", defaultValue = "") String authToken,
			@SessionAttribute(value = "username", required = false) String username,
			@SessionAttribute(value = "email", required = false) String email,
			@SessionAttribute(value = "address", required = false) String address,
			@SessionAttribute(value = "cccd", required = false) String cccd,
			@SessionAttribute(value = "bloodGroup", required = false) String bloodGroup,
			@SessionAttribute(value = "gender", required = false) String gender,
			@PathVariable("eventId") String eventId) {

		try {
			// Validate required session attributes
			if (username == null || email == null || address == null || cccd == null || bloodGroup == null
					|| gender == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required user session attributes");
			}

			// Validate authorization token presence
			if (authToken.isEmpty()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token is missing");
			}
			Long id_event = Long.parseLong(eventId);
			Optional<Event> event = eventService.findById(id_event);
			Event e1 = event.get();
			// Extract user ID from the JWT token
			Claims claims = jwtUtil.extractAllClaims(authToken);
			if (claims == null || !claims.containsKey("id")) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authorization token");
			}
			Users u = userService.findByEmail(email);

			BigInteger userId = BigInteger.valueOf(u.getId());

			// Parse the event ID as BigInteger
			BigInteger bloodId;
			try {
				bloodId = new BigInteger(eventId);
			} catch (NumberFormatException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid event ID format");
			}

			BigInteger donationDate = BigInteger.ZERO;

			// Ensure unique name and bloodId combination
			String transactionHash = blockchainService.addBloodDonation(username, donationDate, // Pass the current date
																								// as Unix timestamp
					LocalDate.now().format(DateTimeFormatter.ISO_DATE), // Registered date as a string
					BigInteger.ZERO, // Default volume, modify as needed
					bloodGroup, "Pending", userId, bloodId, e1.getLocation());

			return ResponseEntity.status(HttpStatus.CREATED)
					.body("Transaction successful with hash: " + transactionHash);

		} catch (JwtException e) {
			logger.error("JWT error while adding blood donation", e);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error in authorization token");
		} catch (Exception e) {
			logger.error("Error adding blood donation", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
		}
	}

	@GetMapping("/details/{email}")
	public ResponseEntity<UserDto> getUserByEmailDetails(@PathVariable String email) {
		try {
			UserDto user = blockchainService.getUserByEmailDetails(email);
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@PostMapping("/addUser1")
	public ResponseEntity<String> register(@RequestBody UserRequest request) {
		try {
			// Call the service to add the user and get the transaction hash
			String transactionHash = blockchainService.addUser(request.getUsername(), request.getPassword(),
					request.getEmail(), request.getCccd(), request.getGender(), request.getDayOfBirth(),
					request.getBloodGroup(), request.getAddress());

			// Prepare a notification message with the user's name
			String title = "Đăng ký người dùng mới";
			String content = "Tài khoản " + request.getUsername() + " đã đăng ký thành công.";
			String noticeDate = java.time.LocalDate.now().toString(); // or any specific date format you need

			// Call the service to add a notification on successful registration
			notificationService.createAndBroadcastNotification(title, content, noticeDate);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body("User registered successfully. Transaction hash: " + transactionHash);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}

	@GetMapping("/donations/{name}")
	public ResponseEntity<List<BloodDonation>> getDonationsByName(@PathVariable String name) {
		if (name == null || name.trim().isEmpty()) {
			return ResponseEntity.badRequest().body(null); // Trả về 400 nếu tên không hợp lệ
		}

		try {
			List<BloodDonation> donations = blockchainService.getDonationsByName(name);
			if (donations.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(donations);
			}
			return ResponseEntity.ok(donations);
		} catch (Exception e) {
			logger.error("Error retrieving donations by name: {}", name, e); // Log lỗi
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping(value = "/addUser", consumes = "multipart/form-data")
	public ResponseEntity<String> addUser(@ModelAttribute UserRequest request) {
		System.out.println("Username: " + request.getUsername());
		System.out.println("CCCD: " + request.getCccd());
		System.out.println("Gender: " + request.getGender());
		System.out.println("Day of Birth: " + request.getDayOfBirth());

		try {
			// Validate required fields directly without normalization
			if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tên người dùng không được để trống.");
			}

			String cccd = request.getCccd();
			if (cccd == null || !cccd.matches("\\b\\d{9,12}\\b")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Căn cước công dân không hợp lệ.");
			}

			if (request.getGender() == null || request.getGender().trim().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Giới tính không được để trống.");
			}

			if (request.getDayOfBirth() == null || request.getDayOfBirth().trim().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ngày sinh không được để trống.");
			}
			// Prepare a notification message with the user's name
			String title = "Đăng ký người dùng mới";
			String content = "Tài khoản" + " " + request.getUsername() + " " + "vừa tham gia";
			String noticeDate = java.time.LocalDate.now().toString(); // or any specific date format you need

			// Call the service to add a notification on successful registration
			notificationService.createAndBroadcastNotification(title, content, noticeDate);
			// Call service to add user to the system
			String transactionHash = blockchainService.addUser(request.getUsername(), request.getPassword(),
					request.getEmail(), request.getCccd(), request.getGender(), request.getDayOfBirth(),
					request.getBloodGroup(), request.getAddress());

			return ResponseEntity.status(HttpStatus.CREATED).body("Đăng kí tài khoản thành công");

		} catch (NoClassDefFoundError e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error: Missing dependency - " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = e.getMessage();

			// Handle specific conflict cases
			if (errorMessage.contains("Email already exists")) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email already exists.");
			} else if (errorMessage.contains("Username already exists")) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username already exists.");
			} else if (errorMessage.contains("CCCD already exists")) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: CCCD already exists.");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
			}
		}
	}

	// Endpoint to add a new notification
	@PostMapping("/addNotification")
	public ResponseEntity<String> addNotification(@RequestBody NotificationRequest request) {
		try {
			Notification notification = notificationService.createAndBroadcastNotification(request.getTitle(),
					request.getContent(), request.getNoticeDate());
			return ResponseEntity.status(HttpStatus.CREATED).body(notification.toString());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}

	@DeleteMapping("/delete/{id}")
	public CompletableFuture<ResponseEntity<String>> deleteBloodDonation(@PathVariable("id") String id) {
		BigInteger donationId = new BigInteger(id);

		return blockchainService.deleteBloodDonation(donationId).thenApply(transactionReceipt -> {
			String transactionHash = transactionReceipt.getTransactionHash();
			return ResponseEntity.ok("Blood donation deleted successfully with transaction hash: " + transactionHash);
		}).exceptionally(e -> {
			return ResponseEntity.status(500).body("Failed to delete blood donation: " + e.getMessage());
		});
	}

	@GetMapping("/userAll")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		try {
			List<UserDto> users = blockchainService.getAllUsers();
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(null);
		}
	}

	// Endpoint to retrieve a user by email
	@GetMapping("/getUserByEmail/{email}")
	public ResponseEntity<String> getUserByEmail(@PathVariable String email) {
		try {
			String userInfo = blockchainService.getUserByEmail(email);
			return ResponseEntity.ok(userInfo);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}

	@DeleteMapping("/notifications/{id}")
	public ResponseEntity<String> deleteNotification(@PathVariable Long id) {
		try {
		 notificationService.deleteNotification(id);
			return ResponseEntity
					.ok("Notification deleted successfully. Transaction hash: " + "xoá thành công");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete notification.");
		}
	}

	static class BloodDonationRequest {
		private String name;
		private String donationDate;
		private String registeredDate;
		private BigInteger volume;
		private String bloodType;
		private String status;
		private BigInteger userId;
		private BigInteger bloodId;

		// Getters and Setters for each attribute
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDonationDate() {
			return donationDate;
		}

		public void setDonationDate(String donationDate) {
			this.donationDate = donationDate;
		}

		public String getRegisteredDate() {
			return registeredDate;
		}

		public void setRegisteredDate(String registeredDate) {
			this.registeredDate = registeredDate;
		}

		public BigInteger getVolume() {
			return volume;
		}

		public void setVolume(BigInteger volume) {
			this.volume = volume;
		}

		public String getBloodType() {
			return bloodType;
		}

		public void setBloodType(String bloodType) {
			this.bloodType = bloodType;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public BigInteger getUserId() {
			return userId;
		}

		public void setUserId(BigInteger userId) {
			this.userId = userId;
		}

		public BigInteger getBloodId() {
			return bloodId;
		}

		public void setBloodId(BigInteger bloodId) {
			this.bloodId = bloodId;
		}
	}

	static class NotificationRequest {
		private String title;
		private String content;
		private String noticeDate;

		// Getters and Setters
		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getNoticeDate() {
			return noticeDate;
		}

		public void setNoticeDate(String noticeDate) {
			this.noticeDate = noticeDate;
		}
	}

	static class UpdateBloodDonationRequest {
		private BigInteger donationId; // The ID of the donation record to be updated
		private String name;
		private String donationDate;
		private String registeredDate;
		private BigInteger volume;
		private String bloodType;
		private String status;

		// Getters and Setters
		public BigInteger getDonationId() {
			return donationId;
		}

		public void setDonationId(BigInteger donationId) {
			this.donationId = donationId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDonationDate() {
			return donationDate;
		}

		public void setDonationDate(String donationDate) {
			this.donationDate = donationDate;
		}

		public String getRegisteredDate() {
			return registeredDate;
		}

		public void setRegisteredDate(String registeredDate) {
			this.registeredDate = registeredDate;
		}

		public BigInteger getVolume() {
			return volume;
		}

		public void setVolume(BigInteger volume) {
			this.volume = volume;
		}

		public String getBloodType() {
			return bloodType;
		}

		public void setBloodType(String bloodType) {
			this.bloodType = bloodType;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}
}
