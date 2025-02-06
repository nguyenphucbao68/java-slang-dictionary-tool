package core;

import java.io.*;
import java.util.*;

public class HistoryManager {
    private final String HISTORY_FILE = "history.txt";
    private final List<String> history = new ArrayList<>();

    private void addHistory(String word) {
        history.add(word);
        saveHistory();
    }

    public List<String> getHistory() {
        return history;
    }

    private void saveHistory() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(HISTORY_FILE, true))) {
            bw.write(history.get(history.size() - 1));
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving history: " + e.getMessage());
        }
    }

    public void loadHistory() {
        history.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                history.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error loading history: " + e.getMessage());
        }
    }

    public void resetHistory(){

        history.clear();

        File file = new File(HISTORY_FILE);
        file.delete();
    }
}
