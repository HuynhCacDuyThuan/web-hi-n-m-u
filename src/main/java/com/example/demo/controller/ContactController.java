package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class ContactController {
    
    @GetMapping("/contact")
    public String contact(
            @SessionAttribute(value = "username", required = false) String username,
            Model model) {
        
        // Kiểm tra xem người dùng có đăng nhập hay không
        boolean isLoggedIn = username != null;

        // Thêm thuộc tính vào model
        model.addAttribute("isLoggedIn", isLoggedIn);

        // Nếu người dùng đã đăng nhập, thêm tên vào model (nếu cần)
        if (isLoggedIn) {
            model.addAttribute("username", username);
            // Thêm các thuộc tính khác như email, address, cccd, bloodGroup nếu cần
        }

        return "user/contact"; // Trả về trang liên hệ
    }
}
