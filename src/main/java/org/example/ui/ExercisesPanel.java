package org.example.ui;

import org.example.dao.ExerciseEntryDao;
import org.example.model.ExerciseEntry;
import org.example.model.ExerciseType;
import org.example.model.User;
import org.example.model.WorkoutSession;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ExercisesPanel extends JPanel {
    private final User currentUser;
    private WorkoutSession selectedSession;
    private final ExerciseEntryDao entryDao = new ExerciseEntryDao();
    private final ExerciseTableModel tableModel = new ExerciseTableModel();
    private final JTable table = new JTable(tableModel);
    private final JButton addBtn = new JButton("Add Exercise");
    private final JButton removeBtn = new JButton("Remove");

    public ExercisesPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(removeBtn);
        add(btnPanel, BorderLayout.SOUTH);

        addBtn.setEnabled(false);
        removeBtn.setEnabled(false);

        table.getSelectionModel().addListSelectionListener(e -> {
            boolean sel = table.getSelectedRow() >= 0;
            removeBtn.setEnabled(sel);
        });

        addBtn.addActionListener(e -> openAddDialog());
        removeBtn.addActionListener(e -> removeSelected());
    }

    public void setSession(WorkoutSession session) {
        this.selectedSession = session;
        loadEntries();
        addBtn.setEnabled(session != null);
    }

    private void loadEntries() {
        if (selectedSession == null) {
            tableModel.setEntries(List.of());
            return;
        }
        new SwingWorker<List<ExerciseEntry>, Void>() {
            protected List<ExerciseEntry> doInBackground() throws Exception {
                return entryDao.findBySession(selectedSession.getId());
            }
            protected void done() {
                try {
                    tableModel.setEntries(get());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ExercisesPanel.this, ex.getMessage(),
                            "Error loading exercises", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void openAddDialog() {
        ExerciseDialog dlg = new ExerciseDialog((Frame) SwingUtilities.getWindowAncestor(this), selectedSession);
        ExerciseEntry e = dlg.showDialog();
        if (e != null) loadEntries();
    }

    private void removeSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        ExerciseEntry e = tableModel.getEntryAt(row);
        new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception {
                entryDao.deleteBySession(e.getSessionId());
                return null;
            }
            protected void done() {
                loadEntries();
            }
        }.execute();
    }
}