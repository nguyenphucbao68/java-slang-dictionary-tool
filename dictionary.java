import java.util.*;
import java.io.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class dictionary  extends JPanel implements ActionListener {
  static JFrame frame;
  static JTextField searchField;
  static JButton searchButton;
  static JButton createButton;
  static JButton editButton;
  static JButton deleteButton;
  static JButton resetButton;
  static JList<String> searchList;
  static JComboBox<String> searchOptions;
  static JLabel todayWordLabel;
  static JLabel todayWordDefinitionLabel;
  static JTable tableHistory;
  static JButton quizOpenWithSlangWord;
  static JButton quizOpenWithDefinition;
  static DefaultListModel<String> listSearchResultModel;
  static HashMap<String, String> slangHashMap = new HashMap<String, String>();
  static HashMap<String, HashSet<String>> definitionHashMap = new HashMap<String, HashSet<String>>();
  static HashMap<String, HashSet<String>> slangWordIndex = new HashMap<String, HashSet<String>>();
  static final String HISTORY_FILE_NAME = "history.txt";
  static final String DATASET_FILE_NAME = "slang.txt";
  static final String DATASET_CLONE_FILE_NAME = "slang_clone.txt";
  static final String INDEX_FILE_NAME = "definition_index.txt";
  static final String SLANG_WORD_INDEX_FILE_NAME = "slang_index.txt";
  static final int NUM_OF_ANSWERS_QUIZ = 4;
  static Random rand = new Random();

  private static void saveHistorySearch(String slangWord){
    try {
      FileWriter fr = new FileWriter(HISTORY_FILE_NAME, true);
      fr.write(slangWord + "|" + new java.util.Date() + "\n");
      
      fr.close();
    } catch (Exception e) {
      System.out.println(e);
      return;
    }
  }

  private static void searchBySlangWord(){
    String slangWord = searchField.getText();
    if (slangWord.length() == 0){
      JOptionPane.showMessageDialog(frame, "Please enter a slang word to search");
      return;
    }

    try {
      if(slangWordIndex.containsKey(slangWord)){
        HashSet<String> slangWords = slangWordIndex.get(slangWord);
        listSearchResultModel.clear();
  
        // concentrate all slang words with its definition
        for(String slangWordWithDefinition : slangWords){
          if(slangWordWithDefinition == "" || slangWordWithDefinition == " ") continue;
          String definition = slangHashMap.get(slangWordWithDefinition);
          if(definition == null) continue;
          listSearchResultModel.addElement(slangWordWithDefinition + " - " + definition);
        }
        searchList.setModel(listSearchResultModel);
      } else {
        JOptionPane.showMessageDialog(frame, "Not found");
      }      
      DefaultTableModel model = (DefaultTableModel) tableHistory.getModel();
      model.insertRow(0, new Object[]{slangWord, new java.util.Date()});
      saveHistorySearch(slangWord);
    } catch (Exception e) {
      System.out.println("error: " + e);
      return;
    }
    
  }

  private static void searchByDefinition(){
    String keywords = searchField.getText();
    if (keywords.length() == 0){
      JOptionPane.showMessageDialog(frame, "Please enter a definition to search");
      return;
    }
    String[] keywordsSplit = keywords.split(" ");
    
    Set<String> slangWordResult = new HashSet<String>();
    boolean first = true;
    for (String keyword : keywordsSplit) {
      if(slangWordResult.size() == 0 && first && definitionHashMap.containsKey(keyword)){
        slangWordResult.addAll(definitionHashMap.get(keyword));
        first = false;
      }else if(definitionHashMap.containsKey(keyword)){
        slangWordResult.retainAll(definitionHashMap.get(keyword));
      }
    }

    listSearchResultModel.clear();

    // concentrate all slang words with its definition
    for(String slangWordWithDefinition : slangWordResult){
      String definition = slangHashMap.get(slangWordWithDefinition);
      if(definition == null) continue;
      listSearchResultModel.addElement(slangWordWithDefinition + " - " + definition);
    }
    searchList.setModel(listSearchResultModel);
    
  }

  private static void loadSlangWords(){
    try {
      FileReader fr = new FileReader(DATASET_FILE_NAME);
      BufferedReader br = new BufferedReader(fr);

      String line = br.readLine();
      while((line = br.readLine()) != null){
        if(!line.contains("`")){
          continue;
        }
        String[] split = line.split("`");

        slangHashMap.put(split[0], split[1]);
        for(int i = 0; i < split[0].length(); i++){
          String subString = split[0].substring(0, i + 1);
          if(!slangWordIndex.containsKey(subString)){
            slangWordIndex.put(subString, new HashSet<String>());
          }
          slangWordIndex.get(subString).add(split[0]);
        }

        split[1] = split[1].replace("|", " ");
        split[1] = split[1].replace("   ", " ");
        split[1] = split[1].replace("  ", " ");
        String[] definitionKeywords = split[1].split(" ");

        for(String keyword : definitionKeywords){
          if(definitionHashMap.containsKey(keyword)){
            definitionHashMap.get(keyword).add(split[0]);
          } else {
            HashSet<String> temp = new HashSet<String>();
            temp.add(split[0]);
            definitionHashMap.put(keyword, temp);
          }
        }
      }
      fr.close();
    } catch (Exception e) {
      System.out.println(e);
      return;
    }
  }

  private static void randomSlangWordToday(){
    HashMap<String, String> slangWord = randomHashMapSlangWords(1);
    todayWordLabel.setText("Today's slang word: " + slangWord.keySet().toArray()[0]);

    String definition = slangWord.get(slangWord.keySet().toArray()[0]);

    // split definition by "|" and show all of them
    String[] definitions = definition.split("|");
    String definitionToShow = "";
    for(String def : definitions){
      definitionToShow += def + "\n";
    }
    todayWordDefinitionLabel.setText(definitionToShow);
  }

  private static void loadHistory(){
    try {
      File file = new File(HISTORY_FILE_NAME);
      if(!file.exists()){
        return;
      }

      FileReader fr = new FileReader(HISTORY_FILE_NAME);
      BufferedReader br = new BufferedReader(fr);
      
      ArrayList<String> history = new ArrayList<String>();

      String line;
      while((line = br.readLine()) != null){
        history.add(line);
      }

      String[] columnNames = {"Slang word", "Date"};
      String[][] data = new String[history.size()][2];
      for(int i = 0; i < history.size(); i++){
        String[] split = history.get(i).split(Pattern.quote("|"));
        data[i][0] = split[0];
        data[i][1] = split[1];
      }

      Arrays.sort(data, new Comparator<String[]>() {
        @Override
        public int compare(final String[] entry1, final String[] entry2) {
          final String time1 = entry1[1];
          final String time2 = entry2[1];
          return time2.compareTo(time1);
        }
      });

      tableHistory.setModel(new DefaultTableModel(data, columnNames));

      fr.close();
    } catch (Exception e) {
      System.out.println(e);
      return;
    }
  }
 
  private static int randomNumber(int size){
    return rand.nextInt(size);
  }

  private static HashMap<String, String> randomHashMapSlangWords(int count){
    HashMap<String, String> randomSlangWords = new HashMap<String, String>();
    for (int i = 0; i < count; i++) {
      int randomIndex = randomNumber(slangHashMap.size());
      int j = 0;
      for (String slangWord : slangHashMap.keySet()) {
        if(j == randomIndex){
          randomSlangWords.put(slangWord,  slangHashMap.get(slangWord));
          break;
        }
        j++;
      }
    }

    return randomSlangWords;
  }

  private static void quizSlangWord(){
    JFrame quizFrame = new JFrame("Quiz");
    quizFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    quizFrame.setLocationRelativeTo(null);
    quizFrame.setLayout(new BorderLayout());

    JPanel panelQuiz = new JPanel();
    panelQuiz.setPreferredSize(new Dimension(400, 200));
    panelQuiz.setLayout(new BoxLayout(panelQuiz, BoxLayout.Y_AXIS));

    HashMap<String, String> randomSlangWords = randomHashMapSlangWords(NUM_OF_ANSWERS_QUIZ);

    String word = "";
    int randomIndex = randomNumber(NUM_OF_ANSWERS_QUIZ);
    int j = 0;
    for (String slangWord : randomSlangWords.keySet()) {
      if(j == randomIndex){
        word = slangWord;
        break;
      }
      j++;
    }

    JPanel panelQuizTitle = new JPanel();
    panelQuizTitle.setLayout(new BoxLayout(panelQuizTitle, BoxLayout.X_AXIS));
    JLabel quizTitle = new JLabel(word + " - Choose the correct one: ");
    quizTitle.setFont(new Font("Arial", Font.BOLD, 20));
    panelQuizTitle.add(Box.createRigidArea(new Dimension(10, 0)));
    panelQuizTitle.add(quizTitle);
    panelQuizTitle.add(Box.createRigidArea(new Dimension(10, 0)));
    panelQuizTitle.add(Box.createHorizontalGlue());
    panelQuiz.add(panelQuizTitle);

    JPanel panelQuizAnswer = new JPanel();
    panelQuizAnswer.setLayout(new BoxLayout(panelQuizAnswer, BoxLayout.Y_AXIS));
    ButtonGroup group = new ButtonGroup();
    for (String slangWord : randomSlangWords.values()) {
      JRadioButton button = new JRadioButton(slangWord);
      group.add(button);
      panelQuizAnswer.add(button);
    }

    panelQuiz.add(panelQuizAnswer);

    JPanel panelQuizButton = new JPanel();
    panelQuizButton.setLayout(new BoxLayout(panelQuizButton, BoxLayout.X_AXIS));
    JButton buttonSubmit = new JButton("Submit");

    buttonSubmit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Enumeration<AbstractButton> buttons = group.getElements();
        int i = 1;
        while(buttons.hasMoreElements()){
          AbstractButton button = buttons.nextElement();
          if(button.isSelected()){
            if(i == randomIndex + 1){
              JOptionPane.showMessageDialog(null, "Correct!");

              panelQuiz.setVisible(false);
              quizFrame.dispose();
            } else {
              JOptionPane.showMessageDialog(null, "Wrong!");
            }
            break;
          }
          i++;
        }
      }
    });

    panelQuizButton.add(buttonSubmit);

    panelQuiz.add(panelQuizButton);

    // JOptionPane.showMessageDialog(null, panelQuiz);

    quizFrame.setVisible(true);

   
    quizFrame.add(panelQuiz);

    quizFrame.pack();
  }

  private static void quizWithDefinition(){
    JFrame quizFrame = new JFrame("Quiz");
    quizFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    quizFrame.setLocationRelativeTo(null);
    quizFrame.setLayout(new BorderLayout());

    JPanel panelQuiz = new JPanel();
    panelQuiz.setLayout(new BoxLayout(panelQuiz, BoxLayout.Y_AXIS));

    JPanel panelQuizTitle = new JPanel();
    panelQuizTitle.setLayout(new BoxLayout(panelQuizTitle, BoxLayout.X_AXIS));

    HashMap<String, String> randomSlangWords = randomHashMapSlangWords(NUM_OF_ANSWERS_QUIZ);

    String definition = "";
    int randomIndex = randomNumber(NUM_OF_ANSWERS_QUIZ);
    int j = 0;
    for (String slangWord : randomSlangWords.keySet()) {
      if(j == randomIndex){
        definition = randomSlangWords.get(slangWord);
        break;
      }
      j++;
    }

    JLabel quizTitle = new JLabel(definition + " - Choose the correct one: ");
    quizTitle.setFont(new Font("Arial", Font.BOLD, 20));
    panelQuizTitle.add(Box.createRigidArea(new Dimension(10, 0)));
    panelQuizTitle.add(quizTitle);
    panelQuizTitle.add(Box.createRigidArea(new Dimension(10, 0)));
    panelQuizTitle.add(Box.createHorizontalGlue());
    panelQuiz.add(panelQuizTitle);


    // create panel quiz answer
    JPanel panelQuizAnswer = new JPanel();
    panelQuizAnswer.setLayout(new BoxLayout(panelQuizAnswer, BoxLayout.Y_AXIS));
    ButtonGroup group = new ButtonGroup();
    for (String slangWord : randomSlangWords.keySet()) {
      JRadioButton button = new JRadioButton(slangWord);
      group.add(button);
      panelQuizAnswer.add(button);
    }

    panelQuiz.add(panelQuizAnswer);

    // create panel quiz button
    JPanel panelQuizButton = new JPanel();
    panelQuizButton.setLayout(new BoxLayout(panelQuizButton, BoxLayout.X_AXIS));
    JButton buttonSubmit = new JButton("Submit");
    
    buttonSubmit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Enumeration<AbstractButton> buttons = group.getElements();
        int i = 1;
        while(buttons.hasMoreElements()){
          AbstractButton button = buttons.nextElement();
          if(button.isSelected()){
            if(i == randomIndex + 1){
              JOptionPane.showMessageDialog(null, "Correct!");

              // close panelQuiz
              // panelQuiz.setVisible(false);
              quizFrame.dispose();
            } else {
              JOptionPane.showMessageDialog(null, "Wrong!");
            }
            break;
          }
          i++;
        }
      }
    });

    panelQuizButton.add(buttonSubmit);

    panelQuiz.add(panelQuizButton);

    // JOptionPane.showMessageDialog(null, panelQuiz);

    quizFrame.setVisible(true);

    quizFrame.add(panelQuiz);

    quizFrame.pack();
  }

  private static void addASlangWord(){
    JFrame addFrame = new JFrame("Add a slang word");
    addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    addFrame.setLocationRelativeTo(null);
    addFrame.setLayout(new BorderLayout());

    JPanel panelAddSlangWord = new JPanel();
    panelAddSlangWord.setLayout(new BoxLayout(panelAddSlangWord, BoxLayout.Y_AXIS));

    JPanel panelAddSlangWordTitle = new JPanel();
    panelAddSlangWordTitle.setLayout(new BoxLayout(panelAddSlangWordTitle, BoxLayout.X_AXIS));
    JLabel addSlangWordTitle = new JLabel("Enter slang word: ");
    addSlangWordTitle.setFont(new Font("Arial", Font.BOLD, 20));
    panelAddSlangWordTitle.add(addSlangWordTitle);
    panelAddSlangWordTitle.add(Box.createRigidArea(new Dimension(10, 0)));
    panelAddSlangWordTitle.add(Box.createHorizontalGlue());
    panelAddSlangWord.add(panelAddSlangWordTitle);

    JPanel panelAddSlangWordInput = new JPanel();
    panelAddSlangWordInput.setLayout(new BoxLayout(panelAddSlangWordInput, BoxLayout.X_AXIS));
    JTextField addSlangWordInput = new JTextField();
    addSlangWordInput.setFont(new Font("Arial", Font.PLAIN, 20));
    panelAddSlangWordInput.add(addSlangWordInput);
    panelAddSlangWordInput.add(Box.createRigidArea(new Dimension(10, 0)));
    panelAddSlangWordInput.add(Box.createHorizontalGlue());
    panelAddSlangWord.add(panelAddSlangWordInput);

    JPanel panelAddSlangWordDefinitionTitle = new JPanel();
    panelAddSlangWordDefinitionTitle.setLayout(new BoxLayout(panelAddSlangWordDefinitionTitle, BoxLayout.X_AXIS));
    JLabel addSlangWordDefinitionTitle = new JLabel("Enter definition: ");
    addSlangWordDefinitionTitle.setFont(new Font("Arial", Font.BOLD, 20));
    panelAddSlangWordDefinitionTitle.add(addSlangWordDefinitionTitle);
    panelAddSlangWordDefinitionTitle.add(Box.createRigidArea(new Dimension(10, 0)));
    panelAddSlangWordDefinitionTitle.add(Box.createHorizontalGlue());
    panelAddSlangWord.add(panelAddSlangWordDefinitionTitle);

    JPanel panelAddSlangWordDefinitionInput = new JPanel();
    panelAddSlangWordDefinitionInput.setLayout(new BoxLayout(panelAddSlangWordDefinitionInput, BoxLayout.X_AXIS));
    JTextField addSlangWordDefinitionInput = new JTextField();
    addSlangWordDefinitionInput.setFont(new Font("Arial", Font.PLAIN, 20));
    panelAddSlangWordDefinitionInput.add(addSlangWordDefinitionInput);
    panelAddSlangWordDefinitionInput.add(Box.createRigidArea(new Dimension(10, 0)));
    panelAddSlangWordDefinitionInput.add(Box.createHorizontalGlue());
    panelAddSlangWord.add(panelAddSlangWordDefinitionInput);

    JPanel panelAddSlangWordButton = new JPanel();
    panelAddSlangWordButton.setLayout(new BoxLayout(panelAddSlangWordButton, BoxLayout.X_AXIS));
    JButton buttonAddSlangWord = new JButton("Add");

    buttonAddSlangWord.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
       // check existence
        String slangWord = addSlangWordInput.getText();
        String definition = addSlangWordDefinitionInput.getText();
        if(slangHashMap.containsKey(slangWord)){
          // JOptionPane.showMessageDialog(null, "Slang word already exists");

          // add meaning for existing slang word
          String existingDefinition = slangHashMap.get(slangWord);
          String newDefinition = existingDefinition + "|" + definition;
          slangHashMap.put(slangWord, newDefinition);

          removeKeywordsByDefinition(slangWord);
          addKeywordsByDefinitionHashMap(slangWord, newDefinition);
      
          saveIndexDictionaryData();

          JOptionPane.showMessageDialog(null, "Slang word updated definition successfully");

          // find index in in searchList with slang word
          int index = 0;
          for(int i = 0; i < searchList.getModel().getSize(); i++){
            String textString = searchList.getModel().getElementAt(i);
            String slangWordTemp = textString.substring(0, textString.indexOf(" - "));
            if(slangWordTemp.equals(slangWord)){
              index = i;
              break;
            }
          }

          // add new element to searchLists
          // searchList.setListData(new String[]{});
          listSearchResultModel.setElementAt(slangWord + " - " + newDefinition, index);


          addFrame.dispose();
          return;
        }

        slangHashMap.put(slangWord, definition);
        addKeywordsByDefinitionHashMap(slangWord, definition);
        addKeywordHashMap(slangWord);

        saveIndexDictionaryData();

        JOptionPane.showMessageDialog(null, "Slang word added successfully");

        panelAddSlangWord.setVisible(false);

        listSearchResultModel.addElement(slangWord);

        addFrame.dispose();
      }
    });

    panelAddSlangWordButton.add(buttonAddSlangWord);

    panelAddSlangWord.add(panelAddSlangWordButton);

    // JOptionPane.showMessageDialog(null, panelAddSlangWord);

    addFrame.setVisible(true);

    addFrame.add(panelAddSlangWord);

    addFrame.pack();
  }

  private static void removeKeywordsByDefinition(String slangWord){
    String definition = slangHashMap.get(slangWord);
    definition = definition.replace("|", " ");
    definition = definition.replace("   ", " ");
    definition = definition.replace("  ", " ");
    String[] keywords = definition.split(" ");
    for(String keyword : keywords){
      if(definitionHashMap.containsKey(keyword)){
        definitionHashMap.get(keyword).remove(slangWord);
      }
    }
  }

  private static void addKeywordsByDefinitionHashMap(String slangWord, String definition){
    definition = definition.replace("|", " ");
    definition = definition.replace("   ", " ");
    definition = definition.replace("  ", " ");
    String[] keywords = definition.split(" ");
    for(String keyword : keywords){
      if(definitionHashMap.containsKey(keyword)){
        definitionHashMap.get(keyword).add(slangWord);
      } else {
        HashSet<String> temp = new HashSet<String>();
        temp.add(slangWord);
        definitionHashMap.put(keyword, temp);
      }
    }
  }

  private static void addKeywordHashMap(String slangWord){
    for(int i = 0; i < slangWord.length(); i++){
      String subString = slangWord.substring(0, i + 1);
      if(!slangWordIndex.containsKey(subString)){
        slangWordIndex.put(subString, new HashSet<String>());
      }
      slangWordIndex.get(subString).add(slangWord);
    }
  }

  private static void removeKeywordIndex(String slangWord){
    for(int i = 0; i < slangWord.length(); i++){
      String subString = slangWord.substring(0, i + 1);
      slangWordIndex.get(subString).remove(slangWord);
    }
  }

  private static void saveIndexDictionaryData(){
    try {
      String fileName = INDEX_FILE_NAME;
      FileWriter fw = new FileWriter(fileName);

      for(String keyword: definitionHashMap.keySet()){
          fw.write(keyword);
          for(String slangWord : definitionHashMap.get(keyword)){
            fw.write(" " + slangWord);
          }
          fw.write("\n");
      }
      fw.close();

      // save slang word index to file
      fileName = SLANG_WORD_INDEX_FILE_NAME;
      fw = new FileWriter(fileName);

      for(String keyword: slangWordIndex.keySet()){
          fw.write(keyword);
          int index = 0;
          for(String slangWord : slangWordIndex.get(keyword)){
            if(index == 0){
              fw.write("`" + slangWord);
            }else{
              fw.write(" " + slangWord);
            }
            index++;
          }
          fw.write("\n");
      }

      fw.close();

      // save slang word to file
      fileName = DATASET_CLONE_FILE_NAME;
      fw = new FileWriter(fileName);

      for(String slangWord: slangHashMap.keySet()){
          fw.write(slangWord + "`" + slangHashMap.get(slangWord) + "\n");
      }

      fw.close();

    } catch (Exception e) {
      System.out.println(e);
      return;
    }
  }

  private static void editASlangWord(){
    // check selected on list or not
    if(searchList.getSelectedIndex() == -1){
      JOptionPane.showMessageDialog(null, "Please select a slang word to edit");
      return;
    }

    JFrame editFrame = new JFrame("Edit a slang word");
    editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    editFrame.setSize(500, 500);
    editFrame.setLocationRelativeTo(null);

    JPanel panelEditSlangWord = new JPanel();
    panelEditSlangWord.setLayout(new BoxLayout(panelEditSlangWord, BoxLayout.Y_AXIS));

    JPanel panelEditSlangWordTitle = new JPanel();
    panelEditSlangWordTitle.setLayout(new BoxLayout(panelEditSlangWordTitle, BoxLayout.X_AXIS));
    JLabel editSlangWordTitle = new JLabel("Edit a slang word");
    editSlangWordTitle.setFont(new Font("Arial", Font.BOLD, 30));
    panelEditSlangWordTitle.add(editSlangWordTitle);
    panelEditSlangWordTitle.add(Box.createRigidArea(new Dimension(10, 0)));
    panelEditSlangWordTitle.add(Box.createHorizontalGlue());
    panelEditSlangWord.add(panelEditSlangWordTitle);

    JPanel panelEditSlangWordInput = new JPanel();
    panelEditSlangWordInput.setLayout(new BoxLayout(panelEditSlangWordInput, BoxLayout.X_AXIS));
    JTextField editSlangWordInput = new JTextField();
    editSlangWordInput.setFont(new Font("Arial", Font.PLAIN, 20));
    editSlangWordInput.setEditable(false);

    String textString = searchList.getSelectedValue();
    // get slangWord from textString
    String slangWordOld = textString.substring(0, textString.indexOf(" - "));

    editSlangWordInput.setText(slangWordOld);
    panelEditSlangWordInput.add(editSlangWordInput);
    panelEditSlangWordInput.add(Box.createRigidArea(new Dimension(10, 0)));
    panelEditSlangWordInput.add(Box.createHorizontalGlue());
    panelEditSlangWord.add(panelEditSlangWordInput);

    JPanel panelEditSlangWordDefinitionInput = new JPanel();
    panelEditSlangWordDefinitionInput.setLayout(new BoxLayout(panelEditSlangWordDefinitionInput, BoxLayout.X_AXIS));
    JTextField editSlangWordDefinitionInput = new JTextField();
    editSlangWordDefinitionInput.setFont(new Font("Arial", Font.PLAIN, 20));
    editSlangWordDefinitionInput.setText(slangHashMap.get(slangWordOld));
    panelEditSlangWordDefinitionInput.add(editSlangWordDefinitionInput);
    panelEditSlangWordDefinitionInput.add(Box.createRigidArea(new Dimension(10, 0)));
    panelEditSlangWordDefinitionInput.add(Box.createHorizontalGlue());
    panelEditSlangWord.add(panelEditSlangWordDefinitionInput);

    JPanel panelEditSlangWordButton = new JPanel();
    panelEditSlangWordButton.setLayout(new BoxLayout(panelEditSlangWordButton, BoxLayout.X_AXIS));
    JButton buttonEditSlangWord = new JButton("Edit");

    buttonEditSlangWord.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String slangWord = editSlangWordInput.getText();

        if(!slangHashMap.containsKey(slangWord)){
          JOptionPane.showMessageDialog(null, "Slang word not found!");
          return;
        }

        String definition = editSlangWordDefinitionInput.getText();
        removeKeywordsByDefinition(slangWord);
        removeKeywordIndex(slangWord);
        addKeywordsByDefinitionHashMap(slangWord, definition);
        addKeywordHashMap(slangWord);
        slangHashMap.put(slangWord, definition);
    
        saveIndexDictionaryData();

        JOptionPane.showMessageDialog(null, "Slang word edited!");

        panelEditSlangWord.setVisible(false);

        // find index in in searchList with slang word
        int index = 0;
        for(int i = 0; i < searchList.getModel().getSize(); i++){
          String textString = searchList.getModel().getElementAt(i);
          String slangWordTemp = textString.substring(0, textString.indexOf(" - "));
          if(slangWordTemp.equals(slangWord)){
            index = i;
            break;
          }
        }

        // add new element to searchLists
        // searchList.setListData(new String[]{});
        listSearchResultModel.setElementAt(slangWord + " - " + definition, index);

        editFrame.dispose();
      }
    });

    panelEditSlangWordButton.add(buttonEditSlangWord);

    panelEditSlangWord.add(panelEditSlangWordButton);

    // JOptionPane.showMessageDialog(null, panelAddSlangWord);

    editFrame.setVisible(true);

    editFrame.add(panelEditSlangWord);

    editFrame.pack();

  }

  private static void deleteASlangWord(){
    String textString = searchList.getSelectedValue();

    String slangWord = textString.substring(0, textString.indexOf(" - "));

    if(!slangHashMap.containsKey(slangWord)){
      JOptionPane.showMessageDialog(null, "Slang word "+slangWord+" does not exist");
      return;
    }

    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure to delete slang word "+slangWord+"?", "Confirm", JOptionPane.YES_NO_OPTION);

    if(confirm == JOptionPane.YES_OPTION){
      removeKeywordsByDefinition(slangWord);
      removeKeywordIndex(slangWord);
      slangHashMap.remove(slangWord);
  
      saveIndexDictionaryData();

      int index = searchList.getSelectedIndex();
      
      if (index != -1) {
        listSearchResultModel.removeElementAt(index);
        JOptionPane.showMessageDialog(null, "Slang word " + slangWord + " deleted!");
      }else{
        JOptionPane.showMessageDialog(null, "Slang word " + slangWord + " deleted failure!");
      }
    } else {
      JOptionPane.showMessageDialog(null, "Slang word " + slangWord + " not deleted!");
    }
  }

  private static void loadSlangWordIndexFromFile(){
    try {
      File file = new File(SLANG_WORD_INDEX_FILE_NAME);
      Scanner sc = new Scanner(file);

      while (sc.hasNextLine()) {
        String line = sc.nextLine();
        String[] words = line.split("`");
        String keyword = words[0];
        words = words[1].split(" ");
        for(int i = 0; i < words.length; i++){
          if(keyword == "" || keyword == " ") continue;
          if(!slangWordIndex.containsKey(keyword)){
            slangWordIndex.put(keyword, new HashSet<String>());
          }
          slangWordIndex.get(keyword).add(words[i]);
        }
      }
      sc.close();
    } catch (Exception e) {
      System.out.println(e);
      return;
    }
  }

  private static void loadIndexDictionaryFromFile(){
    try {
      File file = new File(INDEX_FILE_NAME);
      Scanner sc = new Scanner(file);

      while (sc.hasNextLine()) {
        String line = sc.nextLine();
        String[] words = line.split(" ");
        String keyword = words[0];
        for(int i = 1; i < words.length; i++){
          if(!definitionHashMap.containsKey(keyword)){
            definitionHashMap.put(keyword, new HashSet<String>());
          }
          definitionHashMap.get(keyword).add(words[i]);
        }
      }
      sc.close();
    } catch (Exception e) {
      System.out.println(e);
      return;
    }
  }

  private static void loadDataSetFromFile(){
    try {
      FileReader fr = new FileReader(DATASET_CLONE_FILE_NAME);
      BufferedReader br = new BufferedReader(fr);

      String line;
      while((line = br.readLine()) != null){
        if(!line.contains("`")){
          continue;
        }
        String[] split = line.split("`");

        slangHashMap.put(split[0], split[1]);
      }
      fr.close();
    } catch (Exception e) {
      System.out.println(e);
      return;
    }
  }

  private static void handleIndexBeforeRunningApp(){
    try {
      File file = new File(INDEX_FILE_NAME);
      
      File file2 = new File(SLANG_WORD_INDEX_FILE_NAME);

      File file3 = new File(DATASET_CLONE_FILE_NAME);
      if(file.exists() && file2.exists() && file3.exists()){
        loadSlangWordIndexFromFile();
        loadIndexDictionaryFromFile();
        loadDataSetFromFile();

      }else{
        loadSlangWords();
        saveIndexDictionaryData();
      }
    } catch (Exception e) {
      System.out.println(e);
      return;
    }
  }
  private dictionary(){
    setLayout(new BorderLayout());
    JPanel leftpanel = new JPanel();
    leftpanel.setPreferredSize(new Dimension(500, 500));
    
    searchField = new JTextField(20);
    searchField.addActionListener(this);

    String[] searchOptionsData = { "Search by slang word", "Search by definition" };
    searchOptions = new JComboBox<String>(searchOptionsData);
    searchOptions.setSelectedIndex(0);
    searchOptions.addActionListener(this);

    // add a label above search
    JLabel searchLabel = new JLabel("* Slang word: search by letter; *Definition: full-text search, search by word");
    searchLabel.setForeground(Color.BLUE);

    searchButton = new JButton("Search");
    searchButton.addActionListener(this);
    
    searchList = new JList<String>(); 
    searchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    searchList.setLayoutOrientation(JList.VERTICAL);
    searchList.setVisibleRowCount(-1);
    JScrollPane listScroller = new JScrollPane(searchList);
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
    leftpanel.add(searchOptions);
    leftpanel.add(searchButton);
    leftpanel.add(searchLabel);
    leftpanel.add(listScroller);
    leftpanel.add(btnList);

    JPanel rightpanel = new JPanel();
    rightpanel.setPreferredSize(new Dimension(300, 500));

    JPanel todayWord = new JPanel();
    todayWord.setPreferredSize(new Dimension(250, 100));
    todayWord.setBackground(Color.WHITE);
    todayWordLabel = new JLabel();
    JPanel definition = new JPanel();
    definition.setPreferredSize(new Dimension(250, 100));
    definition.setBackground(Color.WHITE);
    todayWordDefinitionLabel = new JLabel();
    definition.add(todayWordDefinitionLabel);

    todayWord.add(todayWordLabel);
    todayWord.add(definition);

    JPanel history = new JPanel();
    history.setPreferredSize(new Dimension(250, 250));
    history.setBackground(Color.WHITE);
    JLabel historyLabel = new JLabel("History");
    history.add(historyLabel);

    String[] columnNames = {"Slang word", "Date"};
    String[][] data = {{"", ""}};
    tableHistory = new JTable(new DefaultTableModel());

    tableHistory.setModel(new DefaultTableModel(data, columnNames));
    tableHistory.setPreferredScrollableViewportSize(new Dimension(200, 200));
    tableHistory.setFillsViewportHeight(true);

    JScrollPane scrollPane = new JScrollPane(tableHistory);
    history.add(scrollPane);

    JPanel btnList2 = new JPanel();
    btnList2.setLayout(new BoxLayout(btnList2, BoxLayout.LINE_AXIS));
    quizOpenWithSlangWord = new JButton("Quiz Word");
    quizOpenWithSlangWord.addActionListener(this);
    quizOpenWithDefinition = new JButton("Quiz Definition");
    quizOpenWithDefinition.addActionListener(this);
    btnList2.add(quizOpenWithSlangWord);
    btnList2.add(Box.createRigidArea(new Dimension(50,15)));
    btnList2.add(quizOpenWithDefinition);

    rightpanel.add(todayWord);
    rightpanel.add(history);
    rightpanel.add(btnList2);

    add(leftpanel, BorderLayout.WEST);
    add(rightpanel, BorderLayout.EAST);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == searchButton){
      if(searchOptions.getSelectedIndex() == 0){
        searchBySlangWord();
      } else {
        searchByDefinition();
      }
    }else if(e.getSource() == createButton){
      // create
      addASlangWord();
    }else if(e.getSource() == editButton){
      editASlangWord();
    }else if(e.getSource() == deleteButton){
      deleteASlangWord();
    }else if(e.getSource() == quizOpenWithSlangWord){
      quizSlangWord();
    } else if(e.getSource() == quizOpenWithDefinition){
      quizWithDefinition();
    } else if(e.getSource() == resetButton){
      resetDataToDefault();
    }
  }

  private static void createAndShowGUI() 
  {
    //Make sure we have nice window decorations.
    JFrame.setDefaultLookAndFeelDecorated(true);
    JDialog.setDefaultLookAndFeelDecorated(true);

    //Create and set up the window.
    frame = new JFrame("Dictionary");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Create and set up the content pane.
    JComponent newContentPane = new dictionary();
    newContentPane.setOpaque(true); //content panes must be opaque
    frame.setContentPane(newContentPane);

    //Display the window.
    frame.pack(); // fit to all components that you have
    frame.setVisible(true);
  }


  private static void loadAllSlangWordsToSearchResult(){
    listSearchResultModel = new DefaultListModel<String>();

    for (Map.Entry<String, String> entry : slangHashMap.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      listSearchResultModel.addElement(key + " - " + value);
    }

    // listSearchResultModel.addAll(slangHashMap.keySet());

    searchList.setModel(listSearchResultModel);
  }
  
  private static void resetDataToDefault(){
    File file = new File(INDEX_FILE_NAME);
    file.delete();
    file = new File(HISTORY_FILE_NAME);
    file.delete();
    file = new File(SLANG_WORD_INDEX_FILE_NAME);
    file.delete();
    file = new File(DATASET_CLONE_FILE_NAME);
    file.delete();

    slangHashMap.clear();
    slangWordIndex.clear();
    definitionHashMap.clear();

    handleIndexBeforeRunningApp();

    loadAllSlangWordsToSearchResult();

    loadHistory();

    randomSlangWordToday();
  }

  public static void main(String[] args) {

    javax.swing.SwingUtilities.invokeLater(new Runnable() 
    {
        public void run() 
        {
            handleIndexBeforeRunningApp();
            createAndShowGUI();
            loadAllSlangWordsToSearchResult();
            randomSlangWordToday();
            loadHistory();
        }
    });
}
}
