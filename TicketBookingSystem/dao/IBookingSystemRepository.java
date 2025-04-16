package dao;

import Entity.Customer;
import Entity.Event;
import Entity.Venue;
import exception.EventNotFoundException;
import exception.InvalidBookingIDException;

import java.sql.SQLException;
import java.util.List;

public interface IBookingSystemRepository {

    // Create a new event
    void create_event(String name, String date, String time, int totalSeats, double ticketPrice, String type, Venue venue);

    // Book tickets for a given event and list of customers
    int book_tickets(String eventName, int numTickets, List<Customer> customers) throws SQLException;

    // Cancel a booking based on booking ID
    boolean cancel_booking(int bookingId) throws SQLException;

    // Get number of available tickets for a specific event
    int get_available_tickets(String eventName) throws EventNotFoundException;

    // Get list of all event details
    List<Event> get_event_details();
}
