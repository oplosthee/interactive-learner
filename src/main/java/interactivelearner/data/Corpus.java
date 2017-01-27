package interactivelearner.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Corpus {

    private List<Category> categories;
    private int maximumVocabularyLength;

    public Corpus(int maximumVocabularyLength) {
        this.categories = new ArrayList<>();
        this.maximumVocabularyLength = maximumVocabularyLength;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Category getCategory(String name) {
        for (Category category : categories) {
            if (Objects.equals(category.getName(), name)) {
                return category;
            }
        }
        return null;
    }

    /**
     * Returns the amount of documents in the Corpus
     *
     * @return the amount of documents in the Corpus
     */
    public int getDocumentCount() {
        int count = 0;

        for (Category category : categories) {
            count += category.totalDocuments();
        }

        return count;
    }

    public Set<String> getWords() {
        return categories.stream()
            .flatMap(d -> d.getWords().stream())
            .collect(Collectors.toSet());
    }

    public int getWordCount() {
        int count = 0;

        for (Category category : categories) {
            count += category.getWordCount();
        }

        return count;
    }

    public int getVocabularyLength() {
        return maximumVocabularyLength;
    }
}
