package com.example.backendweb.Services;

import com.example.backendweb.DTO.FlightDTO;
import com.example.backendweb.Entity.Info.Flight;
import com.example.backendweb.Repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final Logger logger = LoggerFactory.getLogger(FlightService.class);

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getFlightsByFilter(FlightDTO flightDTO) {
        try {
            List<Flight> res =  flightRepository.findAll()
                    .stream().filter(
                            flight -> flight.getDepartureCity().equals(flightDTO.getFrom())
                                    && flight.getArrivalCity().equals(flightDTO.getTo())&&
                                    LocalDate.parse(flightDTO.getDate()).equals(flight.getDepartureTime().toLocalDate())).toList();
            logger.warn(res.toString());
            return res;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public Flight getFlightById(Long id) {
        return flightRepository.findById(Math.toIntExact(id)).orElse(null);
    }
}
