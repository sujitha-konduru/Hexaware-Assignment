package Entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Sports extends Event {
    private String teamA;
    private String teamB;

    public Sports() {
    }

    public Sports(String eventName, LocalDate eventDate, LocalTime eventTime, Venue venue,
                  int totalSeats, double ticketPrice, String teamA, String teamB) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.venue = venue;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.ticketPrice = ticketPrice;
        this.eventType = "Sports";
        this.teamA = teamA;
        this.teamB = teamB;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    @Override
    public void displayEventDetails() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "Sports Event: " + eventName +
                " | Teams: " + teamA + " vs " + teamB +
                " | Date: " + eventDate +
                " | Time: " + eventTime +
                " | Venue: " + (venue != null ? venue.getVenueName() : "N/A") +
                " | Price: " + ticketPrice +
                " | Available Seats: " + availableSeats;
    }
}
