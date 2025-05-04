package org.example.ui;

import org.example.model.User;
import org.example.model.WeightLog;
import org.example.dao.WeightLogDao;
import org.example.model.WeightLog;
import javax.swing.*;
import java.awt.*;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class WeightLogPanel extends JPanel {
    private final User currentUser;
    private final WeightLogDao dao = new WeightLogDao();
    private final WeightTableModel tableModel = new WeightTableModel();
    private final JTable table = new JTable(tableModel);
    private final JButton addBtn = new JButton("Add/Update");
    private final JButton deleteBtn = new JButton("Delete");

    public WeightLogPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);
        add(btnPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> onAddOrUpdate());
        deleteBtn.addActionListener(e -> onDelete());

        loadLogs();
    }

    private void loadLogs() {
        new SwingWorker<List<WeightLog>, Void>() {
            protected List<WeightLog> doInBackground() throws Exception {
                return dao.findByUser(currentUser.getId());
            }
            protected void done() {
                try {
                    tableModel.setLogs(get());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(WeightLogPanel.this, ex.getMessage(),
                            "Error loading logs", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void onAddOrUpdate() {
        int row = table.getSelectedRow();
        WeightLog existing = row >= 0 ? tableModel.getLogAt(row) : null;
        WeightDialog dlg = new WeightDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                currentUser,
                existing
        );
        WeightLog saved = dlg.showDialog();
        if (saved != null) loadLogs();
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        WeightLog w = tableModel.getLogAt(row);
        int ok = JOptionPane.showConfirmDialog(this, "Delete log for " + w.getLogDate() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            new SwingWorker<Void, Void>() {
                protected Void doInBackground() throws Exception {
                    dao.delete(w.getId());
                    return null;
                }
                protected void done() { loadLogs(); }
            }.execute();
        }
    }
}
