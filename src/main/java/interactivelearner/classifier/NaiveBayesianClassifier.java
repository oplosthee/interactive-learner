package interactivelearner.classifier;

import interactivelearner.data.Category;
import interactivelearner.data.Corpus;
import interactivelearner.data.Document;
import interactivelearner.featureselection.ChiSquare;

import java.util.*;

public class NaiveBayesianClassifier implements Classifier {

    private List<String> vocabulary;
    private Map<Category, Double> prior = new HashMap<>();
    private Map<String, Map<Category, Double>> condprob = new HashMap<>();


    @Override
    public void train(Corpus corpus) {
        vocabulary = ChiSquare.createVocabulary(corpus);
        int documentCount = corpus.getDocumentCount();

        for (Category category : corpus.getCategories()) {
            int documentsInClass = category.totalDocuments();
            prior.put(category, (double) documentsInClass / (double) documentCount);
            int wordCount = category.getWords().size();

            for (String word : vocabulary) {
                int wordCountInCategory = category.wordCount(word);
                // TODO: Compute in log space.
                // TODO: Fix calculation of conditional probabilities.
                double value = ((double) (wordCountInCategory + 1)) / ((double) (wordCount + 1));

                if (condprob.containsKey(word)) {
                    condprob.get(word).put(category, value);
                } else {
                    HashMap<Category, Double> result = new HashMap<>();
                    result.put(category, value);
                    condprob.put(word, result);
                }
            }
        }
    }

    @Override
    public Category classify(Corpus corpus, Document document) {
        Map<Category, Double> scores = new HashMap<>();

        for (Category category : corpus.getCategories()) {
            for (String word : document.getWords()) {
                if (vocabulary.contains(word)) {
                    Double value = prior.get(category);
                    value += condprob.get(word).get(category);
                    scores.put(category, value);
                }
            }
        }

        Optional<Category> category = scores.entrySet()
                .stream()
                .sorted((o1, o2) -> -Double.compare(o1.getValue(), o2.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();

        return category.orElse(null);
    }
}
