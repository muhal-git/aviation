package com.example.aviation.modules.transportations.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aviation.modules.locations.entity.Location;
import com.example.aviation.modules.transportations.entity.Transportation;
import com.example.aviation.modules.transportations.enums.TransportationType;

public interface TransportationRepository extends JpaRepository<Transportation, Long> {

    Optional<Transportation> findByTransportationTypeAndOriginAndDestination(TransportationType type, Location origin, Location destination);
   
    // For non-flight transportation
    List<Transportation> findByTransportationTypeNotAndOrigin(TransportationType type, Location origin);
    List<Transportation> findByTransportationTypeNotAndDestination(TransportationType type, Location destination);

    // For flight transportation from origin
    List<Transportation> findByTransportationTypeAndOrigin(TransportationType type, Location origin);

    // For non-flight transportation from origin to destination
    List<Transportation> findByTransportationTypeNotAndOriginAndDestination(TransportationType type, Location origin, Location destination);
   
    // Generic search by origin and/or destination can be added as needed
    List<Transportation> findByOrigin(Location origin);
    List<Transportation> findByDestination(Location destination);
     
}