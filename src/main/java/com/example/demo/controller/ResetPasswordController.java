package com.example.demo.controller;

import com.example.demo.service.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reset-password")
public class ResetPasswordController {

	@Autowired
	private ResetPasswordService resetPasswordService;

	// Yêu cầu gửi mật khẩu mới qua email
	@PostMapping("/request")
	public String requestPasswordReset(@RequestParam String email) {
		// Gọi service để tạo mật khẩu mới, lưu vào CSDL và gửi qua email
		return resetPasswordService.resetPasswordAndSendEmail(email);
	}
}
