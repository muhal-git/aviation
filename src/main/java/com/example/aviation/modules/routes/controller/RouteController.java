package com.example.aviation.modules.routes.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.example.aviation.modules.routes.service.RouteService;
import com.example.aviation.modules.transportations.dto.RouteResponse;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {


    private final RouteService routeService;

    /**
     * Example request:
     * GET /api/routes?origin=SAW&destination=IST&date=2025-03-12
     */
    @GetMapping
    public List<RouteResponse> getRoutes(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return routeService.getValidRoutes(origin, destination, date);
    }
}
