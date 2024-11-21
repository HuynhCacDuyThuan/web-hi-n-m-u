package com.example.demo.controller;

import com.example.demo.model.Event;
import com.example.demo.repository.EventRepository;
import com.example.demo.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Controller
public class EventController {

	// Đường dẫn để lưu các tệp tải lên (cấu hình trong application.properties)
	@Value("${upload.path}")
	private String pathUploadImage;

	// Tiêm EventRepository để thao tác với cơ sở dữ liệu
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	  private  NotificationService notificationService;
	@PostMapping("/addEvent")
	public ResponseEntity<?> addEvent(@RequestParam("eventName") String eventName,
			@RequestParam("location") String location, @RequestParam("eventDateTime") String eventDateTime,
			@RequestParam("endTime") String endTime, @RequestParam("img_event") MultipartFile imgFile) {

		// Tạo đối tượng Event và thiết lập các giá trị từ tham số
		Event event = new Event();
		event.setEventName(eventName);
		event.setLocation(location);

		try {
			// Chuyển đổi định dạng ngày và giờ
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
			LocalDateTime dateTime = LocalDateTime.parse(eventDateTime, formatter);
			event.setEventDate(dateTime.toLocalDate());
			event.setStartTime(dateTime.toLocalTime());
			event.setEndTime(LocalTime.parse(endTime));

			// Xử lý và lưu hình ảnh nếu có
			if (!imgFile.isEmpty()) {
				String imgFilename = System.currentTimeMillis() + "_"
						+ URLEncoder.encode(imgFile.getOriginalFilename(), StandardCharsets.UTF_8.toString());
				Path imgPath = Paths.get(pathUploadImage + File.separator + imgFilename);
				Files.write(imgPath, imgFile.getBytes());
				event.setImage(imgFilename);
			}

			// Lưu event vào cơ sở dữ liệu
			eventRepository.save(event);
			  // Add a notification for the new blood donation event
            String notificationTitle = "Thông báo hiến máu";
            String notificationContent =  eventName + " tại " + location ;
            String noticeDate = LocalDateTime.now().toString(); // Use the current date-time as notice date

            // Broadcast the notification
            notificationService.createAndBroadcastNotification(notificationTitle, notificationContent, noticeDate);
			// Trả về phản hồi JSON với trạng thái HTTP 200
			return ResponseEntity.ok().body(Map.of("message", "Thêm sự kiện thành công!"));

		} catch (IOException e) {
			e.printStackTrace();

			// Trả về phản hồi lỗi với trạng thái HTTP 500
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Có lỗi xảy ra khi lưu sự kiện."));
		}
	}
	 @DeleteMapping("/api/events/{id}")
	    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
	        // Check if the event exists
	        if (eventRepository.existsById(id)) {
	            // Delete the event
	            eventRepository.deleteById(id);
	            return ResponseEntity.ok("Event deleted successfully.");
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found.");
	        }
	    }
}
