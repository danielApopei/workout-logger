package org.example.ui;

import org.example.model.ExerciseEntry;
import org.example.model.ExerciseType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ExerciseTableModel extends AbstractTableModel {
    private final String[] cols = {"Type", "Sets", "Reps", "Weight", "Duration (s)"};
    private List<ExerciseEntry> entries = new ArrayList<>();

    public void setEntries(List<ExerciseEntry> list) {
        this.entries = list;
        fireTableDataChanged();
    }

    public ExerciseEntry getEntryAt(int row) {
        return entries.get(row);
    }

    @Override
    public int getRowCount() {
        return entries.size();
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
        ExerciseEntry e = entries.get(row);
        return switch (col) {
            case 0 -> e.getType().name();
            case 1 -> e.getSets();
            case 2 -> e.getReps();
            case 3 -> e.getWeight();
            case 4 -> e.getDurationSeconds();
            default -> null;
        };
    }
}
