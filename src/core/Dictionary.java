package core;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class Dictionary {
    private static final String DATASET_FILE_NAME = "slang.txt";
    private static final String DATASET_CLONE_FILE_NAME = "slang_clone.txt";
    private static final String INDEX_FILE_NAME = "definition_index.txt";
    private static final String SLANG_WORD_INDEX_FILE_NAME = "slang_index.txt";

    private final HashMap<String, String> slangHashMap = new HashMap<>();
    private final HashMap<String, HashSet<String>> definitionHashMap = new HashMap<String, HashSet<String>>();
    private final HashMap<String, HashSet<String>> slangWordIndex = new HashMap<String, HashSet<String>>();

    private final Random rand = new Random();

    private final DefaultListModel<String> listModel;
    private final HistoryManager historyManager;

    public Dictionary(HistoryManager history) {
        listModel = new DefaultListModel<>();

        historyManager = history;
        handleIndexBeforeRunningApp();
        loadAllSlangWordsToSearchResult();
        randomSlangWordToday();
    }

    public HashMap<String, String> getSlangHashMap(){
        return slangHashMap;
    }

    private void loadSlangWords() {
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

    private void randomSlangWordToday() {
        if (slangHashMap.isEmpty()) return;
        Object[] keys = slangHashMap.keySet().toArray();
        String randomKey = (String) keys[rand.nextInt(keys.length)];
        String definition = slangHashMap.get(randomKey);
        System.out.println("Slang word of the day: " + randomKey + " - " + definition);
    }

    public void searchBySlangWord(String slangWord, DefaultListModel<String> listSearchResultModel) {
        listSearchResultModel.clear();
        if (slangHashMap.containsKey(slangWord)) {
            HashSet<String> slangWords = slangWordIndex.get(slangWord);
            listSearchResultModel.clear();

            // concentrate all slang words with its definition
            for(String slangWordWithDefinition : slangWords){
                if(Objects.equals(slangWordWithDefinition, "") || Objects.equals(slangWordWithDefinition, " ")) continue;
                String definition = slangHashMap.get(slangWordWithDefinition);
                if(definition == null) continue;
                listSearchResultModel.addElement(slangWordWithDefinition + " - " + definition);
            }

        } else {
            listSearchResultModel.addElement("No results found for: " + slangWord);
        }
    }

    public void searchByDefinition(String keywords, DefaultListModel<String> listSearchResultModel) {
        listSearchResultModel.clear();
        String[] keywordsSplit = keywords.split(" ");

        Set<String> slangWordResult = new HashSet<String>();
        boolean first = true;
        for (String keyword : keywordsSplit) {
            if(slangWordResult.isEmpty() && first && definitionHashMap.containsKey(keyword)){
                slangWordResult.addAll(definitionHashMap.get(keyword));
                first = false;
            }else if(definitionHashMap.containsKey(keyword)){
                slangWordResult.retainAll(definitionHashMap.get(keyword));
            }
        }

        // concentrate all slang words with its definition
        for(String slangWordWithDefinition : slangWordResult) {
            String definition = slangHashMap.get(slangWordWithDefinition);
            if (definition == null) continue;
            listSearchResultModel.addElement(slangWordWithDefinition + " - " + definition);
        }
    }

    private void loadSlangWordIndexFromFile(){
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

    private void loadIndexDictionaryFromFile(){
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

    private void loadDataSetFromFile(){
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

    private void handleIndexBeforeRunningApp() {
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

    public void saveIndexDictionaryData(){
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

    public DefaultListModel<String> getListModel(){
        return listModel;
    }

    public void addSlangWord(String slangWord, String definition) {
        slangHashMap.put(slangWord, definition);

        addKeywordsByDefinitionHashMap(slangWord, definition);
        addKeywordHashMap(slangWord);

        saveIndexDictionaryData();
        listModel.addElement(slangWord + " - " + definition);
    }

    private void addKeywordsByDefinitionHashMap(String slangWord, String definition){
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

    private void addKeywordHashMap(String slangWord){
        for(int i = 0; i < slangWord.length(); i++){
            String subString = slangWord.substring(0, i + 1);
            if(!slangWordIndex.containsKey(subString)){
                slangWordIndex.put(subString, new HashSet<String>());
            }
            slangWordIndex.get(subString).add(slangWord);
        }
    }

    public void editSlangWord(String slangWord, String newDefinition) {
        if (slangHashMap.containsKey(slangWord)) {
            removeKeywordsByDefinition(slangWord);
            removeKeywordIndex(slangWord);
            addKeywordsByDefinitionHashMap(slangWord, newDefinition);
            addKeywordHashMap(slangWord);

            slangHashMap.put(slangWord, newDefinition);
            saveIndexDictionaryData();

            updateListModel(slangWord, newDefinition);
        }
    }

    private void removeKeywordsByDefinition(String slangWord){
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

    private void removeKeywordIndex(String slangWord){
        for(int i = 0; i < slangWord.length(); i++){
            String subString = slangWord.substring(0, i + 1);
            if(slangWordIndex.get(subString) != null) slangWordIndex.get(subString).remove(slangWord);
        }
    }

    public void deleteSlangWord(String slangWord) {
        if (slangHashMap.containsKey(slangWord)) {
            removeKeywordsByDefinition(slangWord);
            removeKeywordIndex(slangWord);
            slangHashMap.remove(slangWord);

            saveIndexDictionaryData();
            removeFromListModel(slangWord);
        }
    }

    public void resetDataToDefault(){
        File file = new File(INDEX_FILE_NAME);
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

        historyManager.resetHistory();
        historyManager.loadHistory();

        randomSlangWordToday();
    }

    public void loadAllSlangWordsToSearchResult(){
        for (Map.Entry<String, String> entry : slangHashMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            listModel.addElement(key + " - " + value);
        }
    }

    private void updateListModel(String slangWord, String newDefinition) {
        for (int i = 0; i < listModel.getSize(); i++) {
            String element = listModel.getElementAt(i);
            if (element.startsWith(slangWord + " - ")) {
                listModel.set(i, slangWord + " - " + newDefinition);
                break;
            }
        }
    }

    private void removeFromListModel(String slangWord) {
        for (int i = 0; i < listModel.getSize(); i++) {
            String element = listModel.getElementAt(i);
            if (element.startsWith(slangWord + " - ")) {
                listModel.remove(i);
                break;
            }
        }
    }

}
