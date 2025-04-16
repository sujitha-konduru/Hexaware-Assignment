package Entity;

public class Venue {
    private int venueId;
    private String venueName;
    private String address;

    public Venue(String venueName, String address) {
        this.venueName = venueName;
        this.address = address;
    }

    public Venue() {}

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
