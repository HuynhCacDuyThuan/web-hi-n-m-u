package com.example.demo.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.District;
@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
	// Lấy danh sách tất cả các huyện của một tỉnh dựa trên provinceId
    List<District> findAllByProvinceId(Long provinceId);
}