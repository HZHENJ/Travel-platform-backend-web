package com.example.backendweb.Controller;

import com.example.backendweb.DTO.FlightDTO;
import com.example.backendweb.Entity.Info.Flight;
import com.example.backendweb.Services.FlightService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping
    public List<Flight> getFlightsByFilter(@RequestBody FlightDTO flightDTO) {
        return flightService.getFlightsByFilter(flightDTO);
    }
    @GetMapping("/{id}")
    public Flight getFlightById(@PathVariable Long id) {
        return flightService.getFlightById(id);
    }
}
