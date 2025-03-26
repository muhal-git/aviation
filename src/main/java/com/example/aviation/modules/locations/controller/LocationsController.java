package com.example.aviation.modules.locations.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aviation.modules.locations.dto.request.LocationCreateOrUpdateRequest;
import com.example.aviation.modules.locations.dto.response.LocationCreateOrUpdateResponse;
import com.example.aviation.modules.locations.service.LocationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationsController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationCreateOrUpdateResponse>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @PostMapping
    public ResponseEntity<LocationCreateOrUpdateResponse> createLocation(@RequestBody @Valid LocationCreateOrUpdateRequest request) {
        return ResponseEntity.ok(locationService.createLocation(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationCreateOrUpdateResponse> updateLocation(@PathVariable Long id,
            @RequestBody @Valid LocationCreateOrUpdateRequest updated) {
        return ResponseEntity.ok(locationService.updateLocation(id, updated));
    }

    @DeleteMapping("/{locationCode}")
    public ResponseEntity<Object> deleteLocation(@PathVariable String locationCode) {
        locationService.deleteLocation(locationCode);
        return ResponseEntity.noContent().build();
    }

}
