package interactivelearner.featureselection;

import interactivelearner.data.Document;
import interactivelearner.util.FileProcessor;

import java.io.IOException;
import java.util.*;

public class StopWordTransformer implements DocumentTransformer {

    private LinkedHashSet<String> stopWords = null;

    @Override
    public List<Document> run(List<Document> documents) {
        if (stopWords == null) {
            return documents;
        }

        List<Document> modifiedDocuments = new ArrayList<>();

        for (Document document : documents) {
            for (String stopWord : stopWords) {
                document.getMap().remove(stopWord);
            }

            modifiedDocuments.add(document);
        }

        return modifiedDocuments;
    }

    public StopWordTransformer(String stopWordsPath) {
        try {
            String stopWordsFile = FileProcessor.readFile(stopWordsPath);
            String[] words = stopWordsFile
                    .replace("\n", " ")
                    .replaceAll("\r", " ")
                    .split(" ");
            stopWords = new LinkedHashSet<>(Arrays.asList(words));
        } catch (IOException e) {
            System.err.println("Could not read stopwords.txt file");
        }
    }
}
