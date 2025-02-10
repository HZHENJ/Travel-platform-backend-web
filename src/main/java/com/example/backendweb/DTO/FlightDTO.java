package com.example.backendweb.DTO;

import lombok.Data;


@Data
public class FlightDTO {
    String from;
    String to;
    String date;
    int passengers;
}
