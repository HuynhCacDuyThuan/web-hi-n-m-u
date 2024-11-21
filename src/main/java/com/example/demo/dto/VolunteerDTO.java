package com.example.demo.dto;

import java.math.BigInteger;
import java.time.LocalDate;

public class VolunteerDTO {
	private BigInteger id;
	private String fullName; // Full name of the volunteer
	private String email; // Email address of the volunteer
	private String bloodType; // Blood type (e.g., A+, O-)
	private boolean status; // Status (e.g., "Sẵn Sàng" or "Đã Hiến Máu")
	private String birthDate; // Ngày tháng năm sinh
	private String idNumber; // Số CMND
	private String address; // Địa chỉ
	private String location; // Địa chỉ
	private LocalDate lastDonationDate; // Date of last donation
	private String formattedLastDonationDate; // New field
	private BigInteger bloodVolume;
	private String currentDate;
	private String blood_status;
	private String img;

	public VolunteerDTO(BigInteger id, String fullName, String email, String bloodType, boolean status,
			String birthDate, String idNumber, String address, String location, LocalDate lastDonationDate,
			String formattedLastDonationDate, BigInteger bloodVolume) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.bloodType = bloodType;
		this.status = status;
		this.birthDate = birthDate;
		this.idNumber = idNumber;
		this.address = address;
		this.location = location;
		this.lastDonationDate = lastDonationDate;
		this.formattedLastDonationDate = formattedLastDonationDate;
		this.bloodVolume = bloodVolume;
	}

	public VolunteerDTO() {
		super();
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDate getLastDonationDate() {
		return lastDonationDate;
	}

	public void setLastDonationDate(LocalDate lastDonationDate) {
		this.lastDonationDate = lastDonationDate;
	}

	// Getters and setters for the new field
	public String getFormattedLastDonationDate() {
		return formattedLastDonationDate;
	}

	public void setFormattedLastDonationDate(String formattedLastDonationDate) {
		this.formattedLastDonationDate = formattedLastDonationDate;
	}

	public BigInteger getBloodVolume() {
		return bloodVolume;
	}

	public void setBloodVolume(BigInteger bloodVolume) {
		this.bloodVolume = bloodVolume;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getBlood_status() {
		return blood_status;
	}

	public void setBlood_status(String blood_status) {
		this.blood_status = blood_status;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
