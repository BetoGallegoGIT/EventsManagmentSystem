package app;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchivesManager {

    private static final String EVENTS_FILE = "events.txt";
    private static final String ATTENDEES_FILE = "attendees.txt";

    public static List<Event> uploadEvents() {
        List<Event> events = new ArrayList<>();
        Map<Long, Event> eventsMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(EVENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                try {
                    String[] parts = line.split(",", 4);
                    long id = Long.parseLong(parts[0]);
                    String description = parts[1];
                    LocalDate date = LocalDate.parse(parts[2]);
                    String location = parts[3];

                    Event event = new Event(id, description, date, location);
                    eventsMap.put(id, event);
                    events.add(event);

                } catch (DateTimeParseException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("WARNING: Error parsing event line (skipping): " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File events.txt not found. Starting fresh.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (events.isEmpty()) return events;

        try (BufferedReader br = new BufferedReader(new FileReader(ATTENDEES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                try {
                    String[] parts = line.split(",", 5);
                    long eventId = Long.parseLong(parts[0]);
                    String name = parts[1];
                    String email = parts[2];
                    long dni = Long.parseLong(parts[3]);
                    long phone = Long.parseLong(parts[4]);

                    Attendee attendee = new Attendee(name, email, dni, phone);

                    Event event = eventsMap.get(eventId);
                    if (event != null) {
                        event.getAttendees().add(attendee);
                    } else {
                        System.err.println("WARNING: Orphan attendee found for Event ID " + eventId);
                    }
                } catch (Exception e) {
                    System.err.println("WARNING: Error parsing attendee line (skipping): " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("attendees.txt does not exist yet. It will be created on save.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return events;
    }

    public static void saveEvents(List<Event> events) {
        try (
                BufferedWriter bwEvents = new BufferedWriter(new FileWriter(EVENTS_FILE));
                BufferedWriter bwAttendees = new BufferedWriter(new FileWriter(ATTENDEES_FILE))
        ) {

            for (Event event : events) {
                // Reemplazamos comas por espacios para no romper el CSV
                String cleanDescription = event.getDescription().replace(",", " ");
                String cleanLocation = event.getLocation().replace(",", " ");

                String eventLine = String.format("%d,%s,%s,%s",
                        event.getId(),
                        cleanDescription,
                        event.getDate().toString(),
                        cleanLocation);

                bwEvents.write(eventLine);
                bwEvents.newLine();

                for (Attendee attendee : event.getAttendees()) {
                    // Reemplazamos comas por espacios para no romper el CSV
                    String cleanName = attendee.getName().replace(",", " ");
                    String cleanEmail = attendee.getEmail().replace(",", " ");

                    String attendeeLine = String.format("%d,%s,%s,%d,%d",
                            event.getId(),
                            cleanName,
                            cleanEmail,
                            attendee.getDni(),
                            attendee.getPhone());

                    bwAttendees.write(attendeeLine);
                    bwAttendees.newLine();
                }
            }

        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "ERROR saving data.\n" + e.getMessage(),
                    "IO Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}