package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.District;
import com.example.demo.model.Province;
import com.example.demo.model.Ward;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.repository.ProvinceRepository;
import com.example.demo.repository.WardRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

	@Autowired
	private ProvinceRepository provinceRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private WardRepository wardRepository;

	// Lấy danh sách tất cả các tỉnh
	public List<Province> getAllProvinces() {
		return provinceRepository.findAll();
	}

	// Lấy danh sách tất cả các huyện của một tỉnh
	public List<District> getDistrictsByProvinceId(Long provinceId) {
		return districtRepository.findAllByProvinceId(provinceId);
	}

	// Lấy danh sách tất cả các xã/phường của một huyện
	public List<Ward> getWardsByDistrictId(Long districtId) {
		return wardRepository.findAllByDistrictId(districtId);
	}

	// Lấy thông tin chi tiết của một tỉnh
	public Optional<Province> getProvinceById(Long id) {
		return provinceRepository.findById(id);
	}

	// Lấy thông tin chi tiết của một huyện
	public Optional<District> getDistrictById(Long id) {
		return districtRepository.findById(id);
	}

	// Lấy thông tin chi tiết của một xã/phường
	public Optional<Ward> getWardById(Long id) {
		return wardRepository.findById(id);
	}
}
