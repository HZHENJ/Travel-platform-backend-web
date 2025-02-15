package com.example.backendweb.Repository;

import com.example.backendweb.Entity.Info.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
}
