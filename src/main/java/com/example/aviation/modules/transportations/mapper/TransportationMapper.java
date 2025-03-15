package com.example.aviation.modules.transportations.mapper;

import com.example.aviation.modules.transportations.dto.request.TransportationCreateOrUpdateRequest;
import com.example.aviation.modules.transportations.dto.response.TransportationCreateOrUpdateResponse;
import com.example.aviation.modules.transportations.entity.Transportation;

public class TransportationMapper {

    private TransportationMapper() {
        // private constructor
    }

    public static TransportationCreateOrUpdateResponse toDto(Transportation transportation) {
        return TransportationCreateOrUpdateResponse.builder()
                .id(transportation.getId())
                .originLocationCode(transportation.getOrigin().getLocationCode())
                .destinationLocationCode(transportation.getDestination().getLocationCode())
                .transportationType(transportation.getTransportationType())
                .operatingDays(transportation.getOperatingDays())
                .createdAt(transportation.getCreatedAt())
                .updatedAt(transportation.getUpdatedAt())
                .build();
    }

    /**
     * Returns a new Transportation entity from the given request object.
     * 
     * <b>Sets the origin and destination locations to null!<b>
     * 
     * Origin and destination locations should be set manually after calling this
     * method using the location repository.
     * 
     * @param transportation
     * @return
     */
    public static Transportation toEntity(TransportationCreateOrUpdateRequest transportation) {
        return Transportation.builder()
                .transportationType(transportation.getTransportationType())
                .operatingDays(transportation.getOperatingDays())
                .build();
    }

}
