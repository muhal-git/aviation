package com.example.aviation.modules.routes.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.aviation.modules.routes.service.RouteService;
import com.example.aviation.modules.transportations.dto.RouteResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    /**
     * Get possible routes.
     * 
     * Returns possible routes between origin and destination on the given date.
     * 
     * @param origin      The origin airport code (e.g., SAW).
     * @param destination The destination airport code (e.g., IST).
     * @param date        The date of travel in ISO format (e.g., 2025-03-12).
     * @return A ResponseEntity containing a list of RouteResponse objects.
     * 
     */
    @GetMapping
    @Operation(
        summary = "Get possible routes.", 
        parameters = {
            @Parameter(name = "origin", description = "The origin location code", required = true, example = "SAW"),
            @Parameter(name = "destination", description = "The destination location code", required = true, example = "IST"),
            @Parameter(name = "date", description = "The date of travel in ISO format", required = true, example = "2025-03-17")
        },
        description = "Returns possible routes between origin and destination on the given date.")
    public ResponseEntity<List<RouteResponse>> getRoutes(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(routeService.getPossibleRoutes(origin, destination, date));
    }
}
