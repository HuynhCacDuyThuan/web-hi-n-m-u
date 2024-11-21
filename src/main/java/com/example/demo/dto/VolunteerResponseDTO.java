package com.example.demo.dto;

public class VolunteerResponseDTO {
	private VolunteerDTO volunteer;
	private String formattedDate;

	// Constructor
	public VolunteerResponseDTO(VolunteerDTO volunteer, String formattedDate) {
		this.volunteer = volunteer;
		this.formattedDate = formattedDate;
	}

	// Getters and Setters
	public VolunteerDTO getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(VolunteerDTO volunteer) {
		this.volunteer = volunteer;
	}

	public String getFormattedDate() {
		return formattedDate;
	}

	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}
}
