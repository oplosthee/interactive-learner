package interactivelearner.util;

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

    public int totalWords() {
        int count = 0;
        for (Document document : documents) {
            count += document.totalWords();
        }
        return count;
    }

    public int wordCount(String word) {
        int count = 0;
        for (Document document : documents) {
            count += document.wordCount(word);
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
