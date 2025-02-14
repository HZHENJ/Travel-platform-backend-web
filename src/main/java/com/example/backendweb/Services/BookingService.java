package com.example.backendweb.Services;

import com.example.backendweb.Entity.Booking.Booking;
import com.example.backendweb.Repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Integer bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();

            // 1. Update Inventory (Crucial: Do this BEFORE deleting the booking!)
            updateInventory(booking); // Implement this method

            // 2. Delete the booking
            bookingRepository.delete(booking);

            // 3. Notify User (Implement your notification logic here)
            notifyUser(booking); // Implement this method

        } else {
            throw new EntityNotFoundException("Booking not found with ID: " + bookingId);
        }
    }

    private void updateInventory(Booking booking) {
        // Your inventory update logic here.  Example:
        if (booking.getBookingType() == Booking.BookingType.Flight) {
            // Example:  Assume you have a FlightInventoryRepository and FlightInventory entity
            // FlightInventory flightInventory = flightInventoryRepository.findByFlightId(booking.getFlightId()); // Assuming you have a flightId in your Booking entity
            // if (flightInventory != null) {
            // flightInventory.setAvailableSeats(flightInventory.getAvailableSeats() + booking.getNumberOfSeats()); // Or however you manage inventory
            // flightInventoryRepository.save(flightInventory);
            // }
        } else if (booking.getBookingType() == Booking.BookingType.Hotel) {
            // Similar logic for hotel inventory
        } else if (booking.getBookingType() == Booking.BookingType.Attraction) {
            // Similar logic for attraction inventory
        }
        // ... handle other booking types
    }

    private void notifyUser(Booking booking){
        // Your user notification logic here. Example:
        System.out.println("Notifying user about cancellation of booking: " + booking.getBookingId());
        // You would typically use a notification service (email, SMS, etc.) here.
    }

    public Booking updateBooking(Integer bookingId, Booking booking) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        // Update the fields you want to allow updating.  Don't just set the whole object!
        existingBooking.setUserId(booking.getUserId());
        existingBooking.setBookingType(booking.getBookingType());
        existingBooking.setStatus(booking.getStatus());
        existingBooking.setTotalAmount(booking.getTotalAmount());
        // ... update other fields ...

        return bookingRepository.save(existingBooking);
    }

}