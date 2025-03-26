package com.example.aviation.modules.transportations.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aviation.modules.transportations.dto.request.TransportationCreateOrUpdateRequest;
import com.example.aviation.modules.transportations.dto.response.TransportationCreateOrUpdateResponse;
import com.example.aviation.modules.transportations.service.TransportationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transportations")
@RequiredArgsConstructor
public class TransportationController {

    private final TransportationService transportationService;

    @GetMapping
    public ResponseEntity<List<TransportationCreateOrUpdateResponse>> getAllTransportations() {
        return ResponseEntity.ok(transportationService.getAllTransportations());
    }

    @PostMapping
    public ResponseEntity<TransportationCreateOrUpdateResponse> createTransportation(
            @RequestBody @Valid TransportationCreateOrUpdateRequest transportation) {
        return ResponseEntity.ok(transportationService.createTransportation(transportation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportationCreateOrUpdateResponse> updateTransportation(@PathVariable Long id,
            @RequestBody @Valid TransportationCreateOrUpdateRequest updated) {
        return ResponseEntity.ok(transportationService.updateTransportation(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTransportation(@PathVariable Long id) {
        transportationService.deleteTransportation(id);
        return ResponseEntity.noContent().build();
    }

}
