package com.example.aviation.modules.transportations.dto.request;

import java.util.List;

import com.example.aviation.modules.transportations.enums.TransportationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportationCreateOrUpdateRequest {

    @NotBlank
    private String originLocationCode;
    @NotBlank
    private String destinationLocationCode;
    @NotNull
    private TransportationType transportationType;
    @NotNull
    @NotEmpty
    private List<Integer> operatingDays;
    
}
