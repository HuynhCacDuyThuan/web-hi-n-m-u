package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.example.demo.model.NewsItem;
import com.example.demo.model.XMLParser;

@Controller
public class XMLController {
    
    @GetMapping("/tin-tuc")
    public String tintuc(
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

        // Phân tích XML và lấy danh sách tin tức
        XMLParser parser = new XMLParser();
        List<NewsItem> list = parser.parseXML(); // Trả về danh sách các đối tượng NewsItem
        model.addAttribute("listTinTuc", list);

        return "user/news"; // Trả về trang tin tức
    }
}
