package com.example.demo.repository;

import com.example.demo.model.Event;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
	// Custom query method with a different name to avoid conflict
	@Query("SELECT e FROM Event e WHERE e.id = :id")
	List<Event> findEventsById(@Param("id") Long id);

	Page<Event> findAll(Pageable pageable);

	Optional<Event> findById(Long id);

}