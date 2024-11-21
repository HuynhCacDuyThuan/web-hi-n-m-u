package com.example.demo.request;

import java.math.BigInteger;

public class UserDetailsResponse {
    private BigInteger id;
    private String username;
    private String email;
    private String cccd;
    private String gender;
    private String dateOfBirth;
    private String bloodGroup;
    private String homeAddress; // Thêm trường homeAddress

    // Constructor
    public UserDetailsResponse(BigInteger id, String username, String email, String cccd, String gender, String dateOfBirth, String bloodGroup, String homeAddress) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.cccd = cccd;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.bloodGroup = bloodGroup;
        this.homeAddress = homeAddress; // Khởi tạo homeAddress
    }

    // Getters and setters
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getHomeAddress() { // Thêm getter cho homeAddress
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) { // Thêm setter cho homeAddress
        this.homeAddress = homeAddress;
    }
}
