package com.example.aviation.modules.routes.service;

import java.time.LocalDate;
import java.util.List;

import com.example.aviation.modules.transportations.dto.RouteResponse;

public interface RouteService {

    List<RouteResponse> getPossibleRoutes(String originCode, String destinationCode, LocalDate date);

}