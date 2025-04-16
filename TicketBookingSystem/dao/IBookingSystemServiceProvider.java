package dao;

import Entity.Customer;
import Entity.Event;
import Entity.Venue;
import exception.EventNotFoundException;
import exception.InvalidBookingIDException;

import java.sql.SQLException;
import java.util.List;

public interface IBookingSystemServiceProvider {
    void createEvent(String name, String date, String time, int totalSeats, double ticketPrice, String type, Venue venue);
    int bookTickets(String eventName, int numberOfTickets, List<Customer> customers) throws EventNotFoundException, SQLException;
    boolean cancelBooking(int bookingId) throws InvalidBookingIDException, SQLException;
    int getAvailableNoOfTickets(String eventName) throws EventNotFoundException;
    List<Event> getEventDetails(); // <- FIXED HERE
}
