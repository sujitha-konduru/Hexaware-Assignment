package dao;

import Entity.Event;
import Entity.Venue;

import java.util.List;

public interface IEventServiceProvider {
    Event createEvent(String name, String date, String time, int seats, double price, String type, Venue venue);
    List<Event> getEventDetails();
    int getAvailableNoOfTickets(String eventName);
}
