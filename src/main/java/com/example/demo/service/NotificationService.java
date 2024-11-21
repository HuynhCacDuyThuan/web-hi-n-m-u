package com.example.demo.service;

import com.example.demo.model.Notification;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.socket.NotificationWebSocketHandler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final NotificationWebSocketHandler notificationWebSocketHandler;

	public NotificationService(NotificationRepository notificationRepository,
			NotificationWebSocketHandler notificationWebSocketHandler) {
		this.notificationRepository = notificationRepository;
		this.notificationWebSocketHandler = notificationWebSocketHandler;
	}

	// Method to add a notification and broadcast it
	public Notification createAndBroadcastNotification(String title, String content, String noticeDate) {
		// 1. Save the notification to the database
		Notification notification = new Notification(title, content, noticeDate);
		Notification savedNotification = notificationRepository.save(notification);

		// 2. Broadcast the notification via WebSocket
		notificationWebSocketHandler.sendNotification(savedNotification);

		return savedNotification;
	}

	// Get all notifications
	public List<Notification> getAllNotifications() {
		return notificationRepository.findAll();
	}

	// Fetch all notifications
	public List<Notification> fetchAllNotifications() {
		return notificationRepository.findAll();
	}

	// Delete a notification by ID
	public void deleteNotification(Long id) {
		if (notificationRepository.existsById(id)) {
			notificationRepository.deleteById(id);
		} else {
			throw new IllegalArgumentException("Notification not found");
		}
	}

	// Post-construct method to broadcast all existing notifications on startup
	@PostConstruct
	public void broadcastExistingNotifications() {
		List<Notification> notifications = notificationRepository.findAll();
		notifications.forEach(notificationWebSocketHandler::sendNotification);
	}
}
