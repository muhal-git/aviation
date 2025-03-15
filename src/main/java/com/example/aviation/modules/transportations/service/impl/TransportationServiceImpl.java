package com.example.aviation.modules.transportations.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.aviation.common.exception.custom.LocationNotFoundExeption;
import com.example.aviation.modules.locations.repository.LocationRepository;
import com.example.aviation.modules.transportations.dto.request.TransportationCreateOrUpdateRequest;
import com.example.aviation.modules.transportations.dto.response.TransportationCreateOrUpdateResponse;
import com.example.aviation.modules.transportations.entity.Transportation;
import com.example.aviation.modules.transportations.mapper.TransportationMapper;
import com.example.aviation.modules.transportations.repository.TransportationRepository;
import com.example.aviation.modules.transportations.service.TransportationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransportationServiceImpl implements TransportationService {

    private final TransportationRepository transportationRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<TransportationCreateOrUpdateResponse> getAllTransportations() {
        return transportationRepository.findAll().stream()
                .map(TransportationMapper::toDto)
                .toList();
    }

    @Override
    public TransportationCreateOrUpdateResponse createTransportation(
            TransportationCreateOrUpdateRequest transportation) {
        Transportation entity = TransportationMapper.toEntity(transportation);
        entity.setOrigin(locationRepository.findByLocationCode(transportation.getOriginLocationCode())
                .orElseThrow(() -> new LocationNotFoundExeption(
                        "Origin location not found. Provide an existing location code")));
        entity.setDestination(locationRepository.findByLocationCode(transportation.getDestinationLocationCode())
                .orElseThrow(() -> new LocationNotFoundExeption(
                        "Destination location not found. Provide an existing location code")));
        return TransportationMapper.toDto(transportationRepository.save(entity));
    }

    @Override
    public TransportationCreateOrUpdateResponse updateTransportation(Long id,
            TransportationCreateOrUpdateRequest updated) {
        Transportation entity = transportationRepository.findById(id).orElseThrow();
        entity.setOrigin(locationRepository.findByLocationCode(updated.getOriginLocationCode())
                .orElseThrow(() -> new LocationNotFoundExeption(
                        "Origin location not found. Provide an existing location code")));
        entity.setDestination(locationRepository.findByLocationCode(updated.getDestinationLocationCode())
                .orElseThrow(() -> new LocationNotFoundExeption(
                        "Destination location not found. Provide an existing location code")));
        entity.setTransportationType(updated.getTransportationType());
        entity.setOperatingDays(updated.getOperatingDays());
        entity.setUpdatedAt(Instant.now());
        return TransportationMapper.toDto(transportationRepository.save(entity));
    }

    @Override
    public void deleteTransportation(Long id) {
        transportationRepository.deleteById(id);
    }
}
