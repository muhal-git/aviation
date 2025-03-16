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
public class RouteServiceImpl implements RouteService {

    private final LocationRepository locationRepository;

    private final TransportationRepository transportationRepository;

    public RouteServiceImpl(LocationRepository locationRepository, TransportationRepository transportationRepository) {
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
    public List<RouteResponse> getPossibleRoutes(String originCode, String destinationCode, LocalDate date) {

        List<RouteResponse> routes = new LinkedList<>();

        Optional<Location> originOpt = locationRepository.findByLocationCode(originCode);
        Optional<Location> destinationOpt = locationRepository.findByLocationCode(destinationCode);

        Location origin = originOpt.orElseThrow(() -> new LocationNotFoundExeption("Origin location not found"));
        Location destination = destinationOpt
                .orElseThrow(() -> new LocationNotFoundExeption("Destination location not found"));

        // 1. Direct flight (only one segment)
        addDirectFlightOption(routes, origin, destination, date);

        // 2. Two-segment routes
        // Option A: Before flight transfer + flight
        addTransferThenFlightOptions(routes, origin, destination, date);

        // Option B: Flight + after flight transfer
        addFlightThenTransferOptions(routes, origin, destination, date);

        // 3. Three-segment routes: before transfer + flight + after transfer
        addTransferThenFlightThenTransferOptions(routes, origin, destination, date);

        return routes;
    }

    private void addTransferThenFlightThenTransferOptions(List<RouteResponse> routes, Location origin, Location destination,
            LocalDate date) {

        List<Transportation> possibleFlightTransfers = transportationRepository
                .findByTransportationTypeNotAndOrigin(TransportationType.FLIGHT, origin);
        List<Transportation> possibleTransfersAfterFlight = transportationRepository
                .findByTransportationTypeNotAndDestination(TransportationType.FLIGHT, destination);

        for (Transportation possibleFlightTransfer : possibleFlightTransfers) {
            for (Transportation possibleTransferAfterFlight : possibleTransfersAfterFlight) {
                Location posibleFlightOrigin = possibleFlightTransfer.getDestination();
                Location possibleFlightDestination = possibleTransferAfterFlight.getOrigin();
                Optional<Transportation> possibleFlight = transportationRepository
                        .findByTransportationTypeAndOriginAndDestination(TransportationType.FLIGHT, posibleFlightOrigin,
                                possibleFlightDestination);
                if (possibleFlight.isPresent() && isOperatingOn(possibleFlight.get(), date)
                        && isOperatingOn(possibleFlightTransfer, date)
                        && isOperatingOn(possibleTransferAfterFlight, date)) {
                    TransportationDto transferBeforeFlightDto = mapToDto(possibleFlightTransfer);
                    TransportationDto flightDto = mapToDto(possibleFlight.get());
                    TransportationDto transferAfterFlightDto = mapToDto(possibleTransferAfterFlight);
                    routes.add(new RouteResponse("via " + possibleFlightTransfer.getDestination().getName(),
                            transferBeforeFlightDto, flightDto, transferAfterFlightDto));
                }
            }
        }

    }

    private void addFlightThenTransferOptions(List<RouteResponse> routes, Location origin, Location destination, LocalDate date) {

        List<Transportation> flights = transportationRepository.findByTransportationTypeAndOrigin(
                TransportationType.FLIGHT,
                origin);
        for (Transportation flight : flights) {
            if (!isOperatingOn(flight, date))
                continue;
            List<Transportation> possibleTransfersAfterFlight = transportationRepository
                    .findByTransportationTypeNotAndOriginAndDestination(TransportationType.FLIGHT,
                            flight.getDestination(), destination);
            for (Transportation possibleTransfer : possibleTransfersAfterFlight) {
                if (isOperatingOn(possibleTransfer, date)) {
                    TransportationDto flightDto = mapToDto(flight);
                    TransportationDto transferDto = mapToDto(possibleTransfer);
                    routes.add(new RouteResponse("via " + flight.getDestination().getName(), null, flightDto,
                            transferDto));
                }
            }
        }

    }

    private void addTransferThenFlightOptions(List<RouteResponse> routes, Location origin, Location destination, LocalDate date) {

        List<Transportation> flightTransfers = transportationRepository
                .findByTransportationTypeNotAndOrigin(TransportationType.FLIGHT, origin);
        for (Transportation transportation : flightTransfers) {
            if (!isOperatingOn(transportation, date))
                continue;
            Optional<Transportation> flightToDestinationAfterTransfer = transportationRepository
                    .findByTransportationTypeAndOriginAndDestination(TransportationType.FLIGHT,
                            transportation.getDestination(), destination);
            if (flightToDestinationAfterTransfer.isPresent()
                    && isOperatingOn(flightToDestinationAfterTransfer.get(), date)) {
                TransportationDto transferDto = mapToDto(transportation);
                TransportationDto flightDto = mapToDto(flightToDestinationAfterTransfer.get());
                routes.add(new RouteResponse("via " + flightToDestinationAfterTransfer.get().getOrigin().getName(),
                        transferDto, flightDto, null));
            }

        }
    }

    private void addDirectFlightOption(List<RouteResponse> routes, Location origin, Location destination, LocalDate date) {

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