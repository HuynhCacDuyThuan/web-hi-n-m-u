package com.example.demo.until;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateConverter {

	public static LocalDate convertToDate(String dayOfBirth) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			return LocalDate.parse(dayOfBirth, inputFormatter);
		} catch (DateTimeParseException e) {
			System.out.println("Error parsing date: " + e.getMessage());
			return null; // or handle error as needed
		}
	}

	public static String formatDate(LocalDate date) {
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return date.format(outputFormatter);
	}
}
