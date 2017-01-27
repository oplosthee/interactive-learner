package interactivelearner.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Category {

    private String name;
    private List<Document> documents;

    public Category(String name, List<Document> documents) {
        this.documents = documents;
        this.name = name;
    }

    public Category(String name) {
        this(name, new ArrayList<>());
    }

    public void addDocument(Document document) {
        documents.add(document);
    }

    public void addDocuments(List<Document> documents) {
        this.documents.addAll(documents);
    }

    public int getWordCount() {
        int count = 0;
        for (Document document : documents) {
            count += document.getWordCount();
        }
        return count;
    }

    public int getWordFrequency(String word) {
        int count = 0;
        for (Document document : documents) {
            count += document.getWordFrequency(word);
        }
        return count;
    }

    public int totalDocuments() {
        return documents.size();
    }

    public int documentCount(String word) {
        int count = 0;
        for (Document document : documents) {
            if (document.containsWord(word)) {
                count++;
            }
        }
        return count;
    }

    public String getName() {
        return name;
    }

    public Set<String> getWords() {
        return documents.stream()
                .flatMap(d -> d.getWords().stream())
                .collect(Collectors.toSet());
    }
}
