package com.example.backendweb.Controller;

import com.example.backendweb.DTO.FlightDTO;
import com.example.backendweb.Entity.Info.Flight;
import com.example.backendweb.Services.FlightService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/api/flights")
    public List<Flight> getFlightsByFilter(@RequestBody FlightDTO flightDTO) {
        return flightService.getFlightsByFilter(flightDTO);
    }
}
