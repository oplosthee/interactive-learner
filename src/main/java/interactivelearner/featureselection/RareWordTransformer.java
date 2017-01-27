package interactivelearner.featureselection;

import interactivelearner.data.Document;
import interactivelearner.util.TransformingUtil;

import java.util.*;
import java.util.stream.Collectors;

public class RareWordTransformer implements DocumentTransformer  {

    private int percentage;

    @Override
    public List<Document> run(List<Document> documents) {
        Map<String, Integer> documentsTotalWords = TransformingUtil.mergeDocumentMaps(documents);
        int wordCount = (int) (documentsTotalWords.size() * ((double) percentage / 100));

        Set<String> rareWords = documentsTotalWords.entrySet()
                .stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .limit(wordCount)
                .collect(Collectors.toSet());

        for (Document document : documents) {
            for (String word : rareWords) {
                document.getMap().remove(word);
            }
        }

        return documents;
    }

    public RareWordTransformer(int percentage) {
        this.percentage = percentage;
    }

}
