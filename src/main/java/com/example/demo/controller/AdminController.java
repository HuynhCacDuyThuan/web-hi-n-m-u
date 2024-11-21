package com.example.demo.controller;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.example.demo.model.Contract.BloodDonation;

import com.example.demo.dto.BloodDonationDTO;
import com.example.demo.dto.BloodDonationStatisticsDTO;
import com.example.demo.dto.BloodDonationSuccessful;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.VolunteerDTO;
import com.example.demo.model.BloodDonationHistory;
import com.example.demo.model.Event;
import com.example.demo.model.Notification;
import com.example.demo.model.Users;
import com.example.demo.service.BlockchainService;
import com.example.demo.service.BloodDonationService;
import com.example.demo.service.EventService;
import com.example.demo.service.MedicalConditionService;
import com.example.demo.service.UserService;

@Controller
public class AdminController {
	@Autowired
	private EventService eventService;
	@Autowired
	private BlockchainService blockchainService;
	@Autowired
	private UserService userService;
	@Autowired
	BloodDonationService bloodDonationService;
	@Autowired
	MedicalConditionService conditionService;

	@GetMapping("/admin/index")
	public String getIndexAdmin(Model model) {
		// Gọi BlockchainService để lấy danh sách hiến máu thành công
		List<BigInteger> successfulDonations = blockchainService.getSuccessfulDonationCounts();
		int countBlood = successfulDonations.size();
		model.addAttribute("countBlood", countBlood);

		// Gọi EventService để lấy danh sách tất cả sự kiện
		List<Event> allEvents = eventService.getAllEvents();
		int countEvent = allEvents.size();
		model.addAttribute("countEvent", countEvent);

		// Gọi BlockchainService để lấy danh sách tất cả lần hiến máu
		List<BloodDonation> allBloodDonations = blockchainService.getAllBloodDonations();
		int countBloods = allBloodDonations.size();
		model.addAttribute("countBloods", countBloods);

		// Gọi BlockchainService để lấy thống kê nhóm máu
		List<BloodDonationStatisticsDTO> statistics = blockchainService.getBloodDonationStatistics();
		model.addAttribute("statistics", statistics);

		// Trả về giao diện admin/index
		return "admin/index";
	}

	@GetMapping("/admin/users")
	public String getUserAdmin(Model model) {
		List<UserDto> users = blockchainService.getAllUsers();
		model.addAttribute("users", users);
		return "admin/users";
	}

	@GetMapping("/admin/blood")
	public String getBloodAdmin(Model model) {
		List<BloodDonation> bloodDonations = blockchainService.getAllBloodDonations();

		// Convert donation dates to readable formats
		List<Map<String, Object>> formattedBloodDonations = new ArrayList<>();
		for (BloodDonation donation : bloodDonations) {
			Map<String, Object> donationData = new HashMap<>();
			donationData.put("id", donation.getId());
			donationData.put("name", donation.getName());
			donationData.put("registeredDate", donation.getRegisteredDate());

			// Convert donationDate (BigInteger Unix timestamp) to a formatted string
			String formattedDonationDate = donation.getDonationDate().equals(BigInteger.ZERO) ? ""
					: Instant.ofEpochSecond(donation.getDonationDate().longValue()).atZone(ZoneId.systemDefault())
							.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			donationData.put("donationDate", formattedDonationDate); // Converted to String
			donationData.put("volume", donation.getVolume());
			donationData.put("bloodType", donation.getBloodType());
			donationData.put("status", donation.getStatus());
			donationData.put("medicalstatus", donation.getMedicalstatus());

			formattedBloodDonations.add(donationData);

		}
		model.addAttribute("conditionServices", conditionService.getAllMedicalConditions());
		model.addAttribute("bloodDonations", formattedBloodDonations);
		return "admin/blood";
	}

	@PostMapping("/update/{id}")
	public ResponseEntity<?> updateMedicalStatus(@PathVariable("id") BigInteger donationId,
			@RequestParam String newMedicalStatus) {
		try {
			// Gọi service để cập nhật trạng thái bệnh án
			TransactionReceipt receipt = blockchainService.updateMedicalStatus(donationId, newMedicalStatus);

			// Trả về phản hồi thành công với chi tiết transaction
			return ResponseEntity.ok().body("Transaction successful. Receipt: " + receipt.getTransactionHash());
		} catch (Exception e) {
			// Xử lý lỗi và trả về phản hồi thất bại
			return ResponseEntity.status(500).body("Failed to update medical status: " + e.getMessage());
		}
	}

	@PostMapping("/updateVolume/{id}")
	public CompletableFuture<ResponseEntity<String>> updateDonationVolume(@PathVariable("id") String id,
			@RequestParam("newVolume") String newVolume, @RequestParam("bloodGroup") String bloodGroup,
			@RequestParam("cccd") String cccd, @RequestParam("gender") String gender) {

		BigInteger donationId = new BigInteger(id);
		BigInteger volume = new BigInteger(newVolume);

		try {
			// Lấy dữ liệu hiến máu dựa trên donationId
			BloodDonationDTO dto = blockchainService.getBloodDonation(donationId);
			List<BloodDonation> donations = blockchainService.getDonationsByName(dto.getName());
			Optional<Users> optional = userService.getUserById(dto.getUserId().longValue());
			Users u = optional.get();
			UserDto userDto = blockchainService.getUserByEmailDetails(u.getEmail());
			// Tìm lần hiến máu gần nhất
			BloodDonation latestDonation = donations.stream().max(Comparator.comparing(BloodDonation::getDonationDate))
					.orElse(null);

			if (latestDonation != null) {
				// Lấy ngày hiến máu gần nhất dưới dạng LocalDateTime
				LocalDateTime lastDonationDate = LocalDateTime
						.ofEpochSecond(latestDonation.getDonationDate().longValue(), 0, ZoneOffset.UTC);
				LocalDateTime currentDate = LocalDateTime.now();

				// Tính số tuần giữa lần hiến máu gần nhất và ngày hiện tại
				long weeksSinceLastDonation = ChronoUnit.WEEKS.between(lastDonationDate, currentDate);

				// Kiểm tra điều kiện: ít nhất 12 tuần cho lần hiến máu gần nhất
				boolean isEligible = weeksSinceLastDonation >= 12;

				if (!isEligible) {
					return CompletableFuture.completedFuture(
							ResponseEntity.status(400).body("không đủ điều kiện do hạn chế về thời gian."));
				}
			}
			System.out.println("id" + userDto.getId());
			// Cập nhật thể tích hiến máu với các thông tin cần thiết
			return blockchainService.updateDonationVolume(userDto.getId(), donationId, volume, bloodGroup, cccd, gender)
					.thenApply(transactionReceipt -> {
						String transactionHash = transactionReceipt.getTransactionHash();
						return ResponseEntity.ok("Transaction successful with hash: " + transactionHash);
					}).exceptionally(e -> {
						return ResponseEntity.status(500).body("Thông tin không trùng khớp");
					});

		} catch (Exception e) {
			return CompletableFuture
					.completedFuture(ResponseEntity.status(500).body("Error occurred: " + e.getMessage()));
		}
	}

	private BloodDonationSuccessful convertToBloodDonationSuccess(BloodDonation donation) {
		BloodDonationSuccessful successful = new BloodDonationSuccessful();

		// Chuyển đổi BigInteger donationDate thành LocalDateTime
		BigInteger donationDateBigInt = donation.getDonationDate();
		LocalDateTime donationDateTime = LocalDateTime.ofEpochSecond(donationDateBigInt.longValue(), 0, ZoneOffset.UTC);

		// Gán giá trị đã chuyển đổi cho donationDate trong BloodDonationSuccessful
		successful.setDonationDate(donationDateTime);

		return successful;
	}

	@GetMapping("/view/{id}")
	@ResponseBody
	public ResponseEntity<BloodDonationDTO> getDonationDetails(@PathVariable("id") BigInteger id) {
		try {
			BloodDonationDTO donation = blockchainService.getBloodDonation(id);
			return ResponseEntity.ok(donation);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/admin/event-blood")
	public String getEventFloodAdmin(Model model) {
		List<Event> events = eventService.getAllEvents();
		model.addAttribute("events", events);

		return "admin/EventFlood";
	}

	@GetMapping("/admin/statistics")
	public String getStatisticsAdmin() {
		return "admin/statistics";
	}

	@GetMapping("/admin/notifications")
	public String getNotificationsAdmin(Model model) {
		List<Notification> notifications = blockchainService.fetchAllNotifications();
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		// Format each noticeDate in the list
		notifications.forEach(notification -> {
			try {
				LocalDateTime dateTime = LocalDateTime.parse(notification.getNoticeDate(), inputFormatter);
				notification.setNoticeDate(dateTime.format(outputFormatter));
			} catch (DateTimeParseException e) {
				e.printStackTrace();
			}
		});

		model.addAttribute("notifications", notifications);
		return "admin/notifications";
	}

	@GetMapping("/admin/volunteer")
	public String getVolunteerAdmin(Model model) throws Exception {
		List<VolunteerDTO> volunteers = bloodDonationService.volunteerDTOs();
		model.addAttribute("volunteers", volunteers);
		return "admin/volunteer";
	}

}
