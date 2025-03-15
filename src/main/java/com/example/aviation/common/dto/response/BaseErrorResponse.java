package com.example.aviation.common.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseErrorResponse {

    private String errorMessage;

    @Builder.Default
    private Instant timestamp = Instant.now();

    public BaseErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
     
}
