package Entity;

import java.time.LocalDate;

public class Booking {
    private int bookingId;
    private int customerId;
    private int eventId;
    private int numTickets;
    private double totalCost;
    private LocalDate bookingDate;

    // Constructors, Getters, Setters

    public Booking() {}

    public Booking(int customerId, int eventId, int numTickets, double totalCost, LocalDate bookingDate) {
        this.customerId = customerId;
        this.eventId = eventId;
        this.numTickets = numTickets;
        this.totalCost = totalCost;
        this.bookingDate = bookingDate;
    }

    // Getters & Setters...

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }
}
