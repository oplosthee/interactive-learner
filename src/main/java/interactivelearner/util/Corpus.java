package interactivelearner.util;

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

    public Set<String> getWords() {
        return categories.stream()
            .flatMap(d -> d.getWords().stream())
            .collect(Collectors.toSet());
    }
}
