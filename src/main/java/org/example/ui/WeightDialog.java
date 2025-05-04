package org.example.ui;

import org.example.dao.WeightLogDao;
import org.example.model.WeightLog;
import org.example.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class WeightDialog extends JDialog {
    private final User currentUser;
    private WeightLog existing;
    private WeightLog result;

    private JSpinner dateSpinner;
    private JTextField weightField;
    private JTextArea notesArea;

    public WeightDialog(Frame owner, User user, WeightLog toEdit) {
        super(owner, toEdit == null ? "Add Weight Log" : "Edit Weight Log", true);
        this.currentUser = user;
        this.existing = toEdit;

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5,5,5,5);

        gc.gridx = 0; gc.gridy = 0; add(new JLabel("Date:"), gc);
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        gc.gridx = 1; add(dateSpinner, gc);

        gc.gridx = 0; gc.gridy = 1; add(new JLabel("Weight:"), gc);
        weightField = new JTextField(8);
        gc.gridx = 1; add(weightField, gc);

        gc.gridx = 0; gc.gridy = 2; gc.anchor = GridBagConstraints.NORTH;
        add(new JLabel("Notes:"), gc);
        notesArea = new JTextArea(4, 20);
        gc.gridx = 1; add(new JScrollPane(notesArea), gc);

        JButton saveBtn = new JButton("Save");
        gc.gridx = 0; gc.gridy = 3; gc.gridwidth = 2;
        add(saveBtn, gc);
        saveBtn.addActionListener(this::onSave);

        pack();
        setLocationRelativeTo(owner);

        if (existing != null) {
            dateSpinner.setValue(Date.from(existing.getLogDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            weightField.setText(String.valueOf(existing.getWeight()));
            notesArea.setText(existing.getNotes());
        }
    }

    private void onSave(ActionEvent ev) {
        try {
            Date d = (Date) dateSpinner.getValue();
            LocalDate ld = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            double w = Double.parseDouble(weightField.getText());
            String notes = notesArea.getText();

            WeightLogDao dao = new WeightLogDao();
            if (existing == null) {
                result = new WeightLog(currentUser.getId(), ld, w, notes);
                result = dao.insert(result);
            } else {
                existing.setLogDate(ld);
                existing.setWeight(w);
                existing.setNotes(notes);
                dao.update(existing);
                result = existing;
            }
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public WeightLog showDialog() {
        setVisible(true);
        return result;
    }
}
