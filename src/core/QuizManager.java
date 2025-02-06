package core;

import java.util.*;

public class QuizManager {
    private static final int NUM_OF_ANSWERS_QUIZ = 4;
    private HashMap<String, String> slangHashMap;
    private Random rand;

    public QuizManager(HashMap<String, String> slangHashMap) {
        this.slangHashMap = slangHashMap;
        this.rand = new Random();
    }

    public void quizWithSlangWord() {
        if (slangHashMap.isEmpty()) return;

        List<String> keys = new ArrayList<>(slangHashMap.keySet());
        String correctSlangWord = keys.get(rand.nextInt(keys.size()));
        String correctDefinition = slangHashMap.get(correctSlangWord);

        Set<String> options = new HashSet<>();
        options.add(correctDefinition);

        while (options.size() < NUM_OF_ANSWERS_QUIZ) {
            String randomDefinition = slangHashMap.get(keys.get(rand.nextInt(keys.size())));
            options.add(randomDefinition);
        }

        List<String> optionsList = new ArrayList<>(options);
        Collections.shuffle(optionsList);

        System.out.println("What is the definition of: " + correctSlangWord);
        for (int i = 0; i < optionsList.size(); i++) {
            System.out.println((i + 1) + ". " + optionsList.get(i));
        }

        // Here you would implement the logic to get the user's answer and check if it's correct
    }

    public void quizWithDefinition() {
        if (slangHashMap.isEmpty()) return;

        List<String> keys = new ArrayList<>(slangHashMap.keySet());
        String correctSlangWord = keys.get(rand.nextInt(keys.size()));
        String correctDefinition = slangHashMap.get(correctSlangWord);

        Set<String> options = new HashSet<>();
        options.add(correctSlangWord);

        while (options.size() < NUM_OF_ANSWERS_QUIZ) {
            String randomSlangWord = keys.get(rand.nextInt(keys.size()));
            options.add(randomSlangWord);
        }

        List<String> optionsList = new ArrayList<>(options);
        Collections.shuffle(optionsList);

        System.out.println("Which slang word matches the definition: " + correctDefinition);
        for (int i = 0; i < optionsList.size(); i++) {
            System.out.println((i + 1) + ". " + optionsList.get(i));
        }

        // Here you would implement the logic to get the user's answer and check if it's correct
    }
}