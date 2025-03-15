package com.example.aviation.modules.locations.service;

import java.util.List;

import com.example.aviation.modules.locations.dto.request.LocationCreateOrUpdateRequest;
import com.example.aviation.modules.locations.dto.response.LocationCreateOrUpdateResponse;

/**
 * Location Service Interface
 * 
 * CRUD operations for Location entity
 */
public interface LocationService {

    List<LocationCreateOrUpdateResponse> getAllLocations();

    LocationCreateOrUpdateResponse createLocation(LocationCreateOrUpdateRequest request);

    LocationCreateOrUpdateResponse updateLocation(String locationCode, LocationCreateOrUpdateRequest updated);

    void deleteLocation(String locationCode);

}
