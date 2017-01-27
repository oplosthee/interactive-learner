package interactivelearner.data;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Document {

    private HashMap<String, Integer> words = new HashMap<>();

    public Document(List<String> words) {
        indexWords(words);
    }

    public int getWordFrequency(String word) {
        if (words.get(word) == null) {
            return 0;
        }

        return words.get(word);
    }

    public int getWordCount() {
        return words.keySet().size();
    }

    public boolean containsWord(String word) {
        return words.containsKey(word);
    }

    public Set<String> getWords() {
        return words.keySet();
    }

    public HashMap<String, Integer> getMap() {
        return words;
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
