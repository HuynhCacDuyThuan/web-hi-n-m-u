package com.example.demo.dto;

import java.math.BigInteger;

public class UserDto {
    private BigInteger id;
    private Long id_user;
    private String username;
    private String email;
    private String cccd;
    private String gender;
    private String dayOfBirth;
    private String bloodGroup;
    private String homeAddress;
    private String role;
    private boolean isLocked;
    private String img;
    
    
	public UserDto(BigInteger id, Long id_user, String username, String email, String cccd, String gender,
			String dayOfBirth, String bloodGroup, String homeAddress, String role, boolean isLocked, String img) {
		super();
		this.id = id;
		this.id_user = id_user;
		this.username = username;
		this.email = email;
		this.cccd = cccd;
		this.gender = gender;
		this.dayOfBirth = dayOfBirth;
		this.bloodGroup = bloodGroup;
		this.homeAddress = homeAddress;
		this.role = role;
		this.isLocked = isLocked;
		this.img = img;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public Long getId_user() {
		return id_user;
	}
	public void setId_user(Long id_user) {
		this.id_user = id_user;
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
	public String getDayOfBirth() {
		return dayOfBirth;
	}
	public void setDayOfBirth(String dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}
	public String getBloodGroup() {
		return bloodGroup;
	}
	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isLocked() {
		return isLocked;
	}
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}

}
