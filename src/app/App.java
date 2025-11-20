package app;

import ui.*;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setLocationRelativeTo(null);
            mainWindow.setVisible(true);
        });
    }
}