package com.example.demo.service;

import com.example.demo.dto.BloodDonationDTO;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.VolunteerDTO;
import com.example.demo.model.Contract;
import com.example.demo.model.Contract.BloodDonation;
import com.example.demo.model.Event;
import com.example.demo.model.Users;
import com.example.demo.until.BloodDonationConverter;
import com.example.demo.until.DateConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.tuples.generated.Tuple10;
import org.web3j.tuples.generated.Tuple11;
import org.web3j.tuples.generated.Tuple12;
import org.web3j.tuples.generated.Tuple8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class BloodDonationService {
	@Autowired
	private UserService userService;
	@Autowired
	private BlockchainService blockchainService;
	private final Contract contract;
	@Autowired
	EventService eventService;
	private static final Logger logger = LoggerFactory.getLogger(BloodDonationService.class);

	// Constructor injection of BlockchainService
	@Autowired
	public BloodDonationService(BlockchainService blockchainService) {
		// Use the contract instance from BlockchainService
		this.contract = blockchainService.getContract();
	}

	// Method to retrieve all blood donations as a list of BloodDonationDTO
	public List<BloodDonationDTO> getAllBloodDonations() {
		try {
			// Call the contract method to get the RemoteFunctionCall object
			RemoteFunctionCall<List> remoteCall = contract.getAllBloodDonations();

			// Execute the call to get a raw list of objects from the blockchain
			List<?> rawBloodDonations = remoteCall.send();

			// Convert the raw list into a list of BloodDonation objects
			List<BloodDonation> bloodDonations = rawBloodDonations.stream().map(item -> (BloodDonation) item) // Cast
																												// each
																												// item
																												// to
																												// BloodDonation
					.collect(Collectors.toList());

			// Convert each BloodDonation to BloodDonationDTO
			return bloodDonations.stream().map(this::convertToDto).collect(Collectors.toList());

		} catch (Exception e) {
			e.printStackTrace();
			return null; // Handle the exception appropriately (e.g., return empty list or throw custom
							// exception)
		}
	}

	private BloodDonationDTO convertToDto(BloodDonation bloodDonation) {
		return new BloodDonationDTO(bloodDonation.getId(), // id
				bloodDonation.getName(), // name
				bloodDonation.getDonationDate(), // donationDate
				bloodDonation.getRegisteredDate(), // registeredDate
				bloodDonation.getVolume(), // volume
				bloodDonation.getBloodType(), // bloodType
				bloodDonation.getStatus(), // status
				bloodDonation.getIdBlood(), // idBlood
				bloodDonation.getUserId(), // userId
				bloodDonation.getCertificationIssued(), // certificationIssued
				bloodDonation.getLocation(), // location (mới thêm)
				bloodDonation.getMedicalstatus() // additionalInfo (hoặc trường mới khác)
		);
	}

	// Service method to get user by email
	public UserDto getUserByEmail(String email) {
		try {
			// Call the contract method to get user information
			RemoteFunctionCall<Tuple8<BigInteger, String, String, String, String, String, String, String>> remoteCall = contract
					.getUserByEmail(email);
			Tuple8<BigInteger, String, String, String, String, String, String, String> result = remoteCall.send();

			Users users = userService.findByEmail(email);

			// Map the Tuple8 result to UserDto
			return new UserDto(result.getValue1(), // Blockchain ID
					users.getId(), // Local database ID
					result.getValue2(), // Username
					result.getValue3(), // Email
					result.getValue4(), // CCCD
					result.getValue5(), // Gender
					result.getValue6(), // Day of Birth (String format)
					result.getValue7(), // Blood Group
					result.getValue8(), // Home Address
					users.getRole().getRolename(), // Role from database (assuming Role has getName())
					users.isLocked(), // isLocked from database
					users.getImg()

			);

		} catch (Exception e) {
			e.printStackTrace();
			return null; // Alternatively, throw a custom exception
		}
	}

	public List<VolunteerDTO> volunteerDTOs() throws Exception {
		List<VolunteerDTO> list = new ArrayList<>();
		List<BloodDonationDTO> bloodDonations = getAllBloodDonations();

		for (BloodDonationDTO dto : bloodDonations) {
			// Retrieve events related to this donation

			BloodDonationDTO donationDTO = getBloodDonationById(dto.getId());

			Optional<Users> optionalUser = userService.getUserById(donationDTO.getUserId().longValue());
			if (optionalUser.isPresent()) {
				Users user = optionalUser.get();
				UserDto userDto = getUserByEmail(user.getEmail());

				// Convert dayOfBirth from UserDto to LocalDate for birthDate in VolunteerDTO
				String dayOfBirth = userDto.getDayOfBirth();
				LocalDate birthDate = DateConverter.convertToDate(dayOfBirth);

				// Format birthDate as a string in "dd/MM/yyyy" format
				String formattedBirthDate = birthDate != null ? DateConverter.formatDate(birthDate) : "N/A";

				// Find the latest successful donation date for this user
				LocalDate lastDonationDate = Instant.ofEpochSecond(dto.getDonationDate().longValue())
						.atZone(ZoneId.systemDefault()).toLocalDate();

				// Format the last donation date as a string in the desired format
				String formattedLastDonationDate = (lastDonationDate != null)
						? lastDonationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
						: "Chưa có";

				// Set the status based on certification issuance
				boolean isCertificationIssued = dto.isCertificationIssued();
				String province = getProvinceFromLocation(donationDTO.getLocation());

				// Create and populate the VolunteerDTO
				VolunteerDTO volunteerDTO = new VolunteerDTO();
				volunteerDTO.setId(dto.getId());
				volunteerDTO.setFullName(donationDTO.getName());
				volunteerDTO.setEmail(userDto.getEmail());
				volunteerDTO.setBloodType(donationDTO.getBloodType());
				volunteerDTO.setStatus(isCertificationIssued);
				volunteerDTO.setBirthDate(formattedBirthDate);
				volunteerDTO.setIdNumber(userDto.getCccd());
				volunteerDTO.setAddress(userDto.getHomeAddress());
				volunteerDTO.setLocation(province);
				volunteerDTO.setLastDonationDate(lastDonationDate);
				volunteerDTO.setFormattedLastDonationDate(formattedLastDonationDate); // Set formatted date
				volunteerDTO.setBloodVolume(donationDTO.getVolume());
				volunteerDTO.setImg(userDto.getImg());
				volunteerDTO.setBlood_status(donationDTO.getStatus());
				// Add the populated VolunteerDTO to the list
				list.add(volunteerDTO);
			} else {
				// Log a warning if user not found
				logger.warn("No user found with ID: {}", dto.getUserId());

			}
		}
		return list;
	}

	public String getProvinceFromLocation(String location) {
		String[] parts = location.split(", ");
		return parts[parts.length - 1];
	}

	public List<BloodDonationDTO> getBloodDonations(List<BigInteger> donationIds) throws Exception {
		// For each donationId, call getBloodDonation individually and collect results
		// as DTOs
		return donationIds.stream().map(this::getBloodDonationById).collect(Collectors.toList());
	}

	private BloodDonationDTO getBloodDonationById(BigInteger donationId) {
		try {
			// Gọi hàm smart contract getBloodDonation, trả về Tuple12
			Tuple12<BigInteger, String, BigInteger, String, BigInteger, String, String, BigInteger, BigInteger, Boolean, String, String> donationTuple = contract
					.getBloodDonation(donationId).send();

			// Chuyển đổi Tuple12 thành BloodDonationDTO
			return convertToDto(donationTuple);
		} catch (Exception e) {
			e.printStackTrace();
			return null; // Có thể xử lý lỗi tốt hơn, ví dụ, ném một exception tùy chỉnh
		}
	}

	private BloodDonationDTO convertToDto(
			Tuple12<BigInteger, String, BigInteger, String, BigInteger, String, String, BigInteger, BigInteger, Boolean, String, String> donationTuple) {

		return new BloodDonationDTO(donationTuple.getValue1(), // id
				donationTuple.getValue2(), // name
				donationTuple.getValue3(), // donationDate
				donationTuple.getValue4(), // registeredDate
				donationTuple.getValue5(), // volume
				donationTuple.getValue6(), // bloodType
				donationTuple.getValue7(), // status
				donationTuple.getValue8(), // idBlood
				donationTuple.getValue9(), // userId
				donationTuple.getValue10(), // certificationIssued
				donationTuple.getValue11(), // location
				donationTuple.getValue12() // additionalInfo
		);
	}

}
