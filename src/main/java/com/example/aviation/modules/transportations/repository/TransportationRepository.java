package com.example.aviation.modules.transportations.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aviation.modules.locations.entity.Location;
import com.example.aviation.modules.transportations.entity.Transportation;
import com.example.aviation.modules.transportations.enums.TransportationType;

public interface TransportationRepository extends JpaRepository<Transportation, Long> {

    List<Transportation> findByTransportationTypeAndOriginAndDestination(TransportationType type, Location origin, Location destination);
   
    // For non-flight transportation
    List<Transportation> findByTransportationTypeNotAndOrigin(Location origin, TransportationType type);
   
    List<Transportation> findByTransportationTypeNotAndDestination(Location destination, TransportationType type);
   
    // Generic search by origin and/or destination can be added as needed
    List<Transportation> findByOrigin(Location origin);
    List<Transportation> findByDestination(Location destination);
     
}