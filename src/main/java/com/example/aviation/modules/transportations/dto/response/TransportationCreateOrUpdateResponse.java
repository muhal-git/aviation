package com.example.aviation.modules.transportations.dto.response;

import java.time.Instant;
import java.util.List;

import com.example.aviation.modules.transportations.enums.TransportationType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Transportation Create Or Update Response")
public class TransportationCreateOrUpdateResponse {

    private Long id;
    private String originLocationCode;
    private String destinationLocationCode;
    private TransportationType transportationType;
    private List<Integer> operatingDays;
    private Instant createdAt;
    private Instant updatedAt;

}
