package com.example.demo.dto;


public class BloodDonationStatisticsDTO {
    
    private String cityName;
    private int numberOfDonors;
    private int numberOfDonations;

    // Constructor
    public BloodDonationStatisticsDTO(String cityName, int numberOfDonors, int numberOfDonations) {
        this.cityName = cityName;
        this.numberOfDonors = numberOfDonors;
        this.numberOfDonations = numberOfDonations;
    }

    // Getters and Setters
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getNumberOfDonors() {
        return numberOfDonors;
    }

    public void setNumberOfDonors(int numberOfDonors) {
        this.numberOfDonors = numberOfDonors;
    }

    public int getNumberOfDonations() {
        return numberOfDonations;
    }

    public void setNumberOfDonations(int numberOfDonations) {
        this.numberOfDonations = numberOfDonations;
    }
}
