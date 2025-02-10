package com.example.backendweb.Services;

import com.example.backendweb.DTO.FlightDTO;
import com.example.backendweb.Entity.Info.Flight;
import com.example.backendweb.Repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getFlightsByFilter(FlightDTO flightDTO) {
        return flightRepository.findAll()
                .stream().filter(
                        flight -> flight.getDepartureCity().equals(flightDTO.getFrom())
                        && flight.getArrivalCity().equals(flightDTO.getTo())&&
                                LocalDate.parse(flightDTO.getDate()).equals(flight.getDepartureTime().toLocalDate())).toList();

    }
}
