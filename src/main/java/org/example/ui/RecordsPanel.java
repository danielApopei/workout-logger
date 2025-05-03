package org.example.ui;

import org.example.model.User;

import javax.swing.*;
import java.awt.*;

public class RecordsPanel extends JPanel {
    private final User currentUser;

    public RecordsPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        JLabel placeholder = new JLabel("Records - Coming Soon!", SwingConstants.CENTER);
        placeholder.setFont(placeholder.getFont().deriveFont(Font.ITALIC, 16f));
        add(placeholder, BorderLayout.CENTER);
    }
}
