import java.util.*;
import java.io.*;

public class dictionary {
  static HashMap<String, String> slangHashMap = new HashMap<String, String>();
  static HashMap<String, HashMap<String, Boolean>> definitionHashMap = new HashMap<String, HashMap<String, Boolean>>();

  private static void searchBySlangWord(){
    System.out.println("===================================");
    System.out.println("Search by slang word");
    System.out.print("Enter slang word: ");
    String slangWord = System.console().readLine();
    if(slangHashMap.containsKey(slangWord)){
      System.out.println("Definition: " + slangHashMap.get(slangWord));
    } else {
      System.out.println("Slang word not found");
    }
  }

  private static void searchByDefinition(){
    System.out.println("===================================");
    System.out.println("Search by definition");
    System.out.print("Enter definition: ");
    String keywords = System.console().readLine();

    String[] keywordsSplit = keywords.split(" ");
    HashMap<String, Integer> mostMatchSlangWord = new HashMap<String, Integer>();
    
    Set<String> slangWordResult = new HashSet<String>();
    boolean first = true;
    for (String keyword : keywordsSplit) {
      if(slangWordResult.size() == 0 && first){
        slangWordResult.addAll(definitionHashMap.get(keyword).keySet());
        first = false;
      }else if(definitionHashMap.containsKey(keyword)){
        slangWordResult.retainAll(definitionHashMap.get(keyword).keySet());
      }
    }

    System.out.println("Slang words: ");
    for (String slangWord : slangWordResult) {
      System.out.println(slangWord + " ");
    }

  }

  private static void loadSlangWords(String fileName){
    // load slang words
    try {
      FileReader fr = new FileReader(fileName);
      BufferedReader br = new BufferedReader(fr);

      String line = br.readLine();
      while((line = br.readLine()) != null){
        if(!line.contains("`")){
          continue;
        }
        String[] split = line.split("`");
        slangHashMap.put(split[0], split[1]);

        String[] definitionKeywords = split[1].split(" ");

        for(String keyword : definitionKeywords){
          if(definitionHashMap.containsKey(keyword)){
            definitionHashMap.get(keyword).put(split[0], true);
          } else {
            HashMap<String, Boolean> temp = new HashMap<String, Boolean>();
            temp.put(split[0], true);
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

  private static void searchHistory(){
    System.out.println("===================================");
    System.out.println("Search history");

    try {
      FileReader fr = new FileReader("search_history.txt");
      BufferedReader br = new BufferedReader(fr);

      String line = br.readLine();
      while((line = br.readLine()) != null){
        String[] split = line.split("|");
        System.out.println(split[0] + " - " + split[1]);
      }
      fr.close();
    } catch (Exception e) {
      // TODO: handle exception

      System.out.println(e);
      return;
    }
  }
  static Random rand = new Random();
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

  static final int numOfAnswersQuiz = 4;

  private static void quizSlangWord(){
    System.out.println("===================================");
    System.out.println("Quiz slang word");

    HashMap<String, String> randomSlangWords = randomHashMapSlangWords(numOfAnswersQuiz);

    String word = "";
    int randomIndex = randomNumber(numOfAnswersQuiz);
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

    HashMap<String, String> randomSlangWords = randomHashMapSlangWords(numOfAnswersQuiz);

    String definition = "";
    int randomIndex = randomNumber(numOfAnswersQuiz);
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

    String[] definitionKeywords = definition.split(" ");

    for(String keyword : definitionKeywords){
      if(definitionHashMap.containsKey(keyword)){
        definitionHashMap.get(keyword).put(slangWord, true);
      } else {
        HashMap<String, Boolean> temp = new HashMap<String, Boolean>();
        temp.put(slangWord, true);
        definitionHashMap.put(keyword, temp);
      }
    }
  }

  public static void main(String[] args) {
    loadSlangWords("slang.txt");

    // searchByDefinition();
    // randomASlangWord();
    // quizSlangWord();
    quizWithDefinition();

    // while(true) {
    //   System.out.println("===================================");
    //   System.out.println("Dictionary");
    //   System.out.println("1. search by slang word");
    //   System.out.println("2. search by definition");
    //   System.out.println("3. search history");
    //   System.out.println("4. create a new slang word");
    //   System.out.println("5. edit a slang word");
    //   System.out.println("6. save student");
    //   System.out.println("7. reset to default");
    //   System.out.println("8. slang word on this day");
    //   System.out.println("9. slang word to choose definition");
    //   System.out.println("10. definition to choose slang word");
    //   System.out.print("Enter your choice: ");
    //   int choice = Integer.parseInt(System.console().readLine());
    //   switch(choice){
    //     case 1:
    //       searchBySlangWord();
    //       break;
    //     case 9:
    //       System.exit(0);
    //       break;
    //     default:
    //       System.out.println("Invalid choice");
    //       break;
    //   }
    // }
 
  }
}
