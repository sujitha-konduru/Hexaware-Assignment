package Main;

import Entity.Customer;
import Entity.Event;
import Entity.Venue;
import dao.BookingSystemServiceProviderImpl;
import dao.IEventServiceProvider;
import dao.EventServiceProviderImpl;
import exception.EventNotFoundException;

import java.sql.SQLException;
import java.util.*;

public class TicketBookingSystem {

    private static final Scanner scanner = new Scanner(System.in);
    private static final BookingSystemServiceProviderImpl bookingService = new BookingSystemServiceProviderImpl();
    private static final IEventServiceProvider eventService = new EventServiceProviderImpl();

    public static void main(String[] args) throws SQLException {
        int choice;

        do {
            System.out.println("\n--- Ticket Booking System Menu ---");
            System.out.println("1. Create Event");
            System.out.println("2. Book Tickets");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Available Tickets");
            System.out.println("5. View All Event Details");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createEvent();
                    break;
                case 2:
                    bookTickets();
                    break;
                case 3:
                    cancelBooking();
                    break;
                case 4:
                    getAvailableTickets();
                    break;
                case 5:
                    listAllEventDetails();
                    break;
                case 6:
                    System.out.println("Thank you for using the Ticket Booking System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please choose between 1-6.");
            }

        } while (choice != 6);
    }

    private static void createEvent() {
        System.out.print("Enter event name: ");
        String name = scanner.nextLine();

        System.out.print("Enter event date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        System.out.print("Enter event time (HH:MM:SS): ");
        String time = scanner.nextLine();

        System.out.print("Enter total seats: ");
        int totalSeats = scanner.nextInt();

        System.out.print("Enter ticket price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter event type (movie/concert/sports): ");
        String type = scanner.nextLine();

        System.out.print("Enter venue name: ");
        String venueName = scanner.nextLine();

        System.out.print("Enter venue address: ");
        String venueAddress = scanner.nextLine();

        Venue venue = new Venue(venueName, venueAddress);
        
        bookingService.createEvent(name, date, time, totalSeats, price, type, venue);

        System.out.println("Event created successfully.");
    }

    private static void bookTickets() throws SQLException {
        try {
            System.out.print("Enter event name to book tickets: ");
            String eventName = scanner.nextLine();

            System.out.print("Enter number of tickets to book: ");
            int numTickets = scanner.nextInt();
            scanner.nextLine(); // consume newline

            List<Customer> customers = new ArrayList<>();
            for (int i = 0; i < numTickets; i++) {
                System.out.println("Enter details for Customer " + (i + 1));
                System.out.print("Name: ");
                String name = scanner.nextLine();

                System.out.print("Email: ");
                String email = scanner.nextLine();

                System.out.print("Phone number: ");
                String phone = scanner.nextLine();

                customers.add(new Customer(name, email, phone));
            }

            int bookingId = bookingService.bookTickets(eventName, numTickets, customers);
            System.out.println("Booking successful. Booking ID: " + bookingId);

        } catch (EventNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void cancelBooking() {
        System.out.print("Enter Booking ID to cancel: ");
        int bookingId = scanner.nextInt();

        try {
            boolean success = bookingService.cancelBooking(bookingId);
            if (success) {
                System.out.println("Booking cancelled successfully.");
            } else {
                System.out.println("Booking cancellation failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while cancelling booking: " + e.getMessage());
        }
    }

    private static void getAvailableTickets() {
        try {
            System.out.print("Enter event name: ");
            String eventName = scanner.nextLine();
            int available = bookingService.getAvailableNoOfTickets(eventName);
            System.out.println("Available tickets: " + available);
        } catch (EventNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void listAllEventDetails() {
        List<Event> details = bookingService.getEventDetails();
        if (details.isEmpty()) {
            System.out.println("No events found.");
        } else {
            System.out.println("--- Event Details ---");
            for (Event d : details) {
                System.out.println(d);
                System.out.println("---------------------");
            }
        }
    }
}
