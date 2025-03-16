package com.example.aviation.modules.transportations.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Response object for route details")
public class RouteResponse {

    @Schema(description = "String description of the route")
    private String description;
    @Schema(description = "Transportation details for the transfer before the flight")
    private TransportationDto beforeFlightTransfer;
    @Schema(description = "Transportation details for the flight")
    private TransportationDto flight;
    @Schema(description = "Transportation details for the transfer after the flight")
    private TransportationDto afterFlightTransfer;
    
}
