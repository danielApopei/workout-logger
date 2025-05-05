package org.example.ui;

import org.example.util.JdbcUtil;
import org.example.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class RecordsPanel extends JPanel {
    private final User currentUser;
    private final JComboBox<String> exerciseCombo;
    private final JLabel maxWeightLabel = new JLabel("Heaviest weight: N/A");
    private final JLabel bestSetLabel   = new JLabel("Best set volume: N/A");
    private final JLabel bestSessionLabel = new JLabel("Best session volume: N/A");

    public RecordsPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout(10,10));

        // dropdown for exercises
        exerciseCombo = new JComboBox<>();
        for (String t : java.util.Arrays.stream(org.example.model.ExerciseType.values()).map(Enum::name).toArray(String[]::new)) {
            exerciseCombo.addItem(t);
        }
        exerciseCombo.insertItemAt("-- Select Exercise --", 0);
        exerciseCombo.setSelectedIndex(0);
        exerciseCombo.addActionListener(e -> updateStats());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Exercise:"));
        top.add(exerciseCombo);
        add(top, BorderLayout.NORTH);

        JPanel stats = new JPanel(new GridLayout(3,1,5,5));
        stats.add(maxWeightLabel);
        stats.add(bestSetLabel);
        stats.add(bestSessionLabel);
        add(stats, BorderLayout.CENTER);
    }

    private void updateStats() {
        String type = (String) exerciseCombo.getSelectedItem();
        if (type == null || type.startsWith("--")) {
            maxWeightLabel.setText("Heaviest weight: N/A");
            bestSetLabel.setText("Best set volume: N/A");
            bestSessionLabel.setText("Best session volume: N/A");
            return;
        }
        new SwingWorker<Void, Void>() {
            Double maxWeight;
            Double bestSet;
            Double bestSession;

            protected Void doInBackground() throws Exception {
                String sqlMaxW =
                        "SELECT MAX(ee.weight) FROM exercise_entry ee " +
                                "JOIN workout_session ws ON ee.session_id=ws.id " +
                                "WHERE ws.user_id=? AND ee.exercise_type=?";
                try (Connection c = JdbcUtil.getConnection();
                     PreparedStatement ps = c.prepareStatement(sqlMaxW)) {
                    ps.setInt(1, currentUser.getId());
                    ps.setString(2, type);
                    ResultSet rs = ps.executeQuery();
                    maxWeight = (rs.next() ? rs.getDouble(1) : 0);
                    if (rs.wasNull()) maxWeight = null;
                }
                String sqlBestSet =
                        "SELECT MAX(ee.weight*ee.reps*ee.sets) FROM exercise_entry ee " +
                                "JOIN workout_session ws ON ee.session_id=ws.id " +
                                "WHERE ws.user_id=? AND ee.exercise_type=?";
                try (Connection c = JdbcUtil.getConnection();
                     PreparedStatement ps = c.prepareStatement(sqlBestSet)) {
                    ps.setInt(1, currentUser.getId());
                    ps.setString(2, type);
                    ResultSet rs = ps.executeQuery();
                    bestSet = (rs.next() ? rs.getDouble(1) : 0);
                    if (rs.wasNull()) bestSet = null;
                }
                String sqlBestSession =
                        "SELECT MAX(sub.vol) FROM (" +
                                " SELECT SUM(ee.weight*ee.reps*ee.sets) AS vol" +
                                " FROM exercise_entry ee" +
                                " JOIN workout_session ws ON ee.session_id=ws.id" +
                                " WHERE ws.user_id=? AND ee.exercise_type=?" +
                                " GROUP BY ee.session_id) sub";
                try (Connection c = JdbcUtil.getConnection();
                     PreparedStatement ps = c.prepareStatement(sqlBestSession)) {
                    ps.setInt(1, currentUser.getId());
                    ps.setString(2, type);
                    ResultSet rs = ps.executeQuery();
                    bestSession = (rs.next() ? rs.getDouble(1) : 0);
                    if (rs.wasNull()) bestSession = null;
                }
                return null;
            }

            protected void done() {
                maxWeightLabel.setText("Heaviest weight: " +
                        (maxWeight != null ? maxWeight + " kg" : "No data"));
                bestSetLabel.setText("Best set volume: " +
                        (bestSet != null ? bestSet + " kg" : "No data"));
                bestSessionLabel.setText("Best session volume: " +
                        (bestSession != null ? bestSession + " kg" : "No data"));
            }
        }.execute();
    }
}
