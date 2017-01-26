package interactivelearner.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Corpus {

    private List<Category> categories;

    public Corpus() {
        this.categories = new ArrayList<>();
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public List<Category> getCategories() {
        return categories;
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
}
