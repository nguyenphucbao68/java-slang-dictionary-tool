//package ui;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class MainFrame extends JFrame {
//    public MainFrame() {
//        setTitle("Slang Dictionary");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new BorderLayout());
//
//        JPanel leftPanel = new JPanel();
//        leftPanel.setPreferredSize(new Dimension(500, 500));
//
//        JTextField searchField = new JTextField(20);
//        JButton searchButton = new JButton("Search");
//        JComboBox<String> searchOptions = new JComboBox<>(new String[]{ "Search by slang word", "Search by definition" });
//        JList<String> searchList = new JList<>();
//        JScrollPane listScroller = new JScrollPane(searchList);
//        listScroller.setPreferredSize(new Dimension(500, 350));
//
//        JPanel btnList = new JPanel();
//        btnList.setLayout(new BoxLayout(btnList, BoxLayout.LINE_AXIS));
//        JButton createButton = new JButton("Create");
//        JButton editButton = new JButton("Edit");
//        JButton deleteButton = new JButton("Delete");
//        JButton resetButton = new JButton("Reset");
//
//        btnList.add(createButton);
//        btnList.add(Box.createRigidArea(new Dimension(50,15)));
//        btnList.add(editButton);
//        btnList.add(Box.createRigidArea(new Dimension(50,15)));
//        btnList.add(deleteButton);
//        btnList.add(Box.createRigidArea(new Dimension(50,15)));
//        btnList.add(resetButton);
//
//        leftPanel.add(searchField);
//        leftPanel.add(searchOptions);
//        leftPanel.add(searchButton);
//        leftPanel.add(listScroller);
//        leftPanel.add(btnList);
//
//        JPanel rightPanel = new JPanel();
//        rightPanel.setPreferredSize(new Dimension(300, 500));
//        JLabel todayWordLabel = new JLabel("Today's Slang Word:");
//        JLabel todayWordDefinitionLabel = new JLabel();
//
//        JPanel historyPanel = new JPanel();
//        historyPanel.setPreferredSize(new Dimension(250, 250));
//        historyPanel.setBackground(Color.WHITE);
//        JLabel historyLabel = new JLabel("History");
//        historyPanel.add(historyLabel);
//
//        JButton quizOpenWithSlangWord = new JButton("Quiz Word");
//        JButton quizOpenWithDefinition = new JButton("Quiz Definition");
//
//        JPanel btnList2 = new JPanel();
//        btnList2.setLayout(new BoxLayout(btnList2, BoxLayout.LINE_AXIS));
//        btnList2.add(quizOpenWithSlangWord);
//        btnList2.add(Box.createRigidArea(new Dimension(50,15)));
//        btnList2.add(quizOpenWithDefinition);
//
//        rightPanel.add(todayWordLabel);
//        rightPanel.add(todayWordDefinitionLabel);
//        rightPanel.add(historyPanel);
//        rightPanel.add(btnList2);
//
//        add(leftPanel, BorderLayout.WEST);
//        add(rightPanel, BorderLayout.EAST);
//
//        pack();
//        setVisible(true);
//    }
//}
package ui;


import core.Dictionary;
import core.HistoryManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Slang Dictionary");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

//        DefaultListModel<String> listModel = new DefaultListModel<>();
//        IndexManager indexManager = new IndexManager();
//        SlangWordManager slangWordManager = new SlangWordManager(listModel);
        HistoryManager historyManager = new HistoryManager();
        Dictionary dict = new Dictionary(historyManager);

        SearchPanel searchPanel = new SearchPanel(dict);
        ActionsPanel actionsPanel = new ActionsPanel(dict);
        HistoryPanel historyPanel = new HistoryPanel(historyManager);
        QuizPanel quizPanel = new QuizPanel(dict);

        add(searchPanel, BorderLayout.NORTH);
        add(actionsPanel, BorderLayout.CENTER);
        add(historyPanel, BorderLayout.WEST);
        add(quizPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}