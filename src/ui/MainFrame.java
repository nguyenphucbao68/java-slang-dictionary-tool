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