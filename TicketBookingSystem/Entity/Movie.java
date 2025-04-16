package Entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Movie extends Event {
    private String genre;
    private String director;
    private String leadActor;

    public Movie() {
        // Optional default constructor
    }

    public Movie(String eventName, LocalDate eventDate, LocalTime eventTime, Venue venue,
                 int totalSeats, double ticketPrice, String genre, String director, String leadActor) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.venue = venue;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.ticketPrice = ticketPrice;
        this.eventType = "Movie";
        this.genre = genre;
        this.director = director;
        this.leadActor = leadActor;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getLeadActor() {
        return leadActor;
    }

    public void setLeadActor(String leadActor) {
        this.leadActor = leadActor;
    }

    @Override
    public void displayEventDetails() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "Movie: " + eventName +
                " | Genre: " + genre +
                " | Director: " + director +
                " | Lead Actor: " + leadActor +
                " | Date: " + eventDate +
                " | Time: " + eventTime +
                " | Venue: " + (venue != null ? venue.getVenueName() : "N/A") +
                " | Price: " + ticketPrice +
                " | Available Seats: " + availableSeats;
    }
}
