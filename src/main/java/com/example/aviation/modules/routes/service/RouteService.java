package com.example.aviation.modules.routes.service;

import org.springframework.stereotype.Service;

import com.example.aviation.modules.locations.entity.Location;
import com.example.aviation.modules.locations.repository.LocationRepository;
import com.example.aviation.modules.transportations.dto.RouteResponse;
import com.example.aviation.modules.transportations.dto.TransportationDto;
import com.example.aviation.modules.transportations.entity.Transportation;
import com.example.aviation.modules.transportations.enums.TransportationType;
import com.example.aviation.modules.transportations.repository.TransportationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
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
        List<RouteResponse> routes = new ArrayList<>();

        Optional<Location> originOpt = locationRepository.findByLocationCode(originCode);
        Optional<Location> destinationOpt = locationRepository.findByLocationCode(destinationCode);

        if (!originOpt.isPresent() || !destinationOpt.isPresent()) {
            return routes; // or throw exception if locations not found
        }

        Location origin = originOpt.get();
        Location destination = destinationOpt.get();

        // 1. Direct flight (only one segment)
        List<Transportation> directFlights = transportationRepository
                .findByTransportationTypeAndOriginAndDestination(TransportationType.FLIGHT, origin, destination);
        for (Transportation flight : directFlights) {
            if (!isOperatingOn(flight, date))
                continue;
            TransportationDto flightDto = mapToDto(flight);
            routes.add(new RouteResponse("Direct flight", null, flightDto, null));
        }

        // 2. Two-segment routes
        // Option A: Before flight transfer + flight
        List<Transportation> beforeTransfers = transportationRepository.findByTransportationTypeNotAndOrigin(
                TransportationType.FLIGHT, origin);
        for (Transportation before : beforeTransfers) {
            if (!isOperatingOn(before, date))
                continue;
            // The destination of before transfer is intermediate
            Location intermediate = before.getDestination();
            List<Transportation> flights = transportationRepository.findByTransportationTypeAndOriginAndDestination(
                    TransportationType.FLIGHT, intermediate, destination);
            for (Transportation flight : flights) {
                if (!isOperatingOn(flight, date))
                    continue;
                TransportationDto beforeDto = mapToDto(before);
                TransportationDto flightDto = mapToDto(flight);
                routes.add(new RouteResponse("via " + intermediate.getName(), beforeDto, flightDto, null));
            }
        }

        // Option B: Flight + after flight transfer
        List<Transportation> flightsFromOrigin = transportationRepository
                .findByTransportationTypeAndOriginAndDestination(TransportationType.FLIGHT, origin, null);
        // Alternatively, fetch all flights originating from origin and then filter by
        // destination match.
        List<Transportation> allFlightsFromOrigin = transportationRepository.findByOrigin(origin);
        for (Transportation flight : allFlightsFromOrigin) {
            if (flight.getTransportationType() != TransportationType.FLIGHT)
                continue;
            if (!isOperatingOn(flight, date))
                continue;
            Location intermediate = flight.getDestination();
            // Find non-flight transportation from intermediate to destination
            List<Transportation> afterTransfers = transportationRepository
                    .findByTransportationTypeNotAndDestination(TransportationType.FLIGHT, destination);
            for (Transportation after : afterTransfers) {
                if (!after.isOriginMatch(intermediate)) {
                    // custom check: ensure that the origin of the afterTransfer equals the
                    // intermediate from flight
                    continue;
                }
                if (!isOperatingOn(after, date))
                    continue;
                TransportationDto flightDto = mapToDto(flight);
                TransportationDto afterDto = mapToDto(after);
                routes.add(new RouteResponse("via " + intermediate.getName(), null, flightDto, afterDto));
            }
        }

        // 3. Three-segment routes: before transfer + flight + after transfer
        for (Transportation before : beforeTransfers) {
            if (!isOperatingOn(before, date))
                continue;
            Location intermediate1 = before.getDestination();
            // Find flights from intermediate1 (mandatory flight)
            List<Transportation> flights = transportationRepository
                    .findByTransportationTypeAndOriginAndDestination(TransportationType.FLIGHT, intermediate1, null);
            for (Transportation flight : flights) {
                if (!isOperatingOn(flight, date))
                    continue;
                Location intermediate2 = flight.getDestination();
                // Find after transfer from intermediate2 to destination
                List<Transportation> afterTransfers = transportationRepository
                        .findByTransportationTypeNotAndDestination(TransportationType.FLIGHT, destination);
                for (Transportation after : afterTransfers) {
                    if (!after.isOriginMatch(intermediate2))
                        continue;
                    if (!isOperatingOn(after, date))
                        continue;
                    TransportationDto beforeDto = mapToDto(before);
                    TransportationDto flightDto = mapToDto(flight);
                    TransportationDto afterDto = mapToDto(after);
                    routes.add(new RouteResponse("via " + intermediate1.getName() + " and " + intermediate2.getName(),
                            beforeDto, flightDto, afterDto));
                }
            }
        }

        // Additional filtering: ensure each route has exactly one flight,
        // not more than one before or after transfers. The above loops already keep
        // segments limited.

        return routes;
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