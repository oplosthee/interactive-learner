package interactivelearner.featureselection;

import interactivelearner.util.Category;
import interactivelearner.util.Corpus;

import java.util.*;
import java.util.stream.Collectors;

public class ChiSquare {

    private static final int HIGHEST = 300;

    public static List<String> createVocabulary(Corpus corpus) {
        Set<String> words = corpus.getWords();
        HashMap<String, Double> wordScores = new HashMap<>();
        for (String word : words) {
            wordScores.put(word, chiSquareValue(word, corpus));
        }
        return getHighestValues(wordScores);
    }

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

    private static List<String> getHighestValues(HashMap<String, Double> wordScores) {
        List<String> result = new ArrayList<>();
        Set<Double> chiValues = new HashSet<>(wordScores.values());
        List<Double> sortedValues = new ArrayList<>(chiValues);
        sortedValues.sort(Collections.reverseOrder());
        for (int i = 0; i < HIGHEST; i++) {
            result.addAll(getKeysByValue(wordScores, sortedValues.get(i)));
        }
        return result;
    }

    private static Set<String> getKeysByValue(HashMap<String, Double> map, Double value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }


}
