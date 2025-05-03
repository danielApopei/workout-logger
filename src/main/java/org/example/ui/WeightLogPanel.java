package org.example.ui;

import org.example.model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class WeightLogPanel extends JPanel {
    private final User currentUser;

    public WeightLogPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        JLabel placeholder = new JLabel("Weight Log - Coming Soon!", SwingConstants.CENTER);
        add(placeholder, BorderLayout.CENTER);
    }
}
