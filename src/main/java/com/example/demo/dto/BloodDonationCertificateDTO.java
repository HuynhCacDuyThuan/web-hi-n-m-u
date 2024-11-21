package com.example.demo.dto;

import java.time.LocalDate;

public class BloodDonationCertificateDTO {

	// Basic Information
	private String donorName; // Tên người hiến máu
	private LocalDate birthDate; // Ngày tháng năm sinh
	private String idNumber; // Số CMND
	private String address; // Địa chỉ

	// Donation Details
	private String donationLocation; // Cơ sở tiếp nhận máu
	private int bloodVolume; // Số lượng máu (in ml)

	// Certificate Information
	private LocalDate certificateDate; // Ngày chứng nhận
	private String certificateNumber; // Mã số chứng nhận

	// Constructors
	public BloodDonationCertificateDTO() {
	}

	public BloodDonationCertificateDTO(String donorName, LocalDate birthDate, String idNumber, String address,
			String donationLocation, int bloodVolume, LocalDate certificateDate, String certificateNumber) {
		this.donorName = donorName;
		this.birthDate = birthDate;
		this.idNumber = idNumber;
		this.address = address;
		this.donationLocation = donationLocation;
		this.bloodVolume = bloodVolume;
		this.certificateDate = certificateDate;
		this.certificateNumber = certificateNumber;
	}

	// Getters and Setters
	public String getDonorName() {
		return donorName;
	}

	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
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

	public String getDonationLocation() {
		return donationLocation;
	}

	public void setDonationLocation(String donationLocation) {
		this.donationLocation = donationLocation;
	}

	public int getBloodVolume() {
		return bloodVolume;
	}

	public void setBloodVolume(int bloodVolume) {
		this.bloodVolume = bloodVolume;
	}

	public LocalDate getCertificateDate() {
		return certificateDate;
	}

	public void setCertificateDate(LocalDate certificateDate) {
		this.certificateDate = certificateDate;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	// toString method for easy debugging
	@Override
	public String toString() {
		return "BloodDonationCertificateDTO{" + "donorName='" + donorName + '\'' + ", birthDate=" + birthDate
				+ ", idNumber='" + idNumber + '\'' + ", address='" + address + '\'' + ", donationLocation='"
				+ donationLocation + '\'' + ", bloodVolume=" + bloodVolume + ", certificateDate=" + certificateDate
				+ ", certificateNumber='" + certificateNumber + '\'' + '}';
	}
}
