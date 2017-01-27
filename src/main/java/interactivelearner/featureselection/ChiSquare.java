package interactivelearner.featureselection;

import interactivelearner.data.Category;
import interactivelearner.data.Corpus;

import java.util.*;
import java.util.stream.Collectors;

public class ChiSquare {

    /**
     * Creates a list of words to be used for processing text documents.
     * @param corpus the corpus to be used for selecting the meaningful words
     * @return list of meaningful words
     */
    public static List<String> createVocabulary(Corpus corpus) {
        Set<String> words = corpus.getWords();
        HashMap<String, Double> wordScores = new HashMap<>();
        for (String word : words) {
            wordScores.put(word, chiSquareValue(word, corpus));
        }
        return wordScores.entrySet()
                .stream()
                .sorted((o1, o2) -> -Double.compare(o1.getValue(), o2.getValue()))
                .map(Map.Entry::getKey)
                .limit(corpus.getVocabularyLength())
                .collect(Collectors.toList());
    }

    /**
     * Calculates the chi-square value of a word.
     * @param word the word for which the chi-square value needs to be calculated
     * @param corpus the word set in calculating the chi-square value
     * @return the chi-square value of the given word
     */
    private static double chiSquareValue(String word, Corpus corpus) {
        double result = 0;
        int categoryAmount = corpus.getCategories().size();
        int[][] table = new int[categoryAmount + 1][3];

        for (int i = 0; i < categoryAmount; i++) {
            Category category = corpus.getCategories().get(i);
            table[i][0] = category.documentCount(word);
            table[categoryAmount][0] += table[i][0];
            table[i][2] = category.totalDocuments();
            table[i][1] = table[i][2] - table[i][0];
            table[categoryAmount][1] += table[i][1];
        }

        table[categoryAmount][2] = table[categoryAmount][0] + table[categoryAmount][1];

        for (int i = 0; i < categoryAmount; i++) {
            for (int j = 0; j < 2; j++) {
                double expectedValue = ((double) table[categoryAmount][j] * (double) table[i][2]) / (double) table[categoryAmount][2];
                double realValue = table[i][j];
                result += Math.pow(realValue - expectedValue, 2) / expectedValue;
            }
        }
        return result;
    }
}
