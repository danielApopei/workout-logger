package org.example.ui;

import org.example.dao.WeightLogDao;
import org.example.model.WeightLog;
import org.example.model.User;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class WeightLogPanel extends JPanel {
    private final User currentUser;
    private final WeightLogDao dao = new WeightLogDao();
    private final WeightTableModel tableModel = new WeightTableModel();
    private final JTable table = new JTable(tableModel);
    private final JButton addBtn = new JButton("Add/Update");
    private final JButton deleteBtn = new JButton("Delete");
    private final JComboBox<String> rangeCombo = new JComboBox<>(new String[]{"1 week", "1 month", "6 months", "All time"});
    private final ChartPanel chartPanel;

    private List<WeightLog> allLogs;

    public WeightLogPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        // Split pane: top=table, bottom=chart
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setResizeWeight(0.5);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);
        tablePanel.add(btnPanel, BorderLayout.SOUTH);
        split.setTopComponent(tablePanel);

        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.add(rangeCombo, BorderLayout.NORTH);
        chartPanel = new ChartPanel(null);
        chartContainer.add(chartPanel, BorderLayout.CENTER);
        split.setBottomComponent(chartContainer);

        add(split, BorderLayout.CENTER);

        addBtn.addActionListener(e -> onAddOrUpdate());
        deleteBtn.addActionListener(e -> onDelete());
        rangeCombo.addActionListener(e -> updateChart());

        loadLogs();
    }

    private void loadLogs() {
        new SwingWorker<List<WeightLog>, Void>() {
            protected List<WeightLog> doInBackground() throws Exception {
                return dao.findByUser(currentUser.getId());
            }
            protected void done() {
                try {
                    allLogs = get();
                    tableModel.setLogs(allLogs);
                    updateChart();
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

    private void updateChart() {
        if (allLogs == null) return;
        LocalDate now = LocalDate.now();
        LocalDate cutoff;
        switch ((String) rangeCombo.getSelectedItem()) {
            case "1 week" -> cutoff = now.minusWeeks(1);
            case "1 month" -> cutoff = now.minusMonths(1);
            case "6 months" -> cutoff = now.minusMonths(6);
            default -> cutoff = LocalDate.MIN;
        }
        List<WeightLog> filtered = allLogs.stream()
                .filter(w -> !w.getLogDate().isBefore(cutoff))
                .collect(Collectors.toList());

        TimeSeries series = new TimeSeries("Weight");
        for (WeightLog w : filtered) {
            series.add(new Day(w.getLogDate().getDayOfMonth(),
                            w.getLogDate().getMonthValue(),
                            w.getLogDate().getYear()),
                    w.getWeight());
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Weight Over Time", "Date", "Weight", dataset,
                false, false, false);
        chartPanel.setChart(chart);
    }
}
