package com.example.aviation.modules.routes.controller;

import com.example.aviation.modules.routes.service.RouteService;
import com.example.aviation.modules.transportations.dto.RouteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RouteControllerTest {

    @Mock
    private RouteService routeService;

    @InjectMocks
    private RouteController routeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(routeController).build();
    }

    @Test
    void testGetRoutes() throws Exception {
        String origin = "SAW";
        String destination = "DLA";
        LocalDate date = LocalDate.of(2025, 3, 12);

        List<RouteResponse> expectedRoutes = Collections.singletonList(new RouteResponse());
        when(routeService.getPossibleRoutes(origin, destination, date)).thenReturn(expectedRoutes);

        ResponseEntity<List<RouteResponse>> response = routeController.getRoutes(origin, destination, date);

        assertEquals(ResponseEntity.ok(expectedRoutes), response);

        mockMvc.perform(get("/api/routes")
                .param("origin", origin)
                .param("destination", destination)
                .param("date", date.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetRoutesWithNullOrigin() throws Exception {
        String origin = null; // Invalid origin
        String destination = "DLA";
        LocalDate date = LocalDate.of(2025, 3, 12);

        mockMvc.perform(get("/api/routes")
                .param("origin", origin)
                .param("destination", destination)
                .param("date", date.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetRoutesWithNullDestination() throws Exception {
        String origin = "SAW";
        String destination = null; // Invalid destination
        LocalDate date = LocalDate.of(2025, 3, 12);

        mockMvc.perform(get("/api/routes")
                .param("origin", origin)
                .param("destination", destination)
                .param("date", date.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetRoutesBadRequestInvalidDate() throws Exception {
        String origin = "SAW";
        String destination = "DLA";
        String date = "invalid-date"; // Invalid date

        mockMvc.perform(get("/api/routes")
                .param("origin", origin)
                .param("destination", destination)
                .param("date", date))
                .andExpect(status().isBadRequest());
    }

}
