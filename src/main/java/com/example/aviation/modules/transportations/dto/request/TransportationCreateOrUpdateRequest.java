package com.example.aviation.modules.transportations.dto.request;

import java.util.List;

import com.example.aviation.modules.transportations.enums.TransportationType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportationCreateOrUpdateRequest {

    private String originLocationCode;
    private String destinationLocationCode;
    private TransportationType transportationType;
    private List<Integer> operatingDays;
    
}
