package com.example.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoadImageController {

    @Value("${upload.path}")
    private String pathUploadImage;

    @GetMapping(value = "loadImage")
    public ResponseEntity<byte[]> loadImage(@RequestParam(value = "imageName") String imageName) throws IOException {
        File file = new File(pathUploadImage + File.separatorChar + imageName);
        
        if (file.exists()) {
            // Lấy MIME type của file
            String mimeType = Files.probeContentType(file.toPath());
            // Tạo headers và đặt Content-Type
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(mimeType));

            // Đọc dữ liệu ảnh và trả về
            byte[] imageBytes;
            try (InputStream inputStream = new FileInputStream(file)) {
                imageBytes = IOUtils.toByteArray(inputStream);
            }
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            // Trả về mã 404 nếu file không tồn tại
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
