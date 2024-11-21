package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.District;
import com.example.demo.model.Province;
import com.example.demo.model.Ward;
import com.example.demo.service.LocationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    // Lấy danh sách tất cả các tỉnh
    @GetMapping("/provinces")
    public ResponseEntity<List<Province>> getAllProvinces() {
        return ResponseEntity.ok(locationService.getAllProvinces());
    }

    // Lấy danh sách tất cả các huyện của một tỉnh
    @GetMapping("/provinces/{provinceId}/districts")
    public ResponseEntity<List<District>> getDistrictsByProvinceId(@PathVariable Long provinceId) {
        List<District> districts = locationService.getDistrictsByProvinceId(provinceId);
        if (districts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(districts);
    }

    // Lấy danh sách tất cả các xã/phường của một huyện
    @GetMapping("/districts/{districtId}/wards")
    public ResponseEntity<List<Ward>> getWardsByDistrictId(@PathVariable Long districtId) {
        List<Ward> wards = locationService.getWardsByDistrictId(districtId);
        if (wards.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(wards);
    }

    // Lấy thông tin chi tiết của một tỉnh
    @GetMapping("/provinces/{id}")
    public ResponseEntity<Province> getProvinceById(@PathVariable Long id) {
        Optional<Province> province = locationService.getProvinceById(id);
        return province.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Lấy thông tin chi tiết của một huyện
    @GetMapping("/districts/{id}")
    public ResponseEntity<District> getDistrictById(@PathVariable Long id) {
        Optional<District> district = locationService.getDistrictById(id);
        return district.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Lấy thông tin chi tiết của một xã/phường
    @GetMapping("/wards/{id}")
    public ResponseEntity<Ward> getWardById(@PathVariable Long id) {
        Optional<Ward> ward = locationService.getWardById(id);
        return ward.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
