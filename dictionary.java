import java.util.*;
import java.io.*;
import java.util.regex.*;

public class dictionary {
  static HashMap<String, String> slangHashMap = new HashMap<String, String>();
  static HashMap<String, HashSet<String>> definitionHashMap = new HashMap<String, HashSet<String>>();
  static HashMap<String, HashSet<String>> slangWordIndex = new HashMap<String, HashSet<String>>();
  static final String HISTORY_FILE_NAME = "history.txt";
  static final String DATASET_FILE_NAME = "slang.txt";
  static final String INDEX_FILE_NAME = "index.txt";
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
    System.out.println("===================================");
    System.out.println("Search by slang word");
    System.out.print("Enter slang word: ");
    String slangWord = System.console().readLine();
    if(slangWordIndex.containsKey(slangWord)){
      // slow list of slangwords
      HashSet<String> slangWords = slangWordIndex.get(slangWord);
      for(String s : slangWords){
        System.out.println(s + ": " + slangHashMap.get(s));
      }
    } else {
      System.out.println("Not found");
    }

    saveHistorySearch(slangWord);
  }

  private static void searchByDefinition(){
    System.out.println("===================================");
    System.out.println("Search by definition");
    System.out.print("Enter definition: ");
    String keywords = System.console().readLine();

    String[] keywordsSplit = keywords.split(" ");
    
    Set<String> slangWordResult = new HashSet<String>();
    boolean first = true;
    for (String keyword : keywordsSplit) {
      if(slangWordResult.size() == 0 && first){
        slangWordResult.addAll(definitionHashMap.get(keyword));
        first = false;
      }else if(definitionHashMap.containsKey(keyword)){
        slangWordResult.retainAll(definitionHashMap.get(keyword));
      }
    }

    System.out.println("Slang words: ");
    for (String slangWord : slangWordResult) {
      System.out.println(slangWord + " ");
    }
  }

  private static void loadSlangWords(){
    // load slang words
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
    System.out.println("===================================");

    HashMap<String, String> slangWord = randomHashMapSlangWords(1);
    System.out.println("Random slang word today: " + slangWord.keySet().toArray()[0]);
  }

  private static void searchHistory(){
    System.out.println("===================================");
    System.out.println("Search history");

    try {
      FileReader fr = new FileReader(HISTORY_FILE_NAME);
      BufferedReader br = new BufferedReader(fr);

      String line;
      while((line = br.readLine()) != null){
        String[] split = line.split(Pattern.quote("|"));
        System.out.println(split[0] + " - " + split[1]);
      }
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
    System.out.println("===================================");
    System.out.println("Random a slang word");

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
    System.out.println("===================================");
    System.out.println("Quiz slang word");

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


    System.out.println(word + " - Choose the correct definition for the slang word (1-4): ");
    int i = 1;
    for (String slangWord : randomSlangWords.keySet()) {
      System.out.println(i + ". " + randomSlangWords.get(slangWord));
      i++;
    }

    System.out.print("Your answer: ");
    int answer = Integer.parseInt(System.console().readLine());
    if(answer == randomIndex + 1){
      System.out.println("Correct");
    } else {
      System.out.println("Incorrect");
    }
  }

  private static void quizWithDefinition(){
    System.out.println("===================================");
    System.out.println("Quiz with definition");

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


    System.out.println(definition + " - Choose the correct definition for the slang word (1-4): ");
    int i = 1;
    for (String slangWord : randomSlangWords.keySet()) {
      System.out.println(i + ". " + slangWord);
      i++;
    }

    System.out.print("Your answer: ");
    int answer = Integer.parseInt(System.console().readLine());
    if(answer == randomIndex + 1){
      System.out.println("Correct");
    } else {
      System.out.println("Incorrect");
    }
  }

  private static void addASlangWord(){
    System.out.println("===================================");
    System.out.println("Add a slang word");

    System.out.print("Enter slang word: ");
    String slangWord = System.console().readLine();

    System.out.print("Enter definition: ");
    String definition = System.console().readLine();

    // check existence
    if(slangHashMap.containsKey(slangWord)){
      System.out.println("Slang word already exists");
      return;
    }

    slangHashMap.put(slangWord, definition);
    addKeywordsByDefinitionHashMap(slangWord, definition);
    addKeywordHashMap(slangWord);

    saveIndexDictionaryData();
  }

  private static void removeKeywordsByDefinition(String slangWord){
    String[] keywords = slangHashMap.get(slangWord).split(" ");
    for(String keyword : keywords){
      definitionHashMap.get(keyword).remove(slangWord);
    }
  }

  private static void addKeywordsByDefinitionHashMap(String slangWord, String definition){
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
          for(String slangWord : slangWordIndex.get(keyword)){
            fw.write(" " + slangWord);
          }
          fw.write("\n");
      }

      fw.close();

    } catch (Exception e) {
      System.out.println(e);
      return;
    }
  }

  private static void editASlangWord(){
    System.out.println("===================================");
    System.out.println("Edit a slang word");

    System.out.print("Enter slang word: ");
    String slangWord = System.console().readLine();

    if(!slangHashMap.containsKey(slangWord)){
      System.out.println("Slang word does not exist");
      return;
    }

    System.out.print("Enter definition: ");
    String definition = System.console().readLine();

    removeKeywordsByDefinition(slangWord);
    removeKeywordIndex(slangWord);
    addKeywordsByDefinitionHashMap(slangWord, definition);
    addKeywordHashMap(slangWord);
    slangHashMap.put(slangWord, definition);

    saveIndexDictionaryData();
  }

  private static void deleteASlangWord(){
    System.out.println("===================================");
    System.out.println("Delete a slang word");

    System.out.print("Enter slang word: ");
    String slangWord = System.console().readLine();

    // check existence
    if(!slangHashMap.containsKey(slangWord)){
      System.out.println("Slang word does not exist");
      return;
    }

    removeKeywordsByDefinition(slangWord);
    removeKeywordIndex(slangWord);
    slangHashMap.remove(slangWord);

    saveIndexDictionaryData();
  }

  private static void loadSlangWordIndexFromFile(){
    try {
      File file = new File(SLANG_WORD_INDEX_FILE_NAME);
      Scanner sc = new Scanner(file);

      while (sc.hasNextLine()) {
        String line = sc.nextLine();
        String[] words = line.split(" ");
        String keyword = words[0];
        for(int i = 1; i < words.length; i++){
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
      FileReader fr = new FileReader(DATASET_FILE_NAME);
      BufferedReader br = new BufferedReader(fr);

      String line = br.readLine();
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
      // check file index exists
      File file = new File(INDEX_FILE_NAME);
      
      // check file slang word index exists
      File file2 = new File(SLANG_WORD_INDEX_FILE_NAME);
      if(file.exists() && file2.exists()){
        loadSlangWordIndexFromFile();
        loadIndexDictionaryFromFile();
        loadDataSetFromFile();
      }else{
        loadSlangWords();
      }
    } catch (Exception e) {
      System.out.println(e);
      return;
    }
  }

  public static void main(String[] args) {
    handleIndexBeforeRunningApp();
    saveIndexDictionaryData();
    while(true){
      System.out.println("===================================");
      System.out.println("1. Search by definition");
      System.out.println("2. Search by slang word");
      System.out.println("3. Random a slang word");
      System.out.println("4. Quiz slang word");
      System.out.println("5. Quiz with definition");
      System.out.println("6. Add a slang word");
      System.out.println("7. Edit a slang word");
      System.out.println("8. Delete a slang word");
      System.out.println("9. Search History");
      System.out.println("10. Exit");
      System.out.print("Your choice: ");
      int choice = Integer.parseInt(System.console().readLine());

      switch(choice){
        case 1:
          searchByDefinition();
          break;
        case 2:
          searchBySlangWord();
          break;
        case 3:
          randomSlangWordToday();
          break;
        case 4:
          quizSlangWord();
          break;
        case 5:
          quizWithDefinition();
          break;
        case 6:
          addASlangWord();
          break;
        case 7:
          editASlangWord();
          break;
        case 8:
          deleteASlangWord();
          break;
        case 9:
          searchHistory();
          break;
        case 10:
          System.exit(0);
          break;
        default:
          System.out.println("Invalid choice");
          break;
      }
    } 
  
  }
}
