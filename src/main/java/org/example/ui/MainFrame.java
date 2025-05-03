package org.example.ui;

import org.example.model.User;
import org.example.model.WorkoutSession;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class MainFrame extends JFrame {
    private final User currentUser;
    private final SessionsPanel sessionsPanel;
    private final ExercisesPanel exercisesPanel;
    private final WeightLogPanel weightLogPanel;
    private final RecordsPanel recordsPanel;

    public MainFrame(User user) {
        super("Workout Logger — " + user.getUsername());
        this.currentUser = user;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        sessionsPanel = new SessionsPanel(currentUser);
        exercisesPanel = new ExercisesPanel(currentUser);
        weightLogPanel = new WeightLogPanel(currentUser);
        recordsPanel = new RecordsPanel(currentUser);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Sessions", sessionsPanel);
        tabs.addTab("Exercises", exercisesPanel);
        tabs.addTab("Weight Log", weightLogPanel);
        tabs.addTab("Records & Calendar", recordsPanel);
        add(tabs, BorderLayout.CENTER);

        setSize(800, 600);
        setLocationRelativeTo(null);

        sessionsPanel.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = sessionsPanel.getTable().getSelectedRow();
                if (row >= 0) {
                    WorkoutSession s = sessionsPanel.getTableModel().getSessionAt(row);
                    exercisesPanel.setSession(s);
                } else {
                    exercisesPanel.setSession(null);
                }
            }
        });
    }

    public SessionsPanel getSessionsPanel() {
        return sessionsPanel;
    }

    public ExercisesPanel getExercisesPanel() {
        return exercisesPanel;
    }
}
