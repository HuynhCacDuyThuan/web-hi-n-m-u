package com.example.demo.dto;

import java.math.BigInteger;

public class BloodDonationStatisticalDTO {
	private BigInteger id;
	private String name;
	private BigInteger donationDate;
	private String registeredDate;
	private BigInteger volume;
	private String bloodType;
	private String status;
	private BigInteger idBlood;
	private BigInteger userId;
	private String province;
	
	
	public BloodDonationStatisticalDTO() {
		super();
	}
	public BloodDonationStatisticalDTO(BigInteger id, String name, BigInteger donationDate, String registeredDate,
			BigInteger volume, String bloodType, String status, BigInteger idBlood, BigInteger userId,
			String province) {
		super();
		this.id = id;
		this.name = name;
		this.donationDate = donationDate;
		this.registeredDate = registeredDate;
		this.volume = volume;
		this.bloodType = bloodType;
		this.status = status;
		this.idBlood = idBlood;
		this.userId = userId;
		this.province = province;
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
	public BigInteger getIdBlood() {
		return idBlood;
	}
	public void setIdBlood(BigInteger idBlood) {
		this.idBlood = idBlood;
	}
	public BigInteger getUserId() {
		return userId;
	}
	public void setUserId(BigInteger userId) {
		this.userId = userId;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	
}
