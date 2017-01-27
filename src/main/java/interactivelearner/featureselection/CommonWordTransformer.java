package interactivelearner.featureselection;

import interactivelearner.data.Document;
import interactivelearner.util.TransformingUtil;

import java.util.*;
import java.util.stream.Collectors;

public class CommonWordTransformer implements DocumentTransformer {

    private int percentage;

    @Override
    public List<Document> run(List<Document> documents) {
        Map<String, Integer> documentsTotalWords = TransformingUtil.mergeDocumentMaps(documents);
        int wordCount = (int) (documentsTotalWords.size() * ((double) percentage / 100));

        Set<String> commonWords = documentsTotalWords.entrySet()
                .stream()
                .sorted((o1, o2) -> -Double.compare(o1.getValue(), o2.getValue()))
                .map(Map.Entry::getKey)
                .limit(wordCount)
                .collect(Collectors.toSet());

        for (Document document : documents) {
            for (String word : commonWords) {
                document.getMap().remove(word);
            }
        }

        return documents;
    }

    public CommonWordTransformer(int percentage) {
        this.percentage = percentage;
    }
}
