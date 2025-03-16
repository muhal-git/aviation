package com.example.aviation.modules.routes.service;

import org.springframework.stereotype.Service;

import com.example.aviation.common.exception.custom.LocationNotFoundExeption;
import com.example.aviation.modules.locations.entity.Location;
import com.example.aviation.modules.locations.repository.LocationRepository;
import com.example.aviation.modules.transportations.dto.RouteResponse;
import com.example.aviation.modules.transportations.dto.TransportationDto;
import com.example.aviation.modules.transportations.entity.Transportation;
import com.example.aviation.modules.transportations.enums.TransportationType;
import com.example.aviation.modules.transportations.repository.TransportationRepository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    private final LocationRepository locationRepository;

    private final TransportationRepository transportationRepository;

    public RouteService(LocationRepository locationRepository, TransportationRepository transportationRepository) {
        this.locationRepository = locationRepository;
        this.transportationRepository = transportationRepository;
    }

    /**
     * Returns a list of valid routes between origin and destination on the given
     * date.
     * The route can have one (direct flight), two (before-flight transfer or
     * after-flight transfer)
     * or three segments (before flight transfer, flight, after flight transfer).
     */
    public List<RouteResponse> getValidRoutes(String originCode, String destinationCode, LocalDate date) {

        List<RouteResponse> routes = new LinkedList<>();

        Optional<Location> originOpt = locationRepository.findByLocationCode(originCode);
        Optional<Location> destinationOpt = locationRepository.findByLocationCode(destinationCode);

        Location origin = originOpt.orElseThrow(() -> new LocationNotFoundExeption("Origin location not found"));
        Location destination = destinationOpt
                .orElseThrow(() -> new LocationNotFoundExeption("Destination location not found"));

        // 1. Direct flight (only one segment)
        addDirectFlight(routes, origin, destination, date);

        // 2. Two-segment routes
        // Option A: Before flight transfer + flight
        List<Transportation> flightTransfers = transportationRepository
                .findByTransportationTypeNotAndOrigin(TransportationType.FLIGHT, origin);
        for (Transportation transportation : flightTransfers) {
            if (!isOperatingOn(transportation, date))
                continue;
            Optional<Transportation> flightToDestination = transportationRepository
                    .findByTransportationTypeAndOriginAndDestination(TransportationType.FLIGHT,
                            transportation.getDestination(), destination);
            if (flightToDestination.isPresent() && isOperatingOn(flightToDestination.get(), date)) {
                TransportationDto transferDto = mapToDto(transportation);
                TransportationDto flightDto = mapToDto(flightToDestination.get());
                routes.add(new RouteResponse("via " + flightToDestination.get().getOrigin().getName(), transferDto, flightDto, null));
            }

        }

        // Option B: Flight + after flight transfer

        // 3. Three-segment routes: before transfer + flight + after transfer

        return routes;
    }

    private void addDirectFlight(List<RouteResponse> routes, Location origin, Location destination, LocalDate date) {

        Optional<Transportation> directFlight = transportationRepository
                .findByTransportationTypeAndOriginAndDestination(TransportationType.FLIGHT, origin, destination);

        if (directFlight.isPresent() && isOperatingOn(directFlight.get(), date)) {
            TransportationDto flightDto = mapToDto(directFlight.get());
            routes.add(new RouteResponse("Direct flight", null, flightDto, null));
        }
    }

    private boolean isOperatingOn(Transportation t, LocalDate date) {
        if (t.getOperatingDays() == null || t.getOperatingDays().isEmpty()) {
            return true;
        }
        // Assuming date.getDayOfWeek().getValue() returns 1 for Monday ... 7 for
        // Sunday.
        int day = date.getDayOfWeek().getValue();
        return t.getOperatingDays().contains(day);
    }

    private TransportationDto mapToDto(Transportation t) {
        return new TransportationDto(t.getOrigin().getName(), t.getDestination().getName(), t.getTransportationType());
    }
}