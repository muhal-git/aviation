package com.example.aviation.modules.locations.dto.response;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Location Create Or Update Response")
public class LocationCreateOrUpdateResponse {

    private Long id;
    private String name;
    private String country;
    private String city;
    private String locationCode;
    private Instant createdAt;
    private Instant updatedAt;

}
