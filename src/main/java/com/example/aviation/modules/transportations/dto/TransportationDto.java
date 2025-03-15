package com.example.aviation.modules.transportations.dto;

import com.example.aviation.modules.transportations.enums.TransportationType;

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
public class TransportationDto {

    private String origin;
    private String destination;
    private TransportationType transportationType;
    
}
