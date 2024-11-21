package com.example.demo.dto;

import java.math.BigInteger;

public class BloodDonationDTO {
	private BigInteger id;
	private String name;
	private BigInteger donationDate;
	private String registeredDate;
	private BigInteger volume;
	private String bloodType;
	private String status;
	public BigInteger id_blood;
	private BigInteger userId;
	private boolean certificationIssued;
	private String location;
	private String medicalstatus;
	// Constructor

	@Override
	public String toString() {
		return String.format(
				"ID: %d, Name: %s, Donation Date: %s, Registered Date: %s, Volume: %d, Blood Type: %s, Status: %s, User ID: %d",
				id, name, donationDate, registeredDate, volume, bloodType, status, userId);
	}

	public BloodDonationDTO(BigInteger id, String name, BigInteger donationDate, String registeredDate,
			BigInteger volume, String bloodType, String status, BigInteger userId) {
		this.id = id;
		this.name = name;
		this.donationDate = donationDate;
		this.registeredDate = registeredDate;
		this.volume = volume;
		this.bloodType = bloodType;
		this.status = status;
		this.userId = userId;
	}

	public BloodDonationDTO(BigInteger id, String name, BigInteger donationDate, String registeredDate,
			BigInteger volume, String bloodType, String status, BigInteger id_blood, BigInteger userId,
			boolean certificationIssued, String location) {
		super();
		this.id = id;
		this.name = name;
		this.donationDate = donationDate;
		this.registeredDate = registeredDate;
		this.volume = volume;
		this.bloodType = bloodType;
		this.status = status;
		this.id_blood = id_blood;
		this.userId = userId;
		this.certificationIssued = certificationIssued;
		this.location = location;
	}

	public BloodDonationDTO(BigInteger id, String name, BigInteger donationDate, String registeredDate,
			BigInteger volume, String bloodType, String status, BigInteger userId, boolean certificationIssued) {
		super();
		this.id = id;
		this.name = name;
		this.donationDate = donationDate;
		this.registeredDate = registeredDate;
		this.volume = volume;
		this.bloodType = bloodType;
		this.status = status;
		this.userId = userId;
		this.certificationIssued = certificationIssued;
	}

	public BloodDonationDTO(BigInteger id, String name, BigInteger donationDate, String registeredDate,
			BigInteger volume, String bloodType, String status, BigInteger id_blood, BigInteger userId,
			boolean certificationIssued) {
		super();
		this.id = id;
		this.name = name;
		this.donationDate = donationDate;
		this.registeredDate = registeredDate;
		this.volume = volume;
		this.bloodType = bloodType;
		this.status = status;
		this.id_blood = id_blood;
		this.userId = userId;
		this.certificationIssued = certificationIssued;
	}

	public BloodDonationDTO(BigInteger id, String name, BigInteger donationDate, String registeredDate,
			BigInteger volume, String bloodType, String status, BigInteger id_blood, BigInteger userId,
			boolean certificationIssued, String location, String medicalstatus) {
		super();
		this.id = id;
		this.name = name;
		this.donationDate = donationDate;
		this.registeredDate = registeredDate;
		this.volume = volume;
		this.bloodType = bloodType;
		this.status = status;
		this.id_blood = id_blood;
		this.userId = userId;
		this.certificationIssued = certificationIssued;
		this.location = location;
		this.medicalstatus = medicalstatus;
	}

	public String getMedicalstatus() {
		return medicalstatus;
	}

	public void setMedicalstatus(String medicalstatus) {
		this.medicalstatus = medicalstatus;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigInteger getDonationDate() {
		return donationDate;
	}

	public void setDonationDate(BigInteger donationDate) {
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

	public BigInteger getId_blood() {
		return id_blood;
	}

	public void setId_blood(BigInteger id_blood) {
		this.id_blood = id_blood;
	}

	public BigInteger getUserId() {
		return userId;
	}

	public void setUserId(BigInteger userId) {
		this.userId = userId;
	}

	public boolean isCertificationIssued() {
		return certificationIssued;
	}

	public void setCertificationIssued(boolean certificationIssued) {
		this.certificationIssued = certificationIssued;
	}

}
