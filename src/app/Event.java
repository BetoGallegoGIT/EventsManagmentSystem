package app;

import java.time.LocalDate;
import java.util.ArrayList;

public class Event {

    // ----------------------------------------------------------------------------- //

    private long id;
    private String description;
    private LocalDate date;
    private String location;
    private ArrayList<Attendee> attendees;

    // ----------------------------------------------------------------------------- //

    public Event(long id, String description, LocalDate date, String location) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.location = location;
        this.attendees = new ArrayList<>();
    }

    // ----------------------------------------------------------------------------- //

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // ----------------------------------------------------------------------------- //

    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }

    public void addAttendee(Attendee attendee) {
        attendees.add(attendee);
    }

    public void removeAttendee(Attendee attendee) {
        attendees.remove(attendee);
    }

    // ----------------------------------------------------------------------------- //

    @Override
    public String toString() {
        return this.id + " | " + this.description + " | " + this.date + " | " + this.location;
    }

    // ----------------------------------------------------------------------------- //

}
