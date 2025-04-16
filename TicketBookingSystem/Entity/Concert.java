package Entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Concert extends Event {
    private String performer;
    private String genre;

    public Concert() {
    }

    public Concert(String eventName, LocalDate eventDate, LocalTime eventTime, Venue venue,
                   int totalSeats, double ticketPrice, String performer, String genre) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.venue = venue;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.ticketPrice = ticketPrice;
        this.eventType = "Concert";
        this.performer = performer;
        this.genre = genre;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public void displayEventDetails() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "Concert: " + eventName +
                " | Performer: " + performer +
                " | Genre: " + genre +
                " | Date: " + eventDate +
                " | Time: " + eventTime +
                " | Venue: " + (venue != null ? venue.getVenueName() : "N/A") +
                " | Price: " + ticketPrice +
                " | Available Seats: " + availableSeats;
    }
}
