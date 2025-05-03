package org.example.ui;

import org.example.dao.WorkoutSessionDao;
import org.example.model.WorkoutSession;
import org.example.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;

public class SessionDialog extends JDialog {
    private WorkoutSession session;
    private WorkoutSession result;
    private final User currentUser;

    private JTextField notesField = new JTextField(20);
    private JSpinner dateSpinner;

    public SessionDialog(JFrame owner, User user) {
        this(owner, user, null);
    }

    public SessionDialog(JFrame owner, User user, WorkoutSession session) {
        super(owner, session == null ? "Add Session" : "Edit Session", true);
        this.currentUser = user;
        this.session = session;

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5,5,5,5);
        gc.gridx = 0; gc.gridy = 0;
        add(new JLabel("Date/Time:"), gc);

        SpinnerDateModel sdm = new SpinnerDateModel();
        dateSpinner = new JSpinner(sdm);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd HH:mm"));
        gc.gridx = 1;
        add(dateSpinner, gc);

        gc.gridx = 0; gc.gridy = 1;
        add(new JLabel("Notes:"), gc);
        gc.gridx = 1;
        add(notesField, gc);

        JButton saveBtn = new JButton("Save");
        gc.gridx = 0; gc.gridy = 2; gc.gridwidth = 2;
        add(saveBtn, gc);

        saveBtn.addActionListener(this::onSave);

        pack();
        setLocationRelativeTo(owner);

        if (session != null) {
            dateSpinner.setValue(java.sql.Timestamp.valueOf(session.getSessionDate()));
            notesField.setText(session.getNotes());
        }
    }

    private void onSave(ActionEvent e) {
        try {
            LocalDateTime dt = ((java.util.Date) dateSpinner.getValue()).toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
            String notes = notesField.getText();
            WorkoutSessionDao dao = new WorkoutSessionDao();
            if (session == null) {
                WorkoutSession s = new WorkoutSession(currentUser.getId(), dt, notes);
                result = dao.insert(s);
            } else {
                session.setSessionDate(dt);
                session.setNotes(notes);
                dao.update(session);
                result = session;
            }
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public WorkoutSession showDialog() {
        setVisible(true);
        return result;
    }
}
