package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import core.Dictionary;

public class SearchPanel extends JPanel implements ActionListener {
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox<String> searchOptions;
    private JList<String> searchList;
    private final DefaultListModel<String> listSearchResultModel;
    private final Dictionary dictionary;

    public SearchPanel(Dictionary dict) {
        setLayout(new BorderLayout());
        dictionary = dict;
        listSearchResultModel = dict.getListModel();

        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchOptions = new JComboBox<>(new String[]{"Search by slang word", "Search by definition"});
        searchList = new JList<>(listSearchResultModel);
        searchList.setModel(listSearchResultModel);

        searchButton.addActionListener(this);
    }

    private void layoutComponents() {
        JPanel searchPanel = new JPanel();
        searchPanel.add(searchOptions);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(searchList), BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String selectedOption = (String) searchOptions.getSelectedItem();
            if ("Search by slang word".equals(selectedOption)) {
                dictionary.searchBySlangWord(searchField.getText(), listSearchResultModel);
            } else if ("Search by definition".equals(selectedOption)) {
                dictionary.searchByDefinition(searchField.getText(), listSearchResultModel);
            }
        }
    }


}