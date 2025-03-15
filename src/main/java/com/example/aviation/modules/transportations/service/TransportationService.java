package com.example.aviation.modules.transportations.service;

import java.util.List;

import com.example.aviation.modules.transportations.dto.request.TransportationCreateOrUpdateRequest;
import com.example.aviation.modules.transportations.dto.response.TransportationCreateOrUpdateResponse;

public interface TransportationService {

    List<TransportationCreateOrUpdateResponse> getAllTransportations();

    TransportationCreateOrUpdateResponse createTransportation(TransportationCreateOrUpdateRequest transportation);

    TransportationCreateOrUpdateResponse updateTransportation(Long id, TransportationCreateOrUpdateRequest updated);

    void deleteTransportation(Long id);
    
}
