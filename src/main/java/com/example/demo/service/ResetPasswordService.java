package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;

import java.security.SecureRandom;

@Service
public class ResetPasswordService {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	// Tạo mật khẩu ngẫu nhiên
	private String generateRandomPassword() {
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder();
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
		for (int i = 0; i < 8; i++) { // Tạo mật khẩu 8 ký tự
			int randomIndex = random.nextInt(chars.length());
			password.append(chars.charAt(randomIndex));
		}
		return password.toString();
	}

	// Gửi email chứa mật khẩu mới và lưu vào cơ sở dữ liệu
	public String resetPasswordAndSendEmail(String email) {
		// Tạo mật khẩu mới ngẫu nhiên
		String newPassword = generateRandomPassword();

		// Mã hóa mật khẩu
		String encodedPassword = passwordEncoder.encode(newPassword);

		// Cập nhật mật khẩu mới vào cơ sở dữ liệu
		Users user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		user.setPassword(encodedPassword);
		userRepository.save(user);

		// Gửi email chứa mật khẩu mới
		sendResetPasswordEmail(email, newPassword);

		return "New password sent to email.";
	}

	// Gửi email chứa mật khẩu mới
	private void sendResetPasswordEmail(String email, String newPassword) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("vuthanh10235@gmail.com");
		message.setTo(email);
		message.setSubject("Mật khẩu mới !");
		message.setText("Mật khẩu mới của bạn là : " + newPassword);

		emailSender.send(message);
	}
}
