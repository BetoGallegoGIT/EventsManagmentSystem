package ui;

import app.Attendee;
import app.Event;
import app.EventsManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EventDialog extends JDialog {

    private JTextField txtDescription, txtDate, txtLocation;
    private JList<Attendee> listAttendeesGUI;
    private DefaultListModel<Attendee> listModelAttendees;

    private EventsManager manager;
    private Event event;
    private boolean isConfirmed;

    public EventDialog(Frame owner, Event event) {

        super(owner, true);
        this.event = event;
        this.manager = EventsManager.getInstance();
        this.isConfirmed = false;

        setTitle(event == null ? "Create new event" : "Modify event");
        setSize(450, 500);
        setLocationRelativeTo(owner);

        setLayout(new BorderLayout());

        // ----------------------------------------------------------------------------- //

        JPanel panelForm = new JPanel(new GridLayout(3, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //1
        panelForm.add(new JLabel("Date (YYYY-MM-DD):"));
        txtDate = new JTextField();
        panelForm.add(txtDate);

        //2
        panelForm.add(new JLabel("Location:"));
        txtLocation = new JTextField();
        panelForm.add(txtLocation);

        //3
        panelForm.add(new JLabel("Description:"));
        txtDescription = new JTextField();
        panelForm.add(txtDescription);

        add(panelForm, BorderLayout.NORTH);

        // ----------------------------------------------------------------------------- //

        JPanel panelAttendees = new JPanel(new BorderLayout());
        panelAttendees.setBorder(BorderFactory.createTitledBorder("Registered attendees"));

        listModelAttendees = new DefaultListModel<>();
        listAttendeesGUI = new JList<>(listModelAttendees);

        panelAttendees.add(new JScrollPane(listAttendeesGUI), BorderLayout.CENTER);

        JButton btnInscribe = new JButton("Inscribe Attendee");
        panelAttendees.add(btnInscribe, BorderLayout.SOUTH);

        add(panelAttendees, BorderLayout.CENTER);

        // ----------------------------------------------------------------------------- //

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnConfirm = new JButton("Confirm");
        JButton btnCancel = new JButton("Cancel");

        panelButtons.add(btnConfirm);
        panelButtons.add(btnCancel);

        add(panelButtons, BorderLayout.SOUTH);

        // ----------------------------------------------------------------------------- //

        if (event != null) {
            panelAttendees.setVisible(true);

            txtDescription.setText(event.getDescription());
            txtDate.setText(event.getDate().toString());
            txtLocation.setText(event.getLocation());

            for (Attendee attendee : event.getAttendees()) {
                listModelAttendees.addElement(attendee);
            }
        } else {
            panelAttendees.setVisible(false);
            setSize(450, 250);
        }

        // ----------------------------------------------------------------------------- //

        btnCancel.addActionListener(e -> {
            this.isConfirmed = false;
            dispose();
        });

        btnConfirm.addActionListener(e -> onConfirm());
        btnInscribe.addActionListener(e -> onInscribe());

    }

    private void onConfirm() {

        try {

            String description = txtDescription.getText().trim();
            LocalDate date = LocalDate.parse(txtDate.getText());
            String location = txtLocation.getText().trim();

            if (description.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (event == null) {
                manager.addEvent(description, date, location);
            } else {
                manager.modifyEvent(event, description, date, location);
            }

            this.isConfirmed = true;
            manager.saveData();
            dispose();

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onInscribe() {

        String name = JOptionPane.showInputDialog(this, "Name:");
        if (name == null || name.trim().isEmpty()) return;

        String email = JOptionPane.showInputDialog(this, name + "'s Email:");
        if (email == null || email.trim().isEmpty()) return;

        Long phone = askValidLong(name + "'s Phone:");
        if (phone == null) return;

        Long dni = askValidLong(name + "'s DNI:");
        if (dni == null) return;

        Attendee newAttendee = new Attendee(name, email, dni, phone);
        manager.registerAttendee(event, newAttendee);
        manager.saveData();
        listModelAttendees.addElement(newAttendee);

    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    private Long askValidLong(String message) {
        while (true) {
            String input = JOptionPane.showInputDialog(this, message);

            if (input == null) {
                return null;
            }

            if (input.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Field cannot be empty. Please enter a valid number.",
                        "Error", JOptionPane.WARNING_MESSAGE);
                continue;
            }

            try {
                return Long.parseLong(input.trim());

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Invalid number format. Please enter a valid numeric value.",
                        "Format Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
