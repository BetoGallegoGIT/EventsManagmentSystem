package app;

import java.time.LocalDate;
import java.util.List;

public class EventsManager {

    private static EventsManager instance;

    public static EventsManager getInstance() {
        if (instance == null) {
            instance = new EventsManager();
        }
        return instance;
    }

    // ----------------------------------------------------------------------------- //

    private List<Event> events;

    private EventsManager() {
        this.events = ArchivesManager.uploadEvents();
    }

    public List<Event> getEvents() {
        return events;
    }

    // ----------------------------------------------------------------------------- //

    public void addEvent(String description, LocalDate date, String location) {
        long id = System.currentTimeMillis();
        Event newEvent = new Event(id, description, date, location);
        this.events.add(newEvent);
    }

    public void modifyEvent(Event modifyingEvent, String description, LocalDate date, String location) {

        modifyingEvent.setDescription(description);
        modifyingEvent.setDate(date);
        modifyingEvent.setLocation(location);
    }

    public void removeEvent(Event removeEvent) {
        this.events.remove(removeEvent);
    }

    // ----------------------------------------------------------------------------- //

    public void registerAttendee(Event event, Attendee attendee) {
        event.addAttendee(attendee);
    }

    // ----------------------------------------------------------------------------- //

    public void saveData() {
        ArchivesManager.saveEvents(this.events);
    }

}
