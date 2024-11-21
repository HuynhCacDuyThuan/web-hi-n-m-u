package com.example.demo.repository;

import com.example.demo.model.Users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
	Optional<Users> findByEmail(String email);

	Optional<Users> findById(Long id);
}
