package com.example.aviation.modules.transportations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponse {

    private String description;
    private TransportationDto beforeFlightTransfer;
    private TransportationDto flight;
    private TransportationDto afterFlightTransfer;
    
}
