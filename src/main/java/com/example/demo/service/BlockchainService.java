package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import com.example.demo.dto.BloodDonationDTO;
import com.example.demo.dto.BloodDonationStatisticalDTO;
import com.example.demo.dto.BloodDonationStatisticsDTO;
import com.example.demo.dto.BloodGroupStatisticsDto;
import com.example.demo.dto.MonthlyDonationStatisticsDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.Contract;
import com.example.demo.model.Contract.BloodDonation;
import com.example.demo.model.Contract.User;
import com.example.demo.model.Role;
import com.example.demo.model.Users;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.UserDetailsResponse;

import java.math.BigInteger;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tuples.generated.Tuple10;
import org.web3j.tuples.generated.Tuple11;
import org.web3j.tuples.generated.Tuple12;
import org.web3j.tuples.generated.Tuple4;

import io.reactivex.Flowable;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import java.util.List;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.web3j.tx.TransactionManager;
import org.web3j.tx.exceptions.ContractCallException;

@Service
public class BlockchainService {
	private final Web3j web3j;
	private final Contract contract;
	private final RawTransactionManager transactionManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	EventService eventService;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	NotificationService notificationService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public BlockchainService(@Value("${blockchain.infura-url}") String infuraUrl,
			@Value("${blockchain.private-key}") String privateKey,
			@Value("${blockchain.contract-address}") String contractAddress) {

		// Connect to Infura
		this.web3j = Web3j.build(new HttpService(infuraUrl));

		// Create credentials from private key
		Credentials credentials = Credentials.create(privateKey);
		this.transactionManager = new RawTransactionManager(web3j, credentials);

		// Load contract from deployed address
		this.contract = Contract.load(contractAddress, web3j, transactionManager, new DefaultGasProvider());
	}

	public Contract getContract() {
		return contract;
	}

	// Add a new donor
	public String addBloodDonation(String name, BigInteger donationDate, String registeredDate, BigInteger volume,
			String bloodType, String status, BigInteger userId, BigInteger bloodId, String _location) {
		try {
			TransactionReceipt transactionReceipt = contract.addDonation(name, donationDate, registeredDate, volume,
					bloodType, status, userId, bloodId, _location).send();
			return transactionReceipt.getTransactionHash();
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	public String deleteUser(BigInteger userId) {
		try {
			// Execute the deleteUser function on the blockchain
			TransactionReceipt transactionReceipt = contract.deleteUser(userId).send();
			return transactionReceipt.getTransactionHash(); // Return transaction hash
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage(); // Return error message in case of failure
		}
	}

	public List<BloodDonation> getAllBloodDonations() {
		try {
			// Call the getAllBloodDonations function from the contract
			RemoteFunctionCall<List> remoteFunctionCall = contract.getAllBloodDonations();
			List<BloodDonation> bloodDonations = new ArrayList<>();

			// Execute the call and retrieve the results
			List<BloodDonation> result = (List<BloodDonation>) remoteFunctionCall.send();

			// Populate the list with retrieved blood donations
			for (BloodDonation donation : result) {
				bloodDonations.add(donation);
			}

			return bloodDonations;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to fetch blood donations: " + e.getMessage());
		}
	}

	// Add a new user
	public String addUser(String username, String password, String email, String cccd, String gender, String dayOfBirth,
			String bloodGroup, String address) {
		try {
// Lưu thông tin vào blockchain trước
			TransactionReceipt transactionReceipt = contract
					.addUser(username, email, cccd, gender, dayOfBirth, bloodGroup, address).send();

// Sau khi giao dịch thành công trên blockchain, lấy transaction hash
			String transactionHash = transactionReceipt.getTransactionHash();

// Tạo đối tượng User mới sau khi blockchain phản hồi thành công
			Optional<Role> userRoleOpt = roleRepository.findByRolename("USER");
			if (!userRoleOpt.isPresent()) {

			}

			Role userRole = userRoleOpt.get();

// Tạo đối tượng User
			Users newUser = new Users();
			newUser.setEmail(email);
			newUser.setPassword(passwordEncoder.encode(password)); // Mã hóa mật khẩu
			newUser.setRole(userRole); // Gán role
			newUser.setLocked(false); // Người dùng không bị khóa

// Lưu người dùng vào cơ sở dữ liệu
			userRepository.save(newUser);

// Trả về transaction hash từ blockchain
			return transactionHash;

		} catch (Exception e) {
// Xử lý các lỗi nếu có trong khi thêm người dùng vào blockchain
			String errorMessage = e.getMessage();

			if (errorMessage.contains("Email already exists")) {
				return "Error: Email already exists."; // Nếu email đã tồn tại trên blockchain
			} else if (errorMessage.contains("Username already exists")) {
				return "Error: Username already exists."; // Nếu username đã tồn tại trên blockchain
			} else if (errorMessage.contains("CCCD already exists")) {
				return "Error: CCCD already exists."; // Nếu CCCD đã tồn tại trên blockchain
			} else {
				return "Error: " + errorMessage; // Thông báo lỗi mặc định nếu có lỗi khác
			}
		}
	}

	public BloodDonationDTO getBloodDonation(BigInteger donationId) {
		try {
			// Gọi hàm smart contract và nhận về Tuple12
			Tuple12<BigInteger, String, BigInteger, String, BigInteger, String, String, BigInteger, BigInteger, Boolean, String, String> donation = contract
					.getBloodDonation(donationId).send();

			// Ánh xạ Tuple12 vào BloodDonationDTO
			return new BloodDonationDTO(donation.getValue1(), // id
					donation.getValue2(), // name
					donation.getValue3(), // donationDate
					donation.getValue4(), // registeredDate
					donation.getValue5(), // volume
					donation.getValue6(), // bloodType
					donation.getValue7(), // status
					donation.getValue8(), // id_blood
					donation.getValue9(), // userId
					donation.getValue10(), // certificationIssued
					donation.getValue11(), // location (mới thêm)
					donation.getValue12() // additionalInfo (giả sử trường mới trong DTO)
			);
		} catch (Exception e) {
			e.printStackTrace();
			return null; // Có thể ném ra exception tùy theo logic của bạn
		}
	}

	// Add this method in BlockchainService class
	public String getUserByEmail(String email) {
		try {
			// Call the getUserByEmail method on the contract
			Tuple8<BigInteger, String, String, String, String, String, String, String> user = contract
					.getUserByEmail(email).send();

			// Format the output as a string
			return String.format(
					"ID: %d, Username: %s, Email: %s, CCCD: %s, Gender: %s, Date of Birth: %s, Blood Group: %s, Home Address: %s",
					user.getValue1(), // ID
					user.getValue2(), // Username
					user.getValue3(), // Email
					user.getValue4(), // CCCD
					user.getValue5(), // Gender
					user.getValue6(), // Date of Birth
					user.getValue7(), // Blood Group
					user.getValue8() // Home Address
			);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	// Add this method in BlockchainService class
	public UserDetailsResponse getUserByEmail_Response(String email) {
		try {
			// Call the getUserByEmail method on the contract
			Tuple8<BigInteger, String, String, String, String, String, String, String> user = contract
					.getUserByEmail(email).send();

			// Create a UserDetailsResponse object with the values from the Tuple8 response
			return new UserDetailsResponse(user.getValue1(), // ID
					user.getValue2(), // Username
					user.getValue3(), // Email
					user.getValue4(), // CCCD
					user.getValue5(), // Gender
					user.getValue6(), // Date of Birth
					user.getValue7(), // Blood Group
					user.getValue8() // Home Address
			);
		} catch (Exception e) {
			e.printStackTrace();
			return null; // Handle exceptions as needed, possibly return an error object
		}
	}

	// Get total users count

	public List<BloodDonation> getDonationsByName(String name) throws Exception {
		// Call the contract method to get donations by name
		RemoteFunctionCall<List> remoteCall = contract.getDonationsByName(name);
		return remoteCall.send(); // This sends the call to the blockchain and returns the result
	}

	// Check if the service is connected to the blockchain
	public boolean isConnected() {
		try {
			return web3j.netVersion().send().getNetVersion() != null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public CompletableFuture<TransactionReceipt> updateDonationVolume(BigInteger id_user, BigInteger id,
			BigInteger newVolume, String bloodGroup, String cccd, String gender) {
		try {
			// Call the smart contract function to update donation volume
			return contract.updateDonationVolumeById(id_user, id, newVolume, bloodGroup, cccd, gender).sendAsync();
		} catch (ContractCallException e) {
			throw new RuntimeException("Error updating donation volume: " + e.getMessage(), e);
		}
	}

	public List<UserDto> getAllUsers() {
		try {
			// Gọi hàm của hợp đồng thông minh để lấy danh sách người dùng
			RemoteFunctionCall<List> remoteFunctionCall = contract.getAllUsers();
			List<User> blockchainUserList = remoteFunctionCall.send();

			// Chuyển đổi danh sách người dùng từ blockchain sang UserDto
			return blockchainUserList.stream().map(blockchainUser -> {
				// Tìm user từ database dựa vào email hoặc ID để lấy role
				Users databaseUser = userRepository.findByEmail(blockchainUser.getEmail()).orElse(null); // Bạn có thể
																											// thay bằng
																											// .orElseThrow()
																											// nếu muốn
																											// xử lý lỗi

				String role = (databaseUser != null && databaseUser.getRole() != null)
						? databaseUser.getRole().getRolename()
						: "Unknown";

				boolean isLocked = (databaseUser != null) && databaseUser.isLocked();

				// Tạo đối tượng UserDto với thông tin role và isLocked từ DB
				return new UserDto(blockchainUser.getId(), databaseUser.getId(), blockchainUser.getUsername(),
						blockchainUser.getEmail(), blockchainUser.getCccd(), blockchainUser.getGender(),
						blockchainUser.getDayOfBirth(), blockchainUser.getBloodGroup(), blockchainUser.getHomeAddress(),
						role, // Thêm role từ database user
						isLocked, // Thêm trạng thái isLocked từ database user,
						databaseUser.getImg());
			}).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to fetch users: " + e.getMessage());
		}
	}

	public UserDto getUserByEmailDetails(String email) {
		try {
			// Call the smart contract function to get user details by email
			RemoteFunctionCall<Tuple8<BigInteger, String, String, String, String, String, String, String>> remoteFunctionCall = contract
					.getUserByEmail(email);

			// Try to fetch user details from blockchain
			Tuple8<BigInteger, String, String, String, String, String, String, String> blockchainUser;
			try {
				blockchainUser = remoteFunctionCall.send();
			} catch (Exception e) {
				throw new RuntimeException("User not found with this email on blockchain: " + email);
			}

			// Retrieve user details from the blockchain
			BigInteger blockchainId = blockchainUser.getValue1();
			String username = blockchainUser.getValue2();
			String blockchainEmail = blockchainUser.getValue3();
			String cccd = blockchainUser.getValue4();
			String gender = blockchainUser.getValue5();
			String dayOfBirth = blockchainUser.getValue6();
			String bloodGroup = blockchainUser.getValue7();
			String homeAddress = blockchainUser.getValue8();

			// Check if the blockchain email matches the requested email
			if (!email.equals(blockchainEmail)) {
				throw new RuntimeException("The email returned from blockchain does not match the requested email.");
			}

			// Find the user from the database by email
			Users databaseUser = userRepository.findByEmail(blockchainEmail).orElse(null);

			// Extract role and lock status from the database
			String role = (databaseUser != null && databaseUser.getRole() != null)
					? databaseUser.getRole().getRolename()
					: "Unknown";
			boolean isLocked = (databaseUser != null) && databaseUser.isLocked();

			// Create and return the UserDto with blockchain and database info
			return new UserDto(blockchainId, (databaseUser != null) ? databaseUser.getId() : null, // Database ID if
																									// exists
					username, blockchainEmail, cccd, gender, dayOfBirth, bloodGroup, homeAddress, role, isLocked,
					databaseUser.getImg());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to fetch user by email: " + e.getMessage());
		}
	}

	public BloodGroupStatisticsDto getBloodGroupStatistics() {
		try {
			// Gọi hàm từ hợp đồng thông minh
			Tuple4<BigInteger, BigInteger, BigInteger, BigInteger> result = contract.getBloodGroupStatistics().send();

			// Tạo DTO để trả về dữ liệu
			return new BloodGroupStatisticsDto(result.getValue1(), // Blood Group A
					result.getValue2(), // Blood Group B
					result.getValue3(), // Blood Group AB
					result.getValue4() // Blood Group O
			);

		} catch (Exception e) {
			throw new RuntimeException("Failed to fetch blood group statistics", e);
		}
	}

	public MonthlyDonationStatisticsDto getMonthlyDonationStatistics() {
		try {
			// Gọi hàm từ hợp đồng thông minh
			List<BigInteger> monthlyDonations = contract.getMonthlyDonationStatistics().send();

			// Trả về DTO với dữ liệu quyên góp theo tháng
			return new MonthlyDonationStatisticsDto(monthlyDonations);

		} catch (Exception e) {
			throw new RuntimeException("Failed to fetch monthly donation statistics", e);
		}
	}

	public List<BigInteger> getSuccessfulDonationCounts() {
		try {
			// Call the smart contract function
			List<BigInteger> successfulDonationCounts = contract.getSuccessfulDonationCounts().send();

			// Process and return the list of counts
			return successfulDonationCounts.stream().map(result -> (BigInteger) result).collect(Collectors.toList());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching successful donation counts", e);
		}
	}

	public List<BloodDonationStatisticalDTO> getBloodDonationStatisticalDTOs() {
		List<BloodDonationStatisticalDTO> list = new ArrayList<>();
		List<BloodDonation> bloodDonationDTOs = getAllBloodDonations(); // Retrieve all blood donations

		for (BloodDonation bloodDonation : bloodDonationDTOs) {

			BloodDonationStatisticalDTO bloodDonationStatisticalDTO = new BloodDonationStatisticalDTO();
			bloodDonationStatisticalDTO.setId(bloodDonation.getId());
			bloodDonationStatisticalDTO.setName(bloodDonation.getName());
			bloodDonationStatisticalDTO.setDonationDate(bloodDonation.getDonationDate());
			bloodDonationStatisticalDTO.setRegisteredDate(bloodDonation.getRegisteredDate());
			bloodDonationStatisticalDTO.setVolume(bloodDonation.getVolume());
			bloodDonationStatisticalDTO.setBloodType(bloodDonation.getBloodType());
			bloodDonationStatisticalDTO.setStatus(bloodDonation.getStatus());
			bloodDonationStatisticalDTO.setIdBlood(bloodDonation.getIdBlood());
			bloodDonationStatisticalDTO.setUserId(bloodDonation.getUserId());

			String location = bloodDonation.getLocation(); // Assuming this returns "Xã Lũng Hồ, Huyện Yên Minh, Hà
															// Giang"

			// Split the location string by commas
			String[] parts = location.split(",");

			if (parts.length > 0) {
				// Trim spaces and get the last part which contains the province
				String province = parts[parts.length - 1].trim();
				bloodDonationStatisticalDTO.setProvince(province); // Set province in DTO
			} else {
				bloodDonationStatisticalDTO.setProvince("Unknown"); // Handle case where location is empty or
																	// malformed
			}

			list.add(bloodDonationStatisticalDTO); // Add DTO to the list

		}
		return list; // Return the populated list
	}

	public List<BloodDonationStatisticsDTO> getBloodDonationStatistics() {
		List<BloodDonationStatisticsDTO> statistics = new ArrayList<>();
		List<BloodDonationStatisticalDTO> bloodDonationList = getBloodDonationStatisticalDTOs(); // Lấy danh sách tất cả
																									// lần hiến máu

		// Sử dụng Map để đếm số lượng nhà tài trợ duy nhất và số lần hiến máu theo từng
		// tỉnh
		Map<String, Set<String>> uniqueDonorsByProvince = new HashMap<>();
		Map<String, Integer> totalDonationsByProvince = new HashMap<>();

		for (BloodDonationStatisticalDTO donation : bloodDonationList) {
			// Kiểm tra xem trạng thái có là "successful" không
			if ("successful".equalsIgnoreCase(donation.getStatus())) {
				String province = donation.getProvince(); // Lấy tỉnh từ đối tượng donation
				String donorName = donation.getName(); // Lấy tên người hiến

				// Khởi tạo mục tỉnh trong các Map nếu chưa tồn tại
				uniqueDonorsByProvince.putIfAbsent(province, new HashSet<>());
				totalDonationsByProvince.putIfAbsent(province, 0);

				// Thêm người hiến vào tập hợp cho tỉnh này (đảm bảo tính duy nhất)
				uniqueDonorsByProvince.get(province).add(donorName);

				// Tăng số lượng hiến máu cho tỉnh này
				totalDonationsByProvince.put(province, totalDonationsByProvince.get(province) + 1);
			}
		}

		// Điền danh sách thống kê từ các Map
		for (String province : uniqueDonorsByProvince.keySet()) {
			int numberOfDonors = uniqueDonorsByProvince.get(province).size(); // Đếm người hiến duy nhất
			int numberOfDonations = totalDonationsByProvince.get(province); // Tổng số lần hiến máu
			statistics.add(new BloodDonationStatisticsDTO(province, numberOfDonors, numberOfDonations));
		}

		return statistics;
	}

	public CompletableFuture<TransactionReceipt> deleteBloodDonation(BigInteger donationId) {
		// Gọi hàm từ xa xóa thông tin hiến máu từ blockchain
		return contract.deleteBloodDonation(donationId).sendAsync();
	}

	public List fetchAllNotifications() {
		try {
			// Call getAllNotifications synchronously
			List notifications = notificationService.getAllNotifications();
			// Process the list if needed
			return notifications;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public TransactionReceipt updateMedicalStatus(BigInteger donationId, String newMedicalStatus) throws Exception {
		// Gọi phương thức updateMedicalStatus từ Web3j smart contract
		return contract.updateMedicalStatus(donationId, newMedicalStatus).send();
	}

}
