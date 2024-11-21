package com.example.demo.service;

import com.example.demo.model.Event;
import com.example.demo.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;

	public List<Event> getAllEvents() {
		return eventRepository.findAll(); // Lấy tất cả các sự kiện
	}

	public List<Event> getEventsById(Long id) {
		return eventRepository.findEventsById(id);
	}

	public Optional<Event> findById(Long id) {
		return eventRepository.findById(id);
	}

	public Page<Event> getAllEvents(Pageable pageable) {
		return eventRepository.findAll(pageable);
	}
}
