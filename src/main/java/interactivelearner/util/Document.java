package interactivelearner.util;

import java.util.HashMap;
import java.util.List;

public class Document {

    private HashMap<String, Integer> words = new HashMap<>();

    public Document(List<String> words) {
        indexWords(words);
    }

    public int wordCount(String word) {
        return words.get(word);
    }

    public int totalWords() {
        return words.keySet().size();
    }

    private void indexWords(List<String> words) {
        for (String word : words) {
            if (this.words.containsKey(word)) {
                this.words.put(word, this.words.get(word) + 1);
            } else {
                this.words.put(word, 1);
            }
        }
    }
}
