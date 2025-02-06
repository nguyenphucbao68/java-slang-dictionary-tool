//package ui;
//
//import javax.swing.*;
//import java.awt.*;
//import core.HistoryManager;
//import java.util.List;
//
//public class HistoryPanel extends JPanel {
//    private JList<String> historyList;
//    private DefaultListModel<String> historyModel;
//
//    public HistoryPanel() {
//        setLayout(new BorderLayout());
//
//        historyModel = new DefaultListModel<>();
//        historyList = new JList<>(historyModel);
//        JScrollPane scrollPane = new JScrollPane(historyList);
//
//        loadHistory();
//
//        add(new JLabel("Search History"), BorderLayout.NORTH);
//        add(scrollPane, BorderLayout.CENTER);
//    }
//
//    private void loadHistory() {
//        List<String> history = HistoryManager.getHistory();
//        historyModel.clear();
//        for (String entry : history) {
//            historyModel.addElement(entry);
//        }
//    }
//}

package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import core.HistoryManager;

import java.util.List;

public class HistoryPanel extends JPanel {
    private DefaultTableModel tableModel;
    private HistoryManager historyManager;

    public HistoryPanel(HistoryManager history) {
        historyManager = history;
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        String[] columnNames = {"Slang word", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable tableHistory = new JTable(tableModel);
        tableHistory.setPreferredScrollableViewportSize(new Dimension(200, 200));
        tableHistory.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(tableHistory);
        add(scrollPane, BorderLayout.CENTER);

        loadHistory();
    }

    public void addHistory(String slangWord, String date) {
        tableModel.addRow(new Object[]{slangWord, date});
    }

    private void loadHistory() {
        List<String> history = historyManager.getHistory();
        for (String entry : history) {
            String[] parts = entry.split(",");
            if (parts.length == 2) {
                addHistory(parts[0], parts[1]);
            }
        }

    }
}