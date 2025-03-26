package com.example.aviation.modules.locations.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
// You could add @Repository, but it's not required
// import org.springframework.stereotype.Repository;

import com.example.aviation.modules.locations.entity.Location;

// @Repository annotation is optional here since the interface extends JpaRepository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByLocationCode(String locationCode);

}
