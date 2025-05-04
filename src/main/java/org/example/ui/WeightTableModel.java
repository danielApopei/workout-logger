package org.example.ui;

import org.example.model.WeightLog;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WeightTableModel extends AbstractTableModel {
    private final String[] cols = {"Date", "Weight", "Notes"};
    private List<WeightLog> logs = new ArrayList<>();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void setLogs(List<WeightLog> list) {
        this.logs = list;
        fireTableDataChanged();
    }

    public WeightLog getLogAt(int row) {
        return logs.get(row);
    }

    @Override
    public int getRowCount() {
        return logs.size();
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
        WeightLog w = logs.get(row);
        return switch (col) {
            case 0 -> fmt.format(w.getLogDate());
            case 1 -> w.getWeight();
            case 2 -> w.getNotes();
            default -> null;
        };
    }
}