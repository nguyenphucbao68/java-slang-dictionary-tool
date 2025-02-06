package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.Dictionary;
import core.QuizManager;

public class QuizPanel extends JPanel implements ActionListener {
    private JButton quizOpenWithSlangWord;
    private JButton quizOpenWithDefinition;
    private final QuizManager quizManager;

    public QuizPanel(Dictionary dict) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        initializeComponents();
        layoutComponents();
        quizManager = new QuizManager(dict.getSlangHashMap());
    }

    private void initializeComponents() {
        quizOpenWithSlangWord = new JButton("Quiz Word");
        quizOpenWithDefinition = new JButton("Quiz Definition");

        quizOpenWithSlangWord.addActionListener(this);
        quizOpenWithDefinition.addActionListener(this);
    }

    private void layoutComponents() {
        add(quizOpenWithSlangWord);
        add(Box.createRigidArea(new Dimension(50, 15)));
        add(quizOpenWithDefinition);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quizOpenWithSlangWord) {
            quizManager.quizWithSlangWord();
        } else if (e.getSource() == quizOpenWithDefinition) {
            quizManager.quizWithDefinition();
        }
    }
}