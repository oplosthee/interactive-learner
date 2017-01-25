package interactivelearner.featureselection;

import interactivelearner.util.Document;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Tokenizer {

    public static Document processString(String text) {
        String[] words = text.split(" ");

        List<String> result = Arrays.stream(words)
                .map(s -> s.toLowerCase().replaceAll("[^a-zA-Z]", ""))
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        return new Document(result);
    }

}