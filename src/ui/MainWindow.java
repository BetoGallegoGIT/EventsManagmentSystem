package ui;

import app.Event;
import app.EventsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;

public class MainWindow extends JFrame {

    private JList<Event> listEventsGUI;
    private DefaultListModel<Event> modelEventsList;
    private JButton btnAddEvent, btnModifyEvent, btnDeleteEvent;
    private JComboBox<String> comboEvents;

    private EventsManager manager;

    public MainWindow() {

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setLayout(new BorderLayout());
        setTitle("Event Management System");
        setSize(600, 400);
        setLocationRelativeTo(null);

        Image icon = Toolkit.getDefaultToolkit().getImage("src/ui/logo.png");
        setIconImage(icon);

        this.manager = EventsManager.getInstance();

        // ----------------------------------------------------------------------------- //

        modelEventsList = new DefaultListModel<>();
        listEventsGUI = new JList<>(modelEventsList);

        listEventsGUI.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPaneEvents = new JScrollPane(listEventsGUI);
        add(scrollPaneEvents, BorderLayout.CENTER);

        JPanel panelNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNorth.add(new JLabel("Show: "));

        comboEvents = new JComboBox<>();
        comboEvents.addItem("All");
        comboEvents.addItem("Future Events");
        comboEvents.addItem("Past Events");

        panelNorth.add(comboEvents);

        add(panelNorth, BorderLayout.NORTH);

        // ----------------------------------------------------------------------------- //

        JPanel panelButtons = new JPanel(new FlowLayout());

        btnAddEvent = new JButton("Add Event");
        btnModifyEvent = new JButton("Modify Event");
        btnDeleteEvent = new JButton("Delete Event");

        panelButtons.add(btnAddEvent);
        panelButtons.add(btnModifyEvent);
        panelButtons.add(btnDeleteEvent);

        add(panelButtons, BorderLayout.SOUTH);

        refreshEventsList();

        // ----------------------------------------------------------------------------- //

        btnAddEvent.addActionListener(e -> onAdd());
        btnModifyEvent.addActionListener(e -> onModify());
        btnDeleteEvent.addActionListener(e -> onDelete());
        comboEvents.addActionListener(e -> refreshEventsList());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
    }

    // ----------------------------------------------------------------------------- //

    private void onModify() {
        Event eventSelected = listEventsGUI.getSelectedValue();

        if (eventSelected == null) {
            JOptionPane.showMessageDialog(this, "Please select an event to modify.", "No event selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EventDialog dialog = new EventDialog(this, eventSelected);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            refreshEventsList();
        } else {
            refreshEventsList();
        }
    }

    private void onAdd() {
        EventDialog dialog = new EventDialog(this, null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            refreshEventsList();
        }
    }

    private void onDelete() {
        Event eventSelected = listEventsGUI.getSelectedValue();

        if (eventSelected == null) {
            JOptionPane.showMessageDialog(this, "Please select an event to delete.", "No event selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int response = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the selected event?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            manager.removeEvent(eventSelected);
            manager.saveData();
            modelEventsList.removeElement(eventSelected);
        }

    }

    // ----------------------------------------------------------------------------- //

    private void refreshEventsList() {
        modelEventsList.clear();
        LocalDate today = LocalDate.now();
        String filter = (String) comboEvents.getSelectedItem();

        java.util.List<Event> sortedEvents = new java.util.ArrayList<>(manager.getEvents());
        sortedEvents.sort((e1, e2) -> e1.getDate().compareTo(e2.getDate()));

        for (Event event : sortedEvents) {
            boolean show = false;

            if ("All".equals(filter)) {
                show = true;
            } else if ("Future Events".equals(filter)){
                if (!event.getDate().isBefore(today))
                    show = true;
            } else if ("Past Events".equals(filter)) {
                if (event.getDate().isBefore(today))
                    show = true;
            }

            if (show) {
                modelEventsList.addElement(event);
            }
        }


    }

    private void confirmExit() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            manager.saveData();

        } else if (confirm == JOptionPane.CANCEL_OPTION || confirm == JOptionPane.NO_OPTION) {
            return;
        }

        dispose();
        System.exit(0);
    }

}
