package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.Dictionary;

public class ActionsPanel extends JPanel implements ActionListener {
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton resetButton;
    private final Dictionary dictionary;

    public ActionsPanel(Dictionary dict) {
        dictionary = dict;

        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        resetButton = new JButton("Reset");

        addButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        resetButton.addActionListener(this);
    }

    private void layoutComponents() {
        setLayout(new FlowLayout());
        add(addButton);
        add(editButton);
        add(deleteButton);
        add(resetButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            // Handle add action
            String slangWord = JOptionPane.showInputDialog(this, "Enter slang word:");
            String definition = JOptionPane.showInputDialog(this, "Enter definition:");
            if (slangWord != null && definition != null) {
                dictionary.addSlangWord(slangWord, definition);
                JOptionPane.showMessageDialog(this, "Slang word added successfully!");
            }
        } else if (e.getSource() == editButton) {
            // Handle edit action
            String slangWord = JOptionPane.showInputDialog(this, "Enter slang word to edit:");
            if (slangWord != null) {
                String newDefinition = JOptionPane.showInputDialog(this, "Enter new definition:");
                if (newDefinition != null) {
                    dictionary.editSlangWord(slangWord, newDefinition);
                    JOptionPane.showMessageDialog(this, "Slang word edited successfully!");
                }
            }
        } else if (e.getSource() == deleteButton) {
            // Handle delete action
            String slangWord = JOptionPane.showInputDialog(this, "Enter slang word to delete:");
            if (slangWord != null) {
                dictionary.deleteSlangWord(slangWord);
                JOptionPane.showMessageDialog(this, "Slang word deleted successfully!");
            }
        } else if (e.getSource() == resetButton) {
            // Handle reset action
            dictionary.resetDataToDefault();

            JOptionPane.showMessageDialog(this, "Slang dictionary reset successfully!");
        }
    }
}