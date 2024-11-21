package com.example.demo.service;

import com.example.demo.dto.BloodDonationDTO;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.VolunteerDTO;
import com.example.demo.model.Contract;
import com.example.demo.model.Event;
import com.example.demo.model.Users;
import com.example.demo.until.DateConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple10;
import org.web3j.tuples.generated.Tuple11;
import org.web3j.tuples.generated.Tuple12;
import org.web3j.tuples.generated.Tuple8;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VolunteerService {
	@Autowired
	private UserService userService;
	@Autowired
	private BlockchainService blockchainService;
	@Autowired
	private Contract contract;
	@Autowired
	EventService eventService;
	@Autowired
	BloodDonationService bloodDonationService;

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
					users.getImg());

		} catch (Exception e) {
			e.printStackTrace();
			return null; // Alternatively, throw a custom exception
		}
	}

	private BloodDonationDTO getBloodDonationById(BigInteger donationId) {
	    try {
	        // Call getBloodDonation for a single donationId, expecting a Tuple12
	        Tuple12<BigInteger, String, BigInteger, String, BigInteger, String, String, BigInteger, BigInteger, Boolean, String, String> donationTuple = contract
	                .getBloodDonation(donationId).send();

	        // Convert the result to BloodDonationDTO, including the 'location' and 'additionalInfo' fields
	        return convertToDto(donationTuple);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null; // You may want to handle this more gracefully
	    }
	}


	private BloodDonationDTO convertToDto(
	        Tuple12<BigInteger, String, BigInteger, String, BigInteger, String, String, BigInteger, BigInteger, Boolean, String, String> donationTuple) {
	    
	    return new BloodDonationDTO(
	            donationTuple.getValue1(), // id
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
	            donationTuple.getValue12()  // additionalInfo
	    );
	}


	public VolunteerDTO getVolunteerById(BigInteger donationId) throws Exception {
		// Retrieve the blood donation by its ID directly
		BloodDonationDTO donationDTO = getBloodDonationById(donationId);
		LocalDate currentDate = LocalDate.now();

		// Format the date as "Ngày dd tháng MM năm yyyy"
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Ngày' dd 'tháng' MM 'năm' yyyy");
		String formattedDate = currentDate.format(formatter);
		if (donationDTO == null) {
			// Handle the case where the donation does not exist
			return null;
		}

		// Retrieve events related to this donation

		// Retrieve the user associated with this donation
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
			LocalDate lastDonationDate = Instant.ofEpochSecond(donationDTO.getDonationDate().longValue())
					.atZone(ZoneId.systemDefault()).toLocalDate();

			// Format the last donation date as a string in the desired format
			String formattedLastDonationDate = (lastDonationDate != null)
					? lastDonationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
					: "Chưa có";

			// Set the status based on certification issuance
			boolean isCertificationIssued = donationDTO.isCertificationIssued();

			// If there are events, get the location from the first event (or handle
			// differently if needed)
			String province = getProvinceFromLocation(donationDTO.getLocation());

			// Create and populate the VolunteerDTO
			VolunteerDTO volunteerDTO = new VolunteerDTO();
			volunteerDTO.setId(donationDTO.getId());
			volunteerDTO.setFullName(donationDTO.getName());
			volunteerDTO.setEmail(userDto.getEmail());
			volunteerDTO.setBloodType(donationDTO.getBloodType());
			volunteerDTO.setStatus(isCertificationIssued);
			volunteerDTO.setBirthDate(formattedBirthDate);
			volunteerDTO.setIdNumber(userDto.getCccd());
			volunteerDTO.setAddress(userDto.getHomeAddress());
			volunteerDTO.setLocation(province);
			volunteerDTO.setLastDonationDate(lastDonationDate);
			volunteerDTO.setFormattedLastDonationDate(formattedLastDonationDate);
			volunteerDTO.setBloodVolume(donationDTO.getVolume());
			volunteerDTO.setCurrentDate(formattedDate);

			return volunteerDTO; // Return the VolunteerDTO once populated
		}

		// Return null if no matching user is found for the donation
		return null;
	}

	public String getProvinceFromLocation(String location) {
		String[] parts = location.split(", ");
		return parts[parts.length - 1];
	}

	public TransactionReceipt issueCertification(BigInteger donationId) throws Exception {
		// Load your contract

		// Call the issueCertification method in your contract
		return contract.issueCertification(donationId).send();
	}
}
