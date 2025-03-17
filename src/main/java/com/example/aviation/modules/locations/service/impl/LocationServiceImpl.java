package com.example.aviation.modules.locations.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.aviation.common.exception.custom.LocationNotFoundExeption;
import com.example.aviation.modules.locations.dto.request.LocationCreateOrUpdateRequest;
import com.example.aviation.modules.locations.dto.response.LocationCreateOrUpdateResponse;
import com.example.aviation.modules.locations.entity.Location;
import com.example.aviation.modules.locations.mapper.LocationMapper;
import com.example.aviation.modules.locations.repository.LocationRepository;
import com.example.aviation.modules.locations.service.LocationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public List<LocationCreateOrUpdateResponse> getAllLocations() {
        return locationRepository.findAll().stream().map(LocationMapper::mapToLocationResponse).toList();
    }

    @Override
    public LocationCreateOrUpdateResponse createLocation(LocationCreateOrUpdateRequest request) {
        Location location = LocationMapper.mapToLocation(request);
        Location savedLocation = locationRepository.save(location);
        return LocationMapper.mapToLocationResponse(savedLocation);
    }

    @Override
    public LocationCreateOrUpdateResponse updateLocation(Long id, LocationCreateOrUpdateRequest updated) {
        return locationRepository.findById(id)
                .map(location -> {
                    location.setName(updated.getName());
                    location.setCountry(updated.getCountry());
                    location.setCity(updated.getCity());
                    location.setLocationCode(updated.getLocationCode());
                    Location updatedLocation = locationRepository.save(location);
                    return LocationMapper.mapToLocationResponse(updatedLocation);
                }).orElseThrow(() -> new LocationNotFoundExeption("Location not found: " + id));
    }

    @Override
    public void deleteLocation(String locationCode) {
        Location location = locationRepository.findByLocationCode(locationCode)
                .orElseThrow(() -> new LocationNotFoundExeption("Location not found: " + locationCode));
        locationRepository.delete(location);
    }

}
