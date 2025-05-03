package org.example.ui;

import org.example.model.WorkoutSession;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SessionTableModel extends AbstractTableModel {
    private final String[] cols = {"Date", "Notes"};
    private List<WorkoutSession> sessions = new ArrayList<>();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void setSessions(List<WorkoutSession> list) {
        this.sessions = list;
        fireTableDataChanged();
    }

    public WorkoutSession getSessionAt(int row) {
        return sessions.get(row);
    }

    @Override
    public int getRowCount() {
        return sessions.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public String getColumnName(int col) {
        return cols[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        WorkoutSession s = sessions.get(row);
        return switch (col) {
            case 0 -> fmt.format(s.getSessionDate());
            case 1 -> s.getNotes();
            default -> null;
        };
    }
}
