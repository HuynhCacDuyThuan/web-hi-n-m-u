package com.example.demo.model;


import java.time.LocalDate;
import java.time.LocalTime;

public class BloodDonationHistory {

    private LocalDate registrationDate; // Ngày Đăng Ký
    private String location;            // Địa Điểm
    private LocalTime startTime;        // Thời Gian Hiến - Start time
    private LocalTime endTime;          // Thời Gian Hiến - End time
    private String status;      
    private String medicalstatus;   // Trạng Thái

    // Constructors
    public BloodDonationHistory() {
    }

    public BloodDonationHistory(LocalDate registrationDate, String location, LocalTime startTime, LocalTime endTime, String status) {
        this.registrationDate = registrationDate;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    // Getters and Setters
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
 
    public String getMedicalstatus() {
		return medicalstatus;
	}

	public void setMedicalstatus(String medicalstatus) {
		this.medicalstatus = medicalstatus;
	}

	@Override
    public String toString() {
        return "BloodDonationHistory{" +
                "registrationDate=" + registrationDate +
                ", location='" + location + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                '}';
    }
}
