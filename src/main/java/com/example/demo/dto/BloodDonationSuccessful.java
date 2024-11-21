package com.example.demo.dto;

import java.time.LocalDateTime;

public class BloodDonationSuccessful {
	private LocalDateTime donationDate; // Ngày hiến máu, sử dụng kiểu LocalDateTime cho thời gian chính xác

	public BloodDonationSuccessful() {
		
	}

	public BloodDonationSuccessful(LocalDateTime donationDate) {
		super();
		this.donationDate = donationDate;
	}

	public LocalDateTime getDonationDate() {
		return donationDate;
	}

	public void setDonationDate(LocalDateTime donationDate) {
		this.donationDate = donationDate;
	}

}
