package dao;

import Entity.*;
import exception.EventNotFoundException;
import exception.InvalidBookingIDException;
import util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingSystemRepositoryImpl implements IBookingSystemRepository {

	@Override
	public void create_event(String name, String date, String time, int totalSeats, double ticketPrice, String type, Venue venue) {
	    String selectVenue = "SELECT venue_id FROM venue WHERE venue_name = ?";
	    String insertVenue = "INSERT INTO venue (venue_name, address) VALUES (?, ?)";
	    String insertEvent = "INSERT INTO event (event_name, event_date, event_time, venue_id, total_seats, available_seats, ticket_price, event_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	    try (Connection conn = DBUtil.getConnection()) {

	        int venueId = -1;

	        // Step 1: Check if venue exists
	        try (PreparedStatement psSelectVenue = conn.prepareStatement(selectVenue)) {
	            psSelectVenue.setString(1, venue.getVenueName());
	            ResultSet rs = psSelectVenue.executeQuery();
	            if (rs.next()) {
	                venueId = rs.getInt("venue_id");
	            } else {
	                // Step 2: Insert default venue if not found
	                try (PreparedStatement psInsertVenue = conn.prepareStatement(insertVenue, Statement.RETURN_GENERATED_KEYS)) {
	                    psInsertVenue.setString(1, venue.getVenueName());
	                    psInsertVenue.setString(2, venue.getAddress());
	                    psInsertVenue.executeUpdate();

	                    ResultSet venueKeys = psInsertVenue.getGeneratedKeys();
	                    if (venueKeys.next()) {
	                        venueId = venueKeys.getInt(1);
	                    }
	                }
	            }
	        }

	        // Step 3: Insert event
	        try (PreparedStatement stmt = conn.prepareStatement(insertEvent)) {
	            stmt.setString(1, name);
	            stmt.setDate(2, Date.valueOf(date));
	            stmt.setTime(3, Time.valueOf(time));
	            stmt.setInt(4, venueId);
	            stmt.setInt(5, totalSeats);
	            stmt.setInt(6, totalSeats); // availableSeats = totalSeats initially
	            stmt.setDouble(7, ticketPrice);
	            stmt.setString(8, type);

	            stmt.executeUpdate();
	            System.out.println("Event created successfully.");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

    
	public int book_tickets(String eventName, int numTickets, List<Customer> customers) throws SQLException {
	    Connection conn = DBUtil.getConnection();
	    conn.setAutoCommit(false); // Start transaction

	    try {
	        // Step 1: Get event details
	        PreparedStatement findEvent = conn.prepareStatement(
	            "SELECT event_id, available_seats, ticket_price FROM event WHERE event_name = ?");
	        findEvent.setString(1, eventName);
	        ResultSet rs = findEvent.executeQuery();

	        if (!rs.next()) {
	            System.out.println("Event not found: " + eventName);
	            return -1;
	        }

	        int eventId = rs.getInt("event_id");
	        int availableSeats = rs.getInt("available_seats");
	        double ticketPrice = rs.getDouble("ticket_price");

	        if (availableSeats < numTickets) {
	            System.out.println("Only " + availableSeats + " tickets available.");
	            return -1;
	        }

	        if (customers.size() != numTickets) {
	            System.out.println("Number of customers must equal number of tickets.");
	            return -1;
	        }

	        // Step 2: Prepare SQL statements
	        String insertCustomerSQL = "INSERT INTO customer1 (customer_name, email, phone_number) VALUES (?, ?, ?)";
	        PreparedStatement customerStmt = conn.prepareStatement(insertCustomerSQL, Statement.RETURN_GENERATED_KEYS);

	        String insertBookingSQL = "INSERT INTO booking (customer_id, event_id, num_tickets, total_cost, booking_date) VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement bookingStmt = conn.prepareStatement(insertBookingSQL, Statement.RETURN_GENERATED_KEYS);

	        String updateCustomerBookingSQL = "UPDATE customer1 SET booking_id = ? WHERE customer_id = ?";
	        PreparedStatement updateCustomerStmt = conn.prepareStatement(updateCustomerBookingSQL);

	        LocalDate bookingDate = LocalDate.now();
	        int lastBookingId = -1;

	        for (Customer c : customers) {
	            // Step 3a: Insert customer
	            customerStmt.setString(1, c.getCustomerName());
	            customerStmt.setString(2, c.getEmail());
	            customerStmt.setString(3, c.getPhoneNumber());
	            customerStmt.executeUpdate();

	            ResultSet custRs = customerStmt.getGeneratedKeys();
	            int customerId = -1;
	            if (custRs.next()) {
	                customerId = custRs.getInt(1);
	            } else {
	                System.out.println("Failed to insert customer.");
	                conn.rollback();
	                return -1;
	            }

	            // Step 3b: Insert booking for that customer
	            bookingStmt.setInt(1, customerId);
	            bookingStmt.setInt(2, eventId);
	            bookingStmt.setInt(3, 1); // 1 ticket per customer
	            bookingStmt.setDouble(4, ticketPrice);
	            bookingStmt.setDate(5, java.sql.Date.valueOf(bookingDate));
	            bookingStmt.executeUpdate();

	            ResultSet bookingRs = bookingStmt.getGeneratedKeys();
	            int bookingId = -1;
	            if (bookingRs.next()) {
	                bookingId = bookingRs.getInt(1);
	                lastBookingId = bookingId;
	            } else {
	                System.out.println("Failed to insert booking.");
	                conn.rollback();
	                return -1;
	            }

	            // Step 3c: Update customer with booking_id
	            updateCustomerStmt.setInt(1, bookingId);
	            updateCustomerStmt.setInt(2, customerId);
	            updateCustomerStmt.executeUpdate();
	        }

	        // Step 4: Update event seat count
	        PreparedStatement updateSeats = conn.prepareStatement(
	            "UPDATE event SET available_seats = available_seats - ? WHERE event_id = ?");
	        updateSeats.setInt(1, numTickets);
	        updateSeats.setInt(2, eventId);
	        updateSeats.executeUpdate();

	        conn.commit();
	        System.out.println("✅ Bookings successful. Last booking ID: " + lastBookingId);
	        return lastBookingId;

	    } catch (Exception e) {
	        conn.rollback();
	        e.printStackTrace();
	        return -1;
	    } finally {
	        conn.setAutoCommit(true);
	    }
	}

    
    
	@Override
	public boolean cancel_booking(int bookingId) throws SQLException {
	    Connection conn = DBUtil.getConnection();

	    // Step 1: Get the customer_id(s) associated with the booking
	    PreparedStatement getCustomerStmt = conn.prepareStatement(
	        "SELECT customer_id FROM booking WHERE booking_id = ?");
	    getCustomerStmt.setInt(1, bookingId);
	    ResultSet rs = getCustomerStmt.executeQuery();

	    List<Integer> customerIds = new ArrayList<>();
	    while (rs.next()) {
	        customerIds.add(rs.getInt("customer_id"));
	    }

	    if (customerIds.isEmpty()) {
	        System.out.println("No booking found with ID: " + bookingId);
	        return false;
	    }

	    // Step 2: Delete from booking table
	    PreparedStatement deleteBookingStmt = conn.prepareStatement(
	        "DELETE FROM booking WHERE booking_id = ?");
	    deleteBookingStmt.setInt(1, bookingId);
	    int rowsDeleted = deleteBookingStmt.executeUpdate();

	    if (rowsDeleted == 0) {
	        System.out.println("Failed to cancel booking.");
	        return false;
	    }

	    // Step 3 (optional): Delete customers linked to this booking
	    PreparedStatement deleteCustomerStmt = conn.prepareStatement(
	        "DELETE FROM customer1 WHERE customer_id = ?");
	    for (int customerId : customerIds) {
	        deleteCustomerStmt.setInt(1, customerId);
	        deleteCustomerStmt.executeUpdate();
	    }

	    System.out.println("Booking and associated customer(s) cancelled successfully.");
	    return true;
	}

    

    @Override
    public int get_available_tickets(String eventName) throws EventNotFoundException {
        String sql = "SELECT available_seats FROM event WHERE event_name = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, eventName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("available_seats");
            } else {
                throw new EventNotFoundException("Event not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Event> get_event_details() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM event";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String type = rs.getString("event_type");
                Event e;

                switch (type.toLowerCase()) {
                    case "movie":
                        e = new Movie();
                        break;
                    case "concert":
                        e = new Concert();
                        break;
                    case "sports":
                        e = new Sports();
                        break;
                    default:
                        e = new Movie(); // fallback
                }

                e.setEventId(rs.getInt("event_id"));
                e.setEventName(rs.getString("event_name"));
                e.setEventDate(rs.getDate("event_date").toLocalDate());
                e.setEventTime(rs.getTime("event_time").toLocalTime());
                e.setTotalSeats(rs.getInt("total_seats"));
                e.setAvailableSeats(rs.getInt("available_seats"));
                e.setTicketPrice(rs.getDouble("ticket_price"));
                e.setEventType(type);
                events.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
}
