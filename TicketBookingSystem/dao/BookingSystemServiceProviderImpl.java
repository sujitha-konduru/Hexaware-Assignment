package dao;
import java.sql.SQLException;
import Entity.Customer;
import Entity.Event;
import Entity.Venue;
import exception.EventNotFoundException;
import exception.InvalidBookingIDException;

import java.sql.SQLException;
import java.util.List;

public class BookingSystemServiceProviderImpl implements IBookingSystemServiceProvider {
    private final IBookingSystemRepository repository = new BookingSystemRepositoryImpl();

    @Override
    public void createEvent(String name, String date, String time, int totalSeats, double ticketPrice, String type, Venue venue) {
        repository.create_event(name, date, time, totalSeats, ticketPrice, type, venue);
    }

    @Override
    public int bookTickets(String eventName, int numberOfTickets, List<Customer> customers) throws EventNotFoundException, SQLException {
        return repository.book_tickets(eventName, numberOfTickets, customers);
    }
    @Override

    public boolean cancelBooking(int bookingId) throws SQLException {
        return repository.cancel_booking(bookingId); // Forwarding the return from repository
    }
    @Override
    public int getAvailableNoOfTickets(String eventName) throws EventNotFoundException {
        return repository.get_available_tickets(eventName);
    }

    @Override
    public List<Event> getEventDetails() {
        return repository.get_event_details();
    }
}
