package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import com.example.demo.model.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

@Controller
@SessionAttributes({"isLoggedIn", "username", "email","gender", "address", "cccd", "bloodGroup","id"}) // Đánh dấu các thuộc tính cần lưu trong session
public class HomeController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/")
    public String home(@CookieValue(value = "authToken", defaultValue = "") String authToken, Model model) {
        System.out.println("authToken: " + authToken);

        if (!authToken.isEmpty()) {
            try {
                Claims claims = jwtUtil.extractAllClaims(authToken);
                model.addAttribute("id", claims.get("id"));
                model.addAttribute("isLoggedIn", true);
                model.addAttribute("username", claims.get("username"));
                model.addAttribute("email", claims.get("email"));
                model.addAttribute("address", claims.get("address")); // Lưu address vào session
                model.addAttribute("cccd", claims.get("cccd")); // Lưu số điện thoại vào session
                model.addAttribute("gender", claims.get("gender")); // Lưu giới tính vào session
                model.addAttribute("bloodGroup", claims.get("bloodGroup")); // Lưu nhóm máu vào session
            } catch (Exception e) {
                model.addAttribute("isLoggedIn", false);
                model.addAttribute("username", null); // Clear username if there's an error
            }
        } else {
            model.addAttribute("isLoggedIn", false);
            model.addAttribute("username", null); // Clear username if not logged in
        }
        return "user/index";
    }

    @GetMapping("/logout")
    public String logout(SessionStatus status, HttpServletResponse response, Model model) {
        status.setComplete(); // Clear all session attributes marked with @SessionAttributes

        // Reset session-related attributes in the model
        model.addAttribute("isLoggedIn", false);
        model.addAttribute("username", null);

        // Clear the authToken cookie
        Cookie authCookie = new Cookie("authToken", null);
        authCookie.setMaxAge(0); // Expire immediately
        authCookie.setPath("/"); // Ensure path matches cookie's original path
        response.addCookie(authCookie);

        return "redirect:/"; // Redirect to the home page or login page
    }

}
