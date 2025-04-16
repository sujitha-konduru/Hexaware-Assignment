package Entity;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class Event {
    protected int eventId;
    protected String eventName;
    protected LocalDate eventDate;
    protected LocalTime eventTime;
    protected int totalSeats;
    protected int availableSeats;
    protected double ticketPrice;
    protected String eventType;
    protected Venue venue;

    // Getters and Setters
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public LocalTime getEventTime() { return eventTime; }
    public void setEventTime(LocalTime eventTime) { this.eventTime = eventTime; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }

    public abstract void displayEventDetails();

    @Override
    public String toString() {
        return "Event ID: " + eventId +
                "\nName: " + eventName +
                "\nDate: " + eventDate +
                "\nTime: " + eventTime +
                "\nType: " + eventType +
                "\nTotal Seats: " + totalSeats +
                "\nAvailable Seats: " + availableSeats +
                "\nTicket Price: ₹" + ticketPrice +
                (venue != null ? ("\nVenue: " + venue.getVenueName() + " | " + venue.getAddress()) : "");
    }
}
