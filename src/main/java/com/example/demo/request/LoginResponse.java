package com.example.demo.request;

import java.math.BigInteger;

public class LoginResponse {
    private String token;
    private BigInteger id;
    private String username;
    private String email;
    private String cccd;
    private String gender;
    private String dateOfBirth;
    private String bloodGroup;
    private String role; // Thêm trường vai trò

    // Constructor
    public LoginResponse(String token, BigInteger id, String username, String email, String cccd, String gender, String dateOfBirth, String bloodGroup, String role) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.cccd = cccd;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.bloodGroup = bloodGroup;
        this.role = role; // Khởi tạo vai trò
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public String getRole() {
        return role; // Getter cho vai trò
    }

    public void setRole(String role) {
        this.role = role; // Setter cho vai trò
    }

	@Override
	public String toString() {
		return "LoginResponse [token=" + token + ", id=" + id + ", username=" + username + ", email=" + email
				+ ", cccd=" + cccd + ", gender=" + gender + ", dateOfBirth=" + dateOfBirth + ", bloodGroup="
				+ bloodGroup + ", role=" + role + "]";
	}
    
}
