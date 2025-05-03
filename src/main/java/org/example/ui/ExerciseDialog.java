package org.example.ui;

import org.example.dao.ExerciseEntryDao;
import org.example.model.ExerciseEntry;
import org.example.model.ExerciseType;
import org.example.model.WorkoutSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ExerciseDialog extends JDialog {
    private final WorkoutSession session;
    private ExerciseEntry result;

    private JComboBox<ExerciseType> typeCombo = new JComboBox<>(ExerciseType.values());
    private JSpinner setsSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 100, 1));
    private JSpinner repsSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1));
    private JTextField weightField = new JTextField(5);
    private JTextField durationField = new JTextField(5);

    public ExerciseDialog(Frame owner, WorkoutSession session) {
        super(owner, "Add Exercise", true);
        this.session = session;

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5,5,5,5);
        gc.gridx = 0; gc.gridy = 0; add(new JLabel("Type:"), gc);
        gc.gridx = 1; add(typeCombo, gc);

        gc.gridx = 0; gc.gridy = 1; add(new JLabel("Sets:"), gc);
        gc.gridx = 1; add(setsSpinner, gc);

        gc.gridx = 0; gc.gridy = 2; add(new JLabel("Reps:"), gc);
        gc.gridx = 1; add(repsSpinner, gc);

        gc.gridx = 0; gc.gridy = 3; add(new JLabel("Weight:"), gc);
        gc.gridx = 1; add(weightField, gc);

        gc.gridx = 0; gc.gridy = 4; add(new JLabel("Duration (s):"), gc);
        gc.gridx = 1; add(durationField, gc);

        JButton save = new JButton("Save");
        gc.gridx = 0; gc.gridy = 5; gc.gridwidth = 2;
        add(save, gc);

        save.addActionListener(this::onSave);
        pack();
        setLocationRelativeTo(owner);
    }

    private void onSave(ActionEvent e) {
        try {
            ExerciseEntryDao dao = new ExerciseEntryDao();
            ExerciseType type = (ExerciseType) typeCombo.getSelectedItem();
            int sets = (Integer) setsSpinner.getValue();
            int reps = (Integer) repsSpinner.getValue();
            double weight = Double.parseDouble(weightField.getText());
            Integer dur = null;
            if (!durationField.getText().isBlank()) dur = Integer.parseInt(durationField.getText());

            result = new ExerciseEntry(
                    session.getId(), type, weight, reps, sets, dur
            );
            dao.insert(result);
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ExerciseEntry showDialog() {
        setVisible(true);
        return result;
    }
}
