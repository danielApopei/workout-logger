package org.example.ui;

import org.example.dao.WorkoutSessionDao;
import org.example.model.WorkoutSession;
import org.example.model.User;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

public class SessionsPanel extends JPanel {
    private final User currentUser;
    private final WorkoutSessionDao sessionDao = new WorkoutSessionDao();
    private final SessionTableModel tableModel = new SessionTableModel();
    private final JTable table = new JTable(tableModel);
    private final JButton addBtn = new JButton("Add");
    private final JButton editBtn = new JButton("Edit");
    private final JButton deleteBtn = new JButton("Delete");

    public SessionsPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        add(btnPanel, BorderLayout.SOUTH);

        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        table.getSelectionModel().addListSelectionListener(e -> {
            boolean sel = table.getSelectedRow() >= 0;
            editBtn.setEnabled(sel);
            deleteBtn.setEnabled(sel);
        });

        addBtn.addActionListener(e -> openAddDialog());
        editBtn.addActionListener(e -> openEditDialog());
        deleteBtn.addActionListener(e -> deleteSelectedSession());

        loadSessions();
    }

    private void loadSessions() {
        new SwingWorker<List<WorkoutSession>, Void>() {
            @Override
            protected List<WorkoutSession> doInBackground() throws Exception {
                return sessionDao.findByUser(currentUser.getId());
            }

            @Override
            protected void done() {
                try {
                    List<WorkoutSession> sessions = get();
                    tableModel.setSessions(sessions);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SessionsPanel.this, ex.getMessage(),
                            "Error loading sessions", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void openAddDialog() {
        SessionDialog dlg = new SessionDialog((JFrame) SwingUtilities.getWindowAncestor(this), currentUser);
        WorkoutSession created = dlg.showDialog();
        if (created != null) loadSessions();
    }

    private void openEditDialog() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        WorkoutSession s = tableModel.getSessionAt(row);
        SessionDialog dlg = new SessionDialog((JFrame) SwingUtilities.getWindowAncestor(this), currentUser, s);
        WorkoutSession updated = dlg.showDialog();
        if (updated != null) loadSessions();
    }

    private void deleteSelectedSession() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        WorkoutSession s = tableModel.getSessionAt(row);
        int ok = JOptionPane.showConfirmDialog(this,
                "Delete session on " + s.getSessionDate() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    sessionDao.delete(s.getId());
                    return null;
                }

                @Override
                protected void done() {
                    loadSessions();
                }
            }.execute();
        }
    }

    public JTable getTable() {
        return table;
    }

    public SessionTableModel getTableModel() {
        return tableModel;
    }
}
