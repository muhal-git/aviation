package com.example.aviation.modules.locations.mapper;

import com.example.aviation.modules.locations.dto.request.LocationCreateOrUpdateRequest;
import com.example.aviation.modules.locations.dto.response.LocationCreateOrUpdateResponse;
import com.example.aviation.modules.locations.entity.Location;

public class LocationMapper {

    private LocationMapper() {
        // private constructor
    }
    
    public static Location mapToLocation(LocationCreateOrUpdateRequest request) {
        Location location = new Location();
        location.setName(request.getName());
        location.setCountry(request.getCountry().toUpperCase());
        location.setCity(request.getCity().toUpperCase());
        location.setLocationCode(request.getLocationCode().toUpperCase());
        return location;
    }

    public static LocationCreateOrUpdateResponse mapToLocationResponse(Location location) {
        return LocationCreateOrUpdateResponse.builder()
            .name(location.getName())
            .country(location.getCountry())
            .city(location.getCity())
            .locationCode(location.getLocationCode())
            .createdAt(location.getCreatedAt())
            .updatedAt(location.getUpdatedAt())
            .build();
    }
    
}
