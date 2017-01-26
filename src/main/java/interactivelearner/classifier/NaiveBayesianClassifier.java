package interactivelearner.classifier;

import interactivelearner.data.Category;
import interactivelearner.data.Corpus;
import interactivelearner.data.Document;
import interactivelearner.featureselection.ChiSquare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NaiveBayesianClassifier implements Classifier {

    private List<String> vocabulary;
    private Map<Category, Double> priorProbabilities = new HashMap<>();
    private Map<String, Map<Category, Double>> conditionalProbabilities = new HashMap<>();

    private int smoothingFactor;

    public NaiveBayesianClassifier(int smoothingFactor) {
        this.smoothingFactor = smoothingFactor;
    }

    @Override
    public void train(Corpus corpus) {
        vocabulary = ChiSquare.createVocabulary(corpus);
        int documentCount = corpus.getDocumentCount();

        for (Category category : corpus.getCategories()) {
            int documentsInCategory = category.totalDocuments();
            priorProbabilities.put(category, (double) documentsInCategory / (double) documentCount);
            int categoryWordCount = category.getWordCount();

            for (String word : vocabulary) {
                int wordFrequencyInCategory = category.getWordFrequency(word);
                double probability = ((double) (wordFrequencyInCategory + smoothingFactor)) /
                        ((double) (categoryWordCount + smoothingFactor * corpus.getWordCount()));

                if (conditionalProbabilities.containsKey(word)) {
                    conditionalProbabilities.get(word).put(category, probability);
                } else {
                    HashMap<Category, Double> result = new HashMap<>();
                    result.put(category, probability);
                    conditionalProbabilities.put(word, result);
                }
            }
        }
    }

    @Override
    public Category classify(Corpus corpus, Document document) {
        Map<Category, Double> scores = new HashMap<>();
        scores.putAll(priorProbabilities);

        for (Category category : corpus.getCategories()) {
            for (String word : document.getWords()) {
                if (vocabulary.contains(word)) {
                    Double value = scores.get(category);
                    value += Math.log(conditionalProbabilities.get(word).get(category));
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
