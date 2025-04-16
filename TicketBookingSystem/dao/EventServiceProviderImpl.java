package dao;

import Entity.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventServiceProviderImpl implements IEventServiceProvider {

    protected List<Event> events = new ArrayList<>();

    @Override
    public Event createEvent(String eventName, String date, String time, int totalSeats, double ticketPrice, String eventType, Venue venue) {
        LocalDate eventDate = LocalDate.parse(date);
        LocalTime eventTime = LocalTime.parse(time);
        Event event = null;

        switch (eventType.toLowerCase()) {
            case "movie":
                event = new Movie(eventName, eventDate, eventTime, venue, totalSeats, ticketPrice, "PG-13", "Christopher Nolan", "Leonardo DiCaprio");
                break;
            case "concert":
                event = new Concert(eventName, eventDate, eventTime, venue, totalSeats, ticketPrice, "Coldplay", "Rock");
                break;
            case "sports":
                event = new Sports(eventName, eventDate, eventTime, venue, totalSeats, ticketPrice, "India", "Australia");
                break;
            default:
                System.out.println("Invalid event type.");
                return null;
        }

        events.add(event);
        return event;
    }

    @Override
    public List<Event> getEventDetails() {
        return events;
    }

    @Override
    public int getAvailableNoOfTickets(String eventName) {
        for (Event e : events) {
            if (e.getEventName().equalsIgnoreCase(eventName)) {
                return e.getAvailableSeats();
            }
        }
        return 0;
    }
}
