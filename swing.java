import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class swing extends JPanel implements ActionListener {
  static JFrame frame;
  static JTextField searchField;
  static  JButton searchButton;
  static JButton createButton;
  static JButton editButton;
  static JButton deleteButton;
  static JButton resetButton;
  private swing(){
    setLayout(new BorderLayout());
    JPanel leftpanel = new JPanel();
    leftpanel.setPreferredSize(new Dimension(500, 500));
    
    searchField = new JTextField(20);
    searchField.addActionListener(this);

    String[] searchOptions = { "Search by slang word", "Search by definition" };
    JComboBox<String> searchList = new JComboBox<String>(searchOptions);
    searchList.setSelectedIndex(0);
    searchList.addActionListener(this);

    searchButton = new JButton("Search");
    searchButton.addActionListener(this);
    
    String[] names = {"Arlo", "Cosmo", "Elmo", "Hugo"};
    JList<String> list = new JList<String>(names); //data has type Object[]
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setLayoutOrientation(JList.VERTICAL);
    list.setVisibleRowCount(-1);
    JScrollPane listScroller = new JScrollPane(list);
    listScroller.setPreferredSize(new Dimension(500, 350));

    JPanel btnList = new JPanel();
    btnList.setLayout(new BoxLayout(btnList, BoxLayout.LINE_AXIS));
    createButton = new JButton("Create");
    createButton.addActionListener(this);
    editButton = new JButton("Edit");
    editButton.addActionListener(this);
    deleteButton = new JButton("Delete");
    deleteButton.addActionListener(this);
    resetButton = new JButton("Reset");
    resetButton.addActionListener(this);
    btnList.add(createButton);
    btnList.add(Box.createRigidArea(new Dimension(50,15)));
    btnList.add(editButton);
    btnList.add(Box.createRigidArea(new Dimension(50,15)));
    btnList.add(deleteButton);
    btnList.add(Box.createRigidArea(new Dimension(50,15)));
    btnList.add(resetButton);

    leftpanel.add(searchField);
    leftpanel.add(searchList);
    leftpanel.add(searchButton);
    leftpanel.add(listScroller);
    leftpanel.add(btnList);

    JPanel rightpanel = new JPanel();
    rightpanel.setPreferredSize(new Dimension(300, 500));

    JPanel todayWord = new JPanel();
    todayWord.setPreferredSize(new Dimension(250, 100));
    todayWord.setBackground(Color.WHITE);
    JLabel todayWordLabel = new JLabel("Today's word");
    
    JPanel definition = new JPanel();
    definition.setPreferredSize(new Dimension(250, 100));
    definition.setBackground(Color.WHITE);
    JLabel addDefinitionLabel = new JLabel("Add definition");
    definition.add(addDefinitionLabel);

    todayWord.add(todayWordLabel);
    todayWord.add(definition);

    JPanel history = new JPanel();
    history.setPreferredSize(new Dimension(250, 200));
    history.setBackground(Color.WHITE);
    JLabel historyLabel = new JLabel("History");
    history.add(historyLabel);

    String[] columnNames = {"Slang word", "Definition"};
    String[][] data = {{"Arlo", "A dog"}, {"Cosmo", "A cat"}, {"Elmo", "A fish"}, {"Hugo", "A bird"}};
    JTable table = new JTable(data, columnNames);
    table.setPreferredScrollableViewportSize(new Dimension(250, 200));
    table.setFillsViewportHeight(true);
    JScrollPane scrollPane = new JScrollPane(table);
    history.add(scrollPane);

    JPanel btnList2 = new JPanel();
    btnList2.setLayout(new BoxLayout(btnList2, BoxLayout.LINE_AXIS));
    JButton btn1 = new JButton("Quiz 1");
    btn1.addActionListener(this);
    JButton btn2 = new JButton("Quiz 2");
    btn2.addActionListener(this);
    btnList2.add(btn1);
    btnList2.add(Box.createRigidArea(new Dimension(50,15)));
    btnList2.add(btn2);

    rightpanel.add(todayWord);
    rightpanel.add(history);
    rightpanel.add(btnList2);

    add(leftpanel, BorderLayout.WEST);
    add(rightpanel, BorderLayout.EAST);
  }

  public void actionPerformed(ActionEvent e) {
    // JComboBox<String> cb = (JComboBox)e.getSource();
    // String petName = (String)cb.getSelectedItem();
    // updateLabel(petName);
  }

  private static void createAndShowGUI() 
  {
    //Make sure we have nice window decorations.
    JFrame.setDefaultLookAndFeelDecorated(true);
    JDialog.setDefaultLookAndFeelDecorated(true);

    //Create and set up the window.
    frame = new JFrame("Name That Baby");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Create and set up the content pane.
    JComponent newContentPane = new swing();
    newContentPane.setOpaque(true); //content panes must be opaque
    frame.setContentPane(newContentPane);

    //Display the window.
    frame.pack(); // fit to all components that you have
    frame.setVisible(true);
  }
  public static void main(String[] args) {
      
      javax.swing.SwingUtilities.invokeLater(new Runnable() 
      {
          public void run() 
          {
              createAndShowGUI();
          }
      });
  }
}
