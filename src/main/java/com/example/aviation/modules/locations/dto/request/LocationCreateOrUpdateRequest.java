package com.example.aviation.modules.locations.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Location Create Or Update Request")
public class LocationCreateOrUpdateRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 3, max = 3, message = "Country code should be in Alpha-3 format")
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    @Size(min = 3, max = 5)
    private String locationCode;

}
